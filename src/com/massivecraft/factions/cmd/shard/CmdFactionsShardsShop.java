package com.massivecraft.factions.cmd.shard;

import com.massivecraft.factions.action.ActionPurchaseItem;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MShop;
import com.massivecraft.factions.entity.conf.ConfItem;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdFactionsShardsShop extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShardsShop()
    {
        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MPerm.getPermShards().has(msender, msenderFaction, true)) return;

        me.openInventory(getShopGui());
    }

    private Inventory getShopGui()
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, 54, Txt.parse("<gray>Faction Shop"));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        int slot = 0;

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        // Items
        for (ConfItem confItem : MShop.get().items)
        {
            // Args
            ItemStack item = new ItemStack(confItem.getItemType(), 1, (short) confItem.getItemData());
            ItemMeta meta = item.getItemMeta();

            // Setup
            meta.setDisplayName(Txt.parse(confItem.getItemName()));
            meta.setLore(Txt.parse(confItem.getItemLore()));
            item.setItemMeta(meta);

            // Add
            chestGui.getInventory().setItem(slot, item);
            chestGui.setAction(slot, new ActionPurchaseItem(msenderFaction, msender, confItem));
            slot++;
        }

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

}
