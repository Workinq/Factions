package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

public class SandAlt extends EntityInternal<SandAlt>
{

    private final String npcId;
    private final String factionId;
    private final PS location;
    private boolean paused;

    public SandAlt(String npcId, String factionId, Location location)
    {
        this.npcId = npcId;
        this.factionId = factionId;
        this.location = PS.valueOf(location);
        this.paused = false;
    }

    public String getNpcId()
    {
        return npcId;
    }

    public String getFactionId()
    {
        return factionId;
    }

    public PS getLocation()
    {
        return location;
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
