package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.event.EventFactionsTntChange;
import com.massivecraft.factions.upgrade.UpgradesManager;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.concurrent.ThreadLocalRandom;

public class EngineTnt extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineTnt i = new EngineTnt();
    public static EngineTnt get() { return i; }

    // -------------------------------------------- //
    // TNT
    // -------------------------------------------- //

    @EventHandler
    public void onShardDrop(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) return;

        // Args
        EntityType type = event.getEntityType();
        if ( ! MConf.get().tntChances.containsKey(type)) return;
        if ( ! MConf.get().entityTypesTnt.contains(type)) return;

        Location location = event.getLocation();
        if (location == null) return;

        Faction at = BoardColl.get().getFactionAt(PS.valueOf(location));
        if (at.isSystemFaction()) return;

        PS chunk = PS.valueOf(location.getChunk());
        if (at.getBaseRegion() == null) return;
        if ( ! at.getBaseRegion().contains(chunk)) return;

        // Cancel
        event.setCancelled(true);

        if (at.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) == 0) return;

        // Args
        int maximumTnt = Integer.parseInt(UpgradesManager.get().getUpgradeByName(MUpgrade.get().tntUpgrade.getUpgradeName()).getCurrentDescription()[at.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) - 1].split(" ")[0].replaceAll(",", ""));
        int minimum = MConf.get().tntChances.get(type).get(0);
        int maximum = MConf.get().tntChances.get(type).get(1);
        int amount = ThreadLocalRandom.current().nextInt(minimum, maximum);

        EventFactionsTntChange tntEvent = new EventFactionsTntChange(at, amount);
        tntEvent.run();
        if (tntEvent.isCancelled()) return;

        // Apply
        at.setTnt(Math.min(at.getTnt() + tntEvent.getTnt(), maximumTnt));
    }

}
