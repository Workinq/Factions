package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.UUID;

public class FactionStrike extends EntityInternal<FactionWarp>
{

    private final String id;
    private final long time;
    private final int points;
    private final String message;
    private final String issuedBy;

    public FactionStrike(long time, int points, String message, String issuedBy)
    {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.time = time;
        this.points = points;
        this.message = message;
        this.issuedBy = issuedBy;
    }

    public String getStrikeId()
    {
        return id;
    }

    public long getCreationMillis()
    {
        return time;
    }

    public int getPoints()
    {
        return points;
    }

    public String getMessage()
    {
        return message;
    }

    public MPlayer getIssuer()
    {
        return MPlayer.get(issuedBy);
    }

    public String getIssuedBy()
    {
        return issuedBy;
    }

}
