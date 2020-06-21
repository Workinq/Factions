package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.UUID;

public class FactionStrike extends EntityInternal<FactionStrike>
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String id;
    public String getStrikeId() { return id; }

    private final long time;
    public long getCreationMillis() { return time; }

    private final int points;
    public int getPoints() { return points; }

    private final String message;
    public String getMessage() { return message; }

    private final String issuedBy;
    public String getIssuedBy() { return issuedBy; }
    public MPlayer getMPlayer() { return MPlayer.get(issuedBy); }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionStrike(long time, int points, String message, String issuedBy)
    {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.time = time;
        this.points = points;
        this.message = message;
        this.issuedBy = issuedBy;
    }

}
