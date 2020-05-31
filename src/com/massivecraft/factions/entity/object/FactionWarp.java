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
    public String getName() { return name; }

    private final UUID creator;
    public MPlayer getCreator() { return MPlayer.get(creator); }

    private final PS location;
    public PS getLocation() { return location; }

    private final String password;
    public String getPassword() { return password; }

    private final long creationMillis;
    public long getCreationMillis() { return creationMillis; }

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

    public boolean hasPassword()
    {
        if (password == null) return false;
        return ! password.equals("") ;
    }

}
