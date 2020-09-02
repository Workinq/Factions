package com.massivecraft.factions.action;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.mixin.MixinCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionRelationModify extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final MPlayer mplayer;
    private final boolean newStatus;
    private final MPerm mperm;
    private final Rel rel;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionRelationModify(MPlayer mplayer, boolean newStatus, MPerm mperm, Rel rel)
    {
        this.mplayer = mplayer;
        this.newStatus = newStatus;
        this.mperm = mperm;
        this.rel = rel;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Verify
        if (rel.isMoreThan(mplayer.getRole()))
        {
            mplayer.msg("<b>You can't modify permissions for roles higher than yours.");
            return true;
        }

        // Execute
        MixinCommand.get().dispatchCommand(player, "f perm set " + mperm.getName() + " " + rel.getName() + " " + (newStatus ? "yes" : "no"));

        // Open Inventory
        player.openInventory(CmdFactions.get().cmdFactionsPerm.cmdFactionsPermGui.getPermissionGui(mplayer, mperm));
        return true;
    }

}
