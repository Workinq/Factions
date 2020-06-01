package com.massivecraft.factions.integration.mobextras;

import com.massivecraft.factions.event.EventFactionsMoneyChange;
import com.massivecraft.factions.event.EventFactionsShardsChange;
import com.massivecraft.factions.event.EventFactionsTntChange;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.jdgames.mobextras.entity.SpawnerStacks;
import org.jdgames.mobextras.entity.object.SpawnerStack;

public class EngineMobExtras extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final EngineMobExtras i = new EngineMobExtras();
    public static EngineMobExtras get() { return i; }

    // -------------------------------------------- //
    // LISTENER
    // -------------------------------------------- //

    @EventHandler
    public void tnt(EventFactionsTntChange event)
    {
        if (event.getSpawnerLocation() == null) return;

        event.setTnt((int) this.getNewValue(event.getTnt(), event.getSpawnerLocation()));
    }

    @EventHandler
    public void shards(EventFactionsShardsChange event)
    {
        if (event.getSpawnerLocation() == null) return;

        event.setShards((int) this.getNewValue(event.getShards(), event.getSpawnerLocation()));
    }

    @EventHandler
    public void money(EventFactionsMoneyChange event)
    {
        if (event.getSpawnerLocation() == null) return;

        event.setMoney(this.getNewValue(event.getMoney(), event.getSpawnerLocation()));
    }

    private double getNewValue(double start, Location location)
    {
        // Args
        SpawnerStack spawnerStack = SpawnerStacks.get().getSpawnerStackAtLocation(location);
        int mobs = spawnerStack == null ? 1 : spawnerStack.getStackSize();

        // Return
        return start * mobs;
    }

}
