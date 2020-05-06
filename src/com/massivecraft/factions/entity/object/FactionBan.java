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
        this.bannerId = that.bannerId;
        this.creationMillis = that.creationMillis;

        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private String bannerId;
    public String getBannerId() { return bannerId; }
    public void setBannerId(String bannerId) { this.bannerId = bannerId; }

    private Long creationMillis;
    public Long getCreationMillis() { return creationMillis; }
    public void setCreationMillis(Long creationMillis) { this.creationMillis = creationMillis; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionBan()
    {
        this(null, null);
    }

    public FactionBan(String inviterId, Long creationMillis)
    {
        this.bannerId = inviterId;
        this.creationMillis = creationMillis;
    }

}
