package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.store.EntityInternal;

public class FactionBan extends EntityInternal<FactionBan>
{
    // -------------------------------------------- //
    // OVERRIDE: ENTITY
    // -------------------------------------------- //

    @Override
    public FactionBan load(FactionBan that)
    {
        this.bannedId = that.bannedId;
        this.bannerId = that.bannerId;
        this.creationMillis = that.creationMillis;
        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private String bannedId;
    public String getBannedId() { return bannedId; }

    private String bannerId;
    public String getBannerId() { return bannerId; }

    private long creationMillis;
    public long getCreationMillis() { return creationMillis; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionBan()
    {
    }

    public FactionBan(String bannerId, String bannedId, long creationMillis)
    {
        this.bannerId = bannerId;
        this.bannedId = bannedId;
        this.creationMillis = creationMillis;
    }

}
