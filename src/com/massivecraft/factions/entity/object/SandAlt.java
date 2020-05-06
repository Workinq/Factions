package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

import java.util.UUID;

public class SandAlt extends EntityInternal<SandAlt> {

    private final String uuid;
    private final String ownerUuid;
    private final PS loc;
    private boolean paused;
    private int amountRemaining;
    private final int maximumDurability;

    public SandAlt(String uuid, String ownerUuid, Location location, int amountRemaining, int maximumDurability)
    {
        this.uuid = uuid;
        this.ownerUuid = ownerUuid;
        this.loc = PS.valueOf(location);
        this.amountRemaining = amountRemaining;
        this.paused = false;
        this.maximumDurability = maximumDurability;
    }

    public PS getPs()
    {
        return loc;
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

    public UUID getUniqueId()
    {
        return UUID.fromString(uuid);
    }

    public UUID getOwnerUniqueId()
    {
        return UUID.fromString(ownerUuid);
    }

    public Location getLocation()
    {
        return loc.asBukkitLocation(true);
    }

    public Boolean isPaused()
    {
        return paused;
    }

    public Integer getAmountRemaining()
    {
        return amountRemaining;
    }

    public void setAmountRemaining(int amountRemaining)
    {
        // Detect Nochange
        if (this.amountRemaining == amountRemaining) return;

        // Apply
        this.amountRemaining = amountRemaining;

        // Mark as changed.
        this.changed();
    }

    public int getMaximumDurability()
    {
        return maximumDurability;
    }

}
