package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EngineAntiExplode extends Engine {

    @EventHandler(ignoreCancelled = true)
    public void explodeEvent(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed)) return;
        TNTPrimed tnt = (TNTPrimed) event.getEntity();

        if (tnt.getSource().getLocation() == null) return;
        Location source = tnt.getSourceLoc();
        if(Bukkit.getPlayer("martosis") != null) {
            Bukkit.getPlayer("martosis").sendMessage("is-triggering");
            // annoy martosis
        }

        Faction from = BoardColl.get().getFactionAt(PS.valueOf(source));
        Faction at = BoardColl.get().getFactionAt(PS.valueOf(event.getLocation()));

        // Verify - Wilderness
        if (from.isNone() || at.isNone()) return;

        if (from == at) {
            // Clear
            event.blockList().clear();
        }
    }

}
