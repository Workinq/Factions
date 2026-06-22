package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.store.EntityInternal;

public class FactionMute extends EntityInternal<FactionMute>
{
    // -------------------------------------------- //
    // OVERRIDE: ENTITY
    // -------------------------------------------- //

    @Override
    public FactionMute load(FactionMute that)
    {
        this.muteId = that.muteId;
        this.muterId = that.muterId;
        this.creationMillis = that.creationMillis;
        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private String muteId;
    public String getMutedId() { return muteId; }

    private String muterId;
    public String getMuterId() { return muterId; }

    private long creationMillis;
    public long getCreationMillis() { return creationMillis; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionMute()
    {
    }

    public FactionMute(String muterId, String mutedId, long creationMillis)
    {
        this.muteId = mutedId;
        this.muterId = muterId;
        this.creationMillis = creationMillis;
    }

}
