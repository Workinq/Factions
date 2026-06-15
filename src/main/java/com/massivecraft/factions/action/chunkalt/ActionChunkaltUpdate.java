package com.massivecraft.factions.action.chunkalt;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionChunkaltUpdate extends ChestActionAbstract
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

    public ActionChunkaltUpdate(Faction faction, MPlayer mplayer, boolean pause)
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
            faction.stopAllChunkAlts();
        }
        else
        {
            faction.startAllChunkAlts();
        }
        mplayer.msg("%s <h>%s <i>all active chunk alts.", mplayer.describeTo(mplayer, true), pause ? "stopped" : "started");
        return true;
    }

}
