package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.UUID;

public class FactionWarp extends EntityInternal<FactionWarp>
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String name;
    private final UUID creator;
    private final PS location;
    private final String password;
    private final long creationMillis;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionWarp(String name, UUID creator, PS location, String password)
    {
        this.name = name;
        this.creator = creator;
        this.location = location;
        this.password = password;
        this.creationMillis = System.currentTimeMillis();
    }

    public String getName()
    {
        return name;
    }

    public MPlayer getCreator()
    {
        return MPlayer.get(creator);
    }

    public PS getLocation()
    {
        return location;
    }

    public boolean hasPassword()
    {
        if (password == null) return false;
        return !password.equals("");
    }

    public String getPassword()
    {
        return password;
    }

    public long getCreationMillis()
    {
        return creationMillis;
    }

}
