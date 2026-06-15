package com.massivecraft.factions.action.chunkalt;

import com.massivecraft.factions.engine.EngineChunk;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionChunkaltEdit extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final ChunkAlt chunkAlt;
    private final Faction faction;
    private final MPlayer mplayer;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionChunkaltEdit(ChunkAlt chunkAlt, Faction faction, MPlayer mplayer)
    {
        this.chunkAlt = chunkAlt;
        this.faction = faction;
        this.mplayer = mplayer;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        player.openInventory(EngineChunk.get().getEditGui(chunkAlt, faction, mplayer, true));
        return true;
    }

}
