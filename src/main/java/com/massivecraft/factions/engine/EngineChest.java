package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.object.AuditAction;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.factions.entity.object.ChestAction;
import com.massivecraft.factions.event.EventFactionsNameChange;
import com.massivecraft.factions.util.AuditUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EngineChest extends Engine
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Map<HumanEntity, ItemStack[]> containers = new HashMap<>();

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineChest i = new EngineChest();
    public static EngineChest get() { return i; }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if ( ! this.isFactionChest(event.getView().getTitle())) return;

        Faction faction = this.getFactionFromTitle(event.getView().getTitle());
        if (faction == null) return;

        containers.put(event.getPlayer(), this.compressInventory(event.getInventory().getContents()));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if ( ! this.isFactionChest(event.getView().getTitle())) return;

        Faction faction = this.getFactionFromTitle(event.getView().getTitle());
        if (faction == null) return;

        int page = this.getPageFromTitle(event.getView().getTitle());
        faction.saveChest(page);

        HumanEntity player = event.getPlayer();
        ItemStack[] before = containers.get(player);

        if (before == null) return;

        ItemStack[] after = this.compressInventory(event.getInventory().getContents());
        ItemStack[] compareInventories = this.compareInventories(before, after);
        for (ItemStack item : compareInventories)
        {
            faction.addChestAction(new ChestAction(player.getUniqueId().toString(), System.currentTimeMillis(), item, page));

            // Mirror the transaction into the unified audit log (negative amount = taken out).
            int amount = item.getAmount();
            AuditUtil.log(AuditCategory.CHEST, amount < 0 ? AuditAction.CHEST_TAKE : AuditAction.CHEST_PUT,
                player, faction, null,
                AuditUtil.details()
                    .put("item", Txt.getItemName(item))
                    .put("amount", String.valueOf(Math.abs(amount)))
                    .put("page", String.valueOf(page))
                    .map());
        }

        containers.remove(player);
    }

    @EventHandler
    public void onFactionNameChange(EventFactionsNameChange event)
    {
        Faction faction = event.getFaction();

        for (int page = 1; page <= faction.getChestCount(); page++)
        {
            Inventory old = faction.getChest(page);

            for (HumanEntity entity : old.getViewers()) entity.closeInventory();

            String title = Txt.parse("<gray>%s - Faction Chest", event.getNewName());
            if (page > 1) title += " #" + page;

            Inventory renamed = Bukkit.createInventory(null, old.getSize(), title);
            renamed.setContents(old.getContents());
            faction.setChest(page, renamed);
            old.clear();
        }
    }

    private class ItemStackComparator implements Comparator<ItemStack>
    {
        @Override
        public int compare(ItemStack first, ItemStack second)
        {
            int firstTypeId = first.getType().ordinal();
            int secondTypeId = second.getType().ordinal();

            if (firstTypeId < secondTypeId) return -1;
            if (firstTypeId > secondTypeId) return 1;

            short firstData = rawData(first);
            short secondData = rawData(second);
            return Short.compare(firstData, secondData);
        }
    }

    private short rawData(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();
        return (meta instanceof Damageable) ? (short) ((Damageable) meta).getDamage() : 0;
    }

    private ItemStack[] compareInventories(ItemStack[] firstItems, ItemStack[] secondItems)
    {
        ItemStackComparator comparator = new ItemStackComparator();
        List<ItemStack> difference = new ArrayList<>();
        int firstCounter = 0, secondCounter = 0;
        while (firstCounter < firstItems.length || secondCounter < secondItems.length)
        {
            if (firstCounter >= firstItems.length)
            {
                difference.add(secondItems[secondCounter]);
                secondCounter++;
            }
            else if (secondCounter >= secondItems.length)
            {
                firstItems[firstCounter].setAmount(firstItems[firstCounter].getAmount() * -1);
                difference.add(firstItems[firstCounter]);
                firstCounter++;
            }
            else
            {
                int comp = comparator.compare(firstItems[firstCounter], secondItems[secondCounter]);
                if (comp < 0)
                {
                    firstItems[firstCounter].setAmount(firstItems[firstCounter].getAmount() * -1);
                    difference.add(firstItems[firstCounter]);
                    firstCounter++;
                }
                else if (comp > 0)
                {
                    difference.add(secondItems[secondCounter]);
                    secondCounter++;
                }
                else
                {
                    int amount = secondItems[secondCounter].getAmount() - firstItems[firstCounter].getAmount();
                    if (amount != 0)
                    {
                        firstItems[firstCounter].setAmount(amount);
                        difference.add(firstItems[firstCounter]);
                    }
                    firstCounter++;
                    secondCounter++;
                }
            }
        }
        return difference.toArray(new ItemStack[0]);
    }

    private ItemStack[] compressInventory(ItemStack[] items)
    {
        List<ItemStack> compressed = new ArrayList<>();
        for (ItemStack item : items)
        {
            // Verify
            if (item == null) continue;

            // Args
            Material type = item.getType();
            short data = rawData(item);
            boolean found = false;

            for (ItemStack compressedItem : compressed)
            {
                if (type == compressedItem.getType() && data == rawData(compressedItem))
                {
                    compressedItem.setAmount(compressedItem.getAmount() + item.getAmount());
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                ItemStack created = new ItemStack(type, item.getAmount());
                if (data != 0)
                {
                    ItemMeta meta = created.getItemMeta();
                    if (meta instanceof Damageable)
                    {
                        ((Damageable) meta).setDamage(data);
                        created.setItemMeta(meta);
                    }
                }
                compressed.add(created);
            }
        }
        compressed.sort(new ItemStackComparator());
        return compressed.toArray(new ItemStack[0]);
    }

    private Faction getFactionFromTitle(String title)
    {
        try
        {
            return FactionColl.get().getByName(ChatColor.stripColor(title).split("-")[0].trim());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private boolean isFactionChest(String title)
    {
        if (title == null) return false;
        return ChatColor.stripColor(title).contains(" - Faction Chest");
    }

    private int getPageFromTitle(String title)
    {
        String stripped = ChatColor.stripColor(title);
        int hashIndex = stripped.lastIndexOf('#');
        if (hashIndex < 0) return 1;
        try
        {
            return Integer.parseInt(stripped.substring(hashIndex + 1).trim());
        }
        catch (NumberFormatException e)
        {
            return 1;
        }
    }

}
