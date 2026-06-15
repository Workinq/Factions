package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

import java.util.UUID;

public abstract class Alt<T extends Alt<T>> extends EntityInternal<T>
{
    // -------------------------------------------- //
    // OVERRIDE: ENTITY
    // -------------------------------------------- //

    @Override
    @SuppressWarnings("unchecked")
    public T load(T that)
    {
        this.npcId = that.npcId;
        this.factionId = that.factionId;
        this.location = that.location;
        this.paused = that.paused;
        return (T) this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    protected UUID npcId;
    public UUID getNpcId() { return npcId; }

    protected String factionId;
    public String getFactionId() { return factionId; }

    protected PS location;
    public PS getPs() { return location; }
    public Location getLocation() { return location.asBukkitLocation(true); }

    protected boolean paused;
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

    public Alt()
    {
    }

    public Alt(UUID npcId, String factionId, Location location)
    {
        this.npcId = npcId;
        this.factionId = factionId;
        this.location = PS.valueOf(location);
        this.paused = false;
    }

}
