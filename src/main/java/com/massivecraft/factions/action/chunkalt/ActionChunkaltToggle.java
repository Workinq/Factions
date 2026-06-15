package com.massivecraft.factions.action.chunkalt;

import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.factions.util.AltUtil;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionChunkaltToggle extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final ChunkAlt chunkAlt;
    private final boolean paused;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionChunkaltToggle(ChunkAlt chunkAlt, boolean paused)
    {
        this.chunkAlt = chunkAlt;
        this.paused = paused;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Apply
        chunkAlt.setPaused(paused);
        chunkAlt.changed();

        // Force-load or release the covered chunks to match the new state
        AltUtil.setLoaded(chunkAlt, ! paused);

        return true;
    }

}
