package com.massivecraft.factions.action;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionOpenRelations extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final MPerm mPerm;
    private final MPlayer mplayer;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionOpenRelations(MPerm mPerm, MPlayer mplayer)
    {
        this.mPerm = mPerm;
        this.mplayer = mplayer;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Open Inventory
        player.openInventory(CmdFactions.get().cmdFactionsPerm.cmdFactionsPermGui.getPermissionGui(mplayer, mPerm));
        return true;
    }

}
