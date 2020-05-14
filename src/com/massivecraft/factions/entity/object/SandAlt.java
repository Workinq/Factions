package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

import java.util.UUID;

public class SandAlt extends EntityInternal<SandAlt>
{

    private final UUID npcId;
    private final String factionId;
    private final PS location;
    private boolean paused;

    public SandAlt(UUID npcId, String factionId, Location location)
    {
        this.npcId = npcId;
        this.factionId = factionId;
        this.location = PS.valueOf(location);
        this.paused = false;
    }

    public UUID getNpcId()
    {
        return npcId;
    }

    public String getFactionId()
    {
        return factionId;
    }

    public PS getPs()
    {
        return location;
    }

    public Location getLocation()
    {
        return location.asBukkitLocation(true);
    }

    public void setPaused(boolean paused)
    {
        // Detect Nochange
        if (this.paused == paused) return;

        // Apply
        this.paused = paused;

        // Mark as changed.
        this.changed();
    }

    public boolean isPaused()
    {
        return paused;
    }

}
