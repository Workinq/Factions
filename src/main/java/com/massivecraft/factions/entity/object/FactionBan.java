package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.store.EntityInternal;

public class FactionBan extends EntityInternal<FactionBan>
{

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String bannedId;
    public String getBannedId() { return bannedId; }

    private final String bannerId;
    public String getBannerId() { return bannerId; }

    private final long creationMillis;
    public long getCreationMillis() { return creationMillis; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionBan(String inviterId, String bannedId, long creationMillis)
    {
        this.bannerId = inviterId;
        this.bannedId = bannedId;
        this.creationMillis = creationMillis;
    }

}
