package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

import java.util.UUID;

public class SandAlt extends EntityInternal<SandAlt>
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final UUID npcId;
    public UUID getNpcId() { return npcId; }

    private final String factionId;
    public String getFactionId() { return factionId; }

    private final PS location;
    public PS getPs() { return location; }
    public Location getLocation() { return location.asBukkitLocation(true); }

    private boolean paused;
    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public SandAlt(UUID npcId, String factionId, Location location)
    {
        this.npcId = npcId;
        this.factionId = factionId;
        this.location = PS.valueOf(location);
        this.paused = false;
    }

}
