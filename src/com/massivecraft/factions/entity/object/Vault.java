package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.engine.EngineVault;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

public class Vault extends EntityInternal<Vault>
{

    public Vault(String factionId, Location location, boolean damaged)
    {
        Faction faction = Faction.get(factionId);
        this.factionId = factionId;
        this.location = location;
        this.damaged = damaged;
        this.whenCanRepair = 0;
        faction.setVault(this);
    }

    public long whenCanRepair;
    public void setWhenCanRepair(long time) { whenCanRepair = time; }
    public boolean getCanRepair()
    {
        final long current = System.currentTimeMillis();
        return whenCanRepair < current;
    }

    public void repairVault() { EngineVault.get().vaultRepaired(this); }

    public String factionId;
    public String getFactionId() { return factionId; }

    public boolean damaged;
    public boolean getIfDamaged() { return damaged; }
    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public final Location location;
    public Location getLocation() { return location; }

    public boolean getHitBreakable(Location location)
    {
        for (int x = -3; x < 4; x++)
        {
            for (int y = -2; y < 4; y++)
            {
                for (int z = -3; z < 4; z++)
                {
                    if (location.clone().add(x, y, z) == location) return true;
                }
            }
        }
        return false;
    }

    public boolean getHitBottom(Location location)
    {
        final Location bottom = this.location.clone().add(0, -3, 0);
        for (int x = -3; x < 4; x++)
        {
            for (int z = -3; x < 4; x++)
            {
                if (bottom.clone().add(x, 0, z) == location) return true;
            }
        }
        return false;
    }

}
