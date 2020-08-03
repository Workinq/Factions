package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CmdFactionsInvsee extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsInvsee()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");

        // Requirements
        this.addRequirements(ReqHasFaction.get());
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        MPlayer mplayer = this.readArg();

        if ( ! MPerm.getPermInvsee().has(msender, msenderFaction, true) ) return;

        if (mplayer == msender)
        {
            msg("<b>You cannot perform /f invsee on yourself.");
            return;
        }

        if ( mplayer.getPlayer() == null || ! mplayer.isOnline() )
        {
            msg("<b>You can only use /f invsee on online players.");
            return;
        }

        if ( ! msender.isOverriding() && mplayer.getFaction() != msenderFaction )
        {
            msg("<b>You can only use /f invsee on players in your faction.");
            return;
        }

        // Apply
        me.openInventory(this.getInvseeGui(mplayer.getPlayer()));

        // Inform
        msg("<i>Viewing %s's <i>inventory.", mplayer.describeTo(msender));
    }

    private Inventory getInvseeGui(Player player)
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, 45, Txt.parse("<gray>%s's Inventory", player.getName()));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory, false);

        // Inventory Contents
        chestGui.getInventory().setContents(player.getInventory().getContents());
        // Armor Contents
        chestGui.getInventory().setItem(36, player.getInventory().getHelmet());
        chestGui.getInventory().setItem(37, player.getInventory().getChestplate());
        chestGui.getInventory().setItem(38, player.getInventory().getLeggings());
        chestGui.getInventory().setItem(39, player.getInventory().getBoots());

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        return chestGui.getInventory();
    }

}
