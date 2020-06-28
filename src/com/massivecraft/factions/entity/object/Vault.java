package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.engine.EngineVault;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

public class Vault extends EntityInternal<Vault>
{

    public Vault(PS location, boolean damaged)
    {
        this.location = location;
        this.damaged = damaged;
        this.whenCanRepair = 0;
    }

    public long whenCanRepair;
    public void setWhenCanRepair(long time) { whenCanRepair = time; }
    public boolean getCanRepair() { return whenCanRepair < System.currentTimeMillis(); }

    public void repairVault() { EngineVault.get().vaultRepaired(this); }

    public boolean damaged;
    public boolean getIfDamaged() { return damaged; }
    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public final PS location;
    public PS getLocation() { return location; }

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
        Location bottom = location.clone().add(0, -3, 0);
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
