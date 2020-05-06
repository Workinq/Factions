package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.ChestAction;
import com.massivecraft.factions.event.EventFactionsNameChange;
import com.massivecraft.factions.util.SerializationUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EngineChest extends Engine
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private Map<HumanEntity, ItemStack[]> containers = new HashMap<>();

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineChest i = new EngineChest();
    public static EngineChest get() { return i; }

    public static short rawData(ItemStack item)
    {
        return item.getType() != null ? (item.getData() != null ? item.getDurability() : 0) : 0;
    }

    public ItemStack[] compareInventories(ItemStack[] firstItems, ItemStack[] secondItems)
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

    public ItemStack[] compressInventory(ItemStack[] items)
    {
        List<ItemStack> compressed = new ArrayList<>();
        for (ItemStack item : items)
        {
            if (item == null) continue;
            int type = item.getTypeId();
            short data = rawData(item);
            boolean found = false;
            for (ItemStack compressedItem : compressed)
            {
                if (type == compressedItem.getTypeId() && data == rawData(compressedItem))
                {
                    compressedItem.setAmount(compressedItem.getAmount() + item.getAmount());
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                compressed.add(new ItemStack(type, item.getAmount(), data));
            }
        }
        Collections.sort(compressed, new ItemStackComparator());
        return compressed.toArray(new ItemStack[0]);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if ( ! event.getInventory().getName().endsWith(" - Faction Chest")) return;

        Faction faction = SerializationUtil.getFactionFromInventory(event.getInventory());
        if (faction == null) return;

        containers.put(event.getPlayer(), this.compressInventory(event.getInventory().getContents()));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if ( ! event.getInventory().getName().endsWith(" - Faction Chest")) return;

        Faction faction = SerializationUtil.getFactionFromInventory(event.getInventory());
        if (faction == null) return;

        faction.saveInventory();
        HumanEntity player = event.getPlayer();
        ItemStack[] before = containers.get(player);

        if (before == null) return;

        ItemStack[] after = this.compressInventory(event.getInventory().getContents());
        ItemStack[] compareInventories = this.compareInventories(before, after);
        for (ItemStack item : compareInventories)
        {
            faction.addChestAction(new ChestAction(player.getUniqueId().toString(), System.currentTimeMillis(), item));
        }
        containers.remove(player);
    }

    @EventHandler
    public void onFactionNameChange(EventFactionsNameChange event)
    {
        for (HumanEntity entity : event.getFaction().getInventory().getViewers())
        {
            entity.closeInventory();
        }
        Inventory old = event.getFaction().getInventory();
        event.getFaction().setInventory(Bukkit.createInventory(null, old.getSize(), Txt.parse("<gray>%s - Faction Chest", event.getNewName())));
        event.getFaction().getInventory().setContents(old.getContents());
        old.clear();
    }

    public static class ItemStackComparator implements Comparator<ItemStack>
    {
        @Override
        public int compare(ItemStack first, ItemStack second)
        {
            int firstTypeId = first.getTypeId();
            int secondTypeId = second.getTypeId();
            if (firstTypeId < secondTypeId)
            {
                return -1;
            }
            if (firstTypeId > secondTypeId)
            {
                return 1;
            }
            short firstData = rawData(first);
            short secondData = rawData(second);
            return Short.compare(firstData, secondData);
        }
    }

}
