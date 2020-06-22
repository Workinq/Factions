package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.store.EntityInternal;

public class FactionMute extends EntityInternal<FactionMute>
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String muteId;
    public String getMutedId() { return muteId; }

    private final String muterId;
    public String getMuterId() { return muterId; }

    private final long creationMillis;
    public long getCreationMillis() { return creationMillis; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionMute(String muterId, String mutedId, long creationMillis)
    {
        this.muteId = muterId;
        this.muterId = mutedId;
        this.creationMillis = creationMillis;
    }

}
