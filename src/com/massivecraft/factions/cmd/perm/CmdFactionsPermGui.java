package com.massivecraft.factions.cmd.perm;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.action.ActionOpenRelations;
import com.massivecraft.factions.action.ActionRelationModify;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.*;
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

import java.util.List;
import java.util.stream.Collectors;

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
        // Verify
        if ( ! MPerm.getPermPerms().has(msender, msenderFaction, true)) return;

        // Args
        List<FactionPermission> factionPermissions = MPermColl.get().getAll().stream().map(mperm -> new FactionPermission(mperm.getName(), Material.ENCHANTED_BOOK)).collect(Collectors.toList());
        Inventory inventory = Bukkit.createInventory(null, 54, Txt.parse("<gray>Faction Permissions"));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);
        int slot = 0;

        // Loop - Permissions
        for (FactionPermission permission : factionPermissions)
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

    public Inventory getPermissionGui(MPlayer mplayer, MPerm mperm)
    {
        Faction faction = mplayer.getFaction();
        Inventory inventory = Bukkit.createInventory(null, 9, Txt.parse("<gray>Editing %s", mperm.getName()));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);

        int slot = 0;
        for (Rel rel : Rel.values())
        {
            boolean status = faction.isPermitted(mperm.getName(), rel);
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
            if (rel != Rel.LEADER) chestGui.setAction(slot, new ActionRelationModify(mplayer, !status, mperm, rel));
            slot++;
        }

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

}
