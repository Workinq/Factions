package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.event.EventFactionsTntChange;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.upgrade.UpgradesManager;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.SpawnerSpawnEvent;

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
    public void onTntDrop(SpawnerSpawnEvent event)
    {
        // Args
        EntityType type = event.getEntityType();
        if ( ! MConf.get().tntChances.containsKey(type)) return;
        if ( ! MConf.get().entityTypesTnt.contains(type)) return;

        // Cancel
        event.setCancelled(true);

        // Location
        Location location = event.getLocation();
        if (location == null) return;

        Faction at = BoardColl.get().getFactionAt(PS.valueOf(location));
        if (at.isSystemFaction()) return;

        PS chunk = PS.valueOf(location.getChunk());
        if ( ! at.hasBaseRegion() ) return;
        if ( ! at.getBaseRegion().contains(chunk)) return;

        if (at.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) == 0) return;

        // Args
        int maximumTnt = Integer.parseInt(UpgradesManager.get().getUpgradeByName(MUpgrade.get().tntUpgrade.getUpgradeName()).getCurrentDescription()[at.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) - 1].split(" ")[0].replaceAll(",", ""));
        int minimum = MConf.get().tntChances.get(type).get(0);
        int maximum = MConf.get().tntChances.get(type).get(1);
        int amount = ThreadLocalRandom.current().nextInt(minimum, maximum);

        EventFactionsTntChange tntEvent = new EventFactionsTntChange(at, amount, event.getSpawner().getLocation());
        tntEvent.run();
        if (tntEvent.isCancelled()) return;

        int newTnt = Math.min(at.getTnt() + tntEvent.getTnt(), maximumTnt);

        // Calculate excess TNT
        int excessTnt;

        if(newTnt > maximumTnt)
        {
            excessTnt = newTnt - maximumTnt;

            // Deposit
            Money.spawn(at, null, excessTnt * MConf.get().tntSellPrice);
        }

        // Apply
        at.setTnt(newTnt);
    }

}
