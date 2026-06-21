package com.massivecraft.factions.integration.essentials;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import net.ess3.api.events.UserTeleportHomeEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class EngineEssentials extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final EngineEssentials i = new EngineEssentials();
    public static EngineEssentials get() { return i; }

    // -------------------------------------------- //
    // HOME CHECK
    // -------------------------------------------- //

    @EventHandler(ignoreCancelled = true)
    public void onTeleportHome(UserTeleportHomeEvent event)
    {
        Location to = event.getHomeLocation();
        if (to == null) return;

        // Args
        Player player = event.getUser().getBase();
        MPlayer mplayer = MPlayer.get(player);
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(to));

        // Verify
        if (faction.isNone() || faction == mplayer.getFaction()) return;

        // Cancel
        event.setCancelled(true);

        // Inform
        mplayer.msg("<b>You can't teleport there as it's in forbidden territory.");
    }

}
