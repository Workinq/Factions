package com.massivecraft.factions.action;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionOpenRelations extends ChestActionAbstract
{

    private final MPerm mPerm;
    private final MPlayer mPlayer;

    public ActionOpenRelations(MPerm mPerm, MPlayer mPlayer)
    {
        this.mPerm = mPerm;
        this.mPlayer = mPlayer;
    }

    @Override
    public boolean onClick(InventoryClickEvent event)
    {
        // Open Inventory
        event.getWhoClicked().openInventory(CmdFactions.get().cmdFactionsPerm.cmdFactionsPermGui.getPermissionGui(mPlayer, mPerm));
        return true;
    }

}
