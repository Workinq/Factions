package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

import java.util.UUID;

public class SandAlt extends EntityInternal<SandAlt>
{
    // -------------------------------------------- //
    // OVERRIDE: ENTITY
    // -------------------------------------------- //

    @Override
    public SandAlt load(SandAlt that)
    {
        this.npcId = that.npcId;
        this.factionId = that.factionId;
        this.location = that.location;
        this.paused = that.paused;
        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private UUID npcId;
    public UUID getNpcId() { return npcId; }

    private String factionId;
    public String getFactionId() { return factionId; }

    private PS location;
    public PS getPs() { return location; }
    public Location getLocation() { return location.asBukkitLocation(true); }

    private boolean paused;
    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused)
    {
        if (this.paused == paused) return;
        this.paused = paused;
        this.changed();
    }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public SandAlt()
    {
    }

    public SandAlt(UUID npcId, String factionId, Location location)
    {
        this.npcId = npcId;
        this.factionId = factionId;
        this.location = PS.valueOf(location);
        this.paused = false;
    }

}
