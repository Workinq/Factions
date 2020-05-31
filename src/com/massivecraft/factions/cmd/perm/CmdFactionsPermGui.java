package com.massivecraft.factions.cmd.perm;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.action.ActionOpenRelations;
import com.massivecraft.factions.action.ActionRelationModify;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.FPerm;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.FactionPermission;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdFactionsPermGui extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsPermGui()
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
        if (FPerm.get().factionPermissions.isEmpty()) FPerm.get().setupPermissions();

        // Verify
        if ( ! MPerm.getPermPerms().has(msender, msenderFaction, true)) return;

        // Args
        Inventory inventory = Bukkit.createInventory(null, 54, Txt.parse(FPerm.get().permissionGuiName));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        int slot = 0;

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        // Loop - Permissions
        for (FactionPermission permission : FPerm.get().factionPermissions)
        {
            // Args
            MPerm mPerm = permission.getMPerm();
            ItemStack item = new ItemStack(permission.getItemMaterial());
            ItemMeta meta = item.getItemMeta();

            // Verify
            if ( ! mPerm.isEditable()) continue;

            // Item Setup
            meta.setDisplayName(Txt.parse("<k>%s", permission.getMPermString()));
            meta.setLore(Txt.parse(MUtil.list("<n>Click here to modify what roles", "<n>have this permission")));
            item.setItemMeta(meta);
            chestGui.getInventory().setItem(slot, item);

            chestGui.setAction(slot, new ActionOpenRelations(permission.getMPerm(), msender));
            slot++;
        }

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Open
        me.openInventory(chestGui.getInventory());
    }

    public Inventory getPermissionGui(MPlayer mPlayer, MPerm mPerm)
    {
        Faction faction = mPlayer.getFaction();
        Inventory inventory = Bukkit.createInventory(null, 9, Txt.parse("<gray>Editing %s", mPerm.getName()));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        int slot = 0;
        for (Rel rel : Rel.values())
        {
            boolean status = faction.isPermitted(mPerm.getName(), rel);
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName((status ? ChatColor.GREEN : ChatColor.RED) + rel.getName());
            if (rel == Rel.LEADER)
            {
                meta.setLore(Txt.parse(MUtil.list("<n>You cannot modify permissions", "<n>for <g>your faction leader<n>.")));
            }
            else
            {
                if (status)
                {
                    meta.setLore(Txt.parse(MUtil.list("<n>Click to <b>deny <n>access to this permission")));
                }
                else
                {
                    meta.setLore(Txt.parse(MUtil.list("<n>Click to <g>allow <n>access to this permission")));
                }
            }
            item.setItemMeta(meta);
            item.setDurability((short) (status ? 5 : 14));
            chestGui.getInventory().setItem(slot, item);
            if (rel != Rel.LEADER) chestGui.setAction(slot, new ActionRelationModify(mPlayer, !status, mPerm, rel));
            slot++;
        }

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

}