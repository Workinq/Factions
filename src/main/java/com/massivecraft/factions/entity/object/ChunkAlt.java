package com.massivecraft.factions.entity.object;

import org.bukkit.Location;

import java.util.UUID;

public class ChunkAlt extends Alt<ChunkAlt>
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ChunkAlt()
    {
    }

    public ChunkAlt(UUID npcId, String factionId, Location location)
    {
        super(npcId, factionId, location);
    }

}
