package com.massivecraft.factions.action;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.mixin.MixinCommand;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionRelationModify extends ChestActionAbstract {

    private final MPlayer mPlayer;
    private final boolean newStatus;
    private final MPerm mPerm;
    private final Rel rel;

    public ActionRelationModify(MPlayer mPlayer, boolean newStatus, MPerm mPerm, Rel rel)
    {
        this.mPlayer = mPlayer;
        this.newStatus = newStatus;
        this.mPerm = mPerm;
        this.rel = rel;
    }

    @Override
    public boolean onClick(InventoryClickEvent event)
    {
        // Verify
        if (rel.isMoreThan(mPlayer.getRole()))
        {
            mPlayer.msg("<b>You can't modify permissions for roles higher than yours.");
            return true;
        }

        // Execute
        MixinCommand.get().dispatchCommand(event.getWhoClicked(), "f perm set " + mPerm.getName() + " " + rel.getName() + " " + (newStatus ? "yes" : "no"));

        // Open Inventory
        event.getWhoClicked().openInventory(CmdFactions.get().cmdFactionsPerm.cmdFactionsPermGui.getPermissionGui(mPlayer, mPerm));
        return true;
    }

}
