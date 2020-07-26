package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsMoneyChange;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import java.util.concurrent.ThreadLocalRandom;

public class EngineMoney extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineMoney i = new EngineMoney();
    public static EngineMoney get() { return i; }

    // -------------------------------------------- //
    // MONEY
    // -------------------------------------------- //

    @EventHandler
    public void onMoneyDrop(SpawnerSpawnEvent event)
    {
        // Verify
        if ( ! MConf.get().autoSellMobs ) return;

        // Args
        EntityType type = event.getEntityType();
        if ( ! MConf.get().moneyChances.containsKey(type) ) return;
        if ( ! MConf.get().entityTypesMoney.contains(type) ) return;

        // Cancel
        event.setCancelled(true);

        Location location = event.getLocation();
        if (location == null) return;

        Faction at = BoardColl.get().getFactionAt(PS.valueOf(location));
        if (at.isSystemFaction()) return;

        PS chunk = PS.valueOf(location.getChunk());
        if ( ! at.hasBaseRegion() ) return;
        if ( ! at.getBaseRegion().contains(chunk)) return;

        // Args
        int minimum = MConf.get().moneyChances.get(type).get(0);
        int maximum = MConf.get().moneyChances.get(type).get(1);
        int amount = ThreadLocalRandom.current().nextInt(minimum, maximum);

        EventFactionsMoneyChange moneyEvent = new EventFactionsMoneyChange(at, amount, event.getSpawner().getLocation());
        moneyEvent.run();
        if (moneyEvent.isCancelled()) return;

        // Apply
        Money.spawn(at, null, moneyEvent.getMoney());
    }

}
