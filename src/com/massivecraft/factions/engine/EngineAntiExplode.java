package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import net.convictmc.augustegusteau.api.event.TNTPreExplodeEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;

public class EngineAntiExplode extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineAntiExplode i = new EngineAntiExplode();
    public static EngineAntiExplode get() { return i; }

    @EventHandler(ignoreCancelled = true)
    public void explodeEvent(TNTPreExplodeEvent event) {
        final Location source = event.getTntPrimed().getSourceLoc();

        Faction from = BoardColl.get().getFactionAt(PS.valueOf(source));
        Faction at = BoardColl.get().getFactionAt(PS.valueOf(event.getTntPrimed().getLocation()));

        // Verify - Wilderness
        if (from.isNone() || at.isNone()) return;

        if (from == at) {
            // Remove damage blocks
            event.setDamageBlocks(false);
        }
    }
}
