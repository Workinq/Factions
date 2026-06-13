package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.UUID;

public class FactionStrike extends EntityInternal<FactionStrike>
{
    // -------------------------------------------- //
    // OVERRIDE: ENTITY
    // -------------------------------------------- //

    @Override
    public FactionStrike load(FactionStrike that)
    {
        this.id = that.id;
        this.time = that.time;
        this.points = that.points;
        this.message = that.message;
        this.issuedBy = that.issuedBy;
        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private String id;
    public String getStrikeId() { return id; }

    private long time;
    public long getCreationMillis() { return time; }

    private int points;
    public int getPoints() { return points; }

    private String message;
    public String getMessage() { return message; }

    private String issuedBy;
    public String getIssuedBy() { return issuedBy; }
    public MPlayer getMPlayer() { return MPlayer.get(issuedBy); }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionStrike()
    {
    }

    public FactionStrike(long time, int points, String message, String issuedBy)
    {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.time = time;
        this.points = points;
        this.message = message;
        this.issuedBy = issuedBy;
    }

}
