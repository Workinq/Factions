package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionUpdateSandAlts extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final MPlayer mplayer;
    private final boolean pause;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionUpdateSandAlts(Faction faction, MPlayer mplayer, boolean pause)
    {
        this.faction = faction;
        this.mplayer = mplayer;
        this.pause = pause;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        if (pause)
        {
            faction.stopAllSandAlts();
        }
        else
        {
            faction.startAllSandAlts();
        }
        mplayer.msg("%s <h>%s <i>all active sand alts.", mplayer.describeTo(mplayer, true), pause ? "stopped" : "started");
        return true;
    }

}
