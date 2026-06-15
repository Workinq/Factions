package com.massivecraft.factions.engine;

import com.massivecraft.factions.action.chunkalt.ActionChunkaltDespawn;
import com.massivecraft.factions.action.chunkalt.ActionChunkaltToggle;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.util.AltUtil;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;

import java.util.Set;

public class EngineChunk extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineChunk i = new EngineChunk();
    public static EngineChunk get() { return i; }

    // -------------------------------------------- //
    // ACTIVATE
    // -------------------------------------------- //

    @Override
    public void setActiveInner(boolean active)
    {
        if ( ! active) return;
        if ( ! this.hasPlugin()) return;

        // Re-apply the force-load tickets for active chunk alts once the server has settled.
        Bukkit.getScheduler().runTaskLater(this.getPlugin(), () ->
        {
            for (Faction faction : FactionColl.get().getAll())
            {
                for (ChunkAlt chunkAlt : faction.getChunkAlts())
                {
                    if (chunkAlt.isPaused()) continue;
                    AltUtil.setLoaded(chunkAlt, true);
                }
            }
        }, 20L);
    }

    // -------------------------------------------- //
    // LISTENERS
    // -------------------------------------------- //

    @EventHandler
    public void unclaim(EventFactionsChunksChange event)
    {
        for (PS chunk : event.getOldChunkFaction().keySet())
        {
            // Args
            Faction faction = event.getOldChunkFaction().get(chunk);
            Set<ChunkAlt> chunkAlts = faction.getChunkAltsInChunk(chunk);

            // Loop - Chunk Alts
            for (ChunkAlt chunkAlt : chunkAlts)
            {
                // Args
                Location location = chunkAlt.getLocation();

                // Apply
                faction.despawnChunkAlt(chunkAlt);

                // Inform
                faction.msg(Txt.parse("<g>A chunk alt at <i>x: <h>%,d <i>y: <h>%,d <i>z: <h>%,d <i>world: <h>%s <i>was despawned due to land change.", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()));
            }
        }
    }

    @EventHandler
    public void unload(ChunkUnloadEvent event)
    {
        // Args
        PS chunk = PS.valueOf(event.getChunk());

        // Apply - keep the chunk loaded if an active alt still needs it (ChunkUnloadEvent is no longer cancellable)
        if (AltUtil.isChunkNeeded(chunk.getWorld(), chunk.getChunkX(true), chunk.getChunkZ(true)))
        {
            event.getChunk().setForceLoaded(true);
        }
    }

    @EventHandler
    public void edit(NPCRightClickEvent event) { this.editNpc(event); }

    @EventHandler
    public void edit(NPCLeftClickEvent event) { this.editNpc(event); }

    private void editNpc(NPCClickEvent event)
    {
        // Args
        PS ps = PS.valueOf(event.getNPC().getStoredLocation());
        Faction faction = BoardColl.get().getFactionAt(ps);
        Player player = event.getClicker();
        MPlayer mplayer = MPlayer.get(player);

        // Verify - Chunk Alt
        ChunkAlt chunkAlt = faction.getChunkAltAt(ps);
        if (chunkAlt == null) return;

        // MPerm
        if ( ! MPerm.getPermChunkalt().has(mplayer, faction, true)) return;

        // Inventory
        player.openInventory(this.getEditGui(chunkAlt, faction, mplayer, false));
    }

    public Inventory getEditGui(ChunkAlt chunkAlt, Faction faction, MPlayer mplayer, boolean redirect)
    {
        Inventory inventory = Bukkit.createInventory(null, 27, Txt.parse("<gray>Edit Chunk Alt"));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);
        int span = MConf.get().chunkAltRadius * 2 + 1;

        // Stop/Start loading
        if (chunkAlt.isPaused())
        {
            chestGui.getInventory().setItem(12, new ItemBuilder(Material.LIME_DYE).name(Txt.parse("<g><bold>Start Loading")).withLore(Txt.parse(MUtil.list("<n>Click here to <g>start <n>keeping a", "<n>" + span + "x" + span + " chunk area loaded."))));
        }
        else
        {
            chestGui.getInventory().setItem(12, new ItemBuilder(Material.RED_DYE).name(Txt.parse("<b><bold>Stop Loading")).withLore(Txt.parse(MUtil.list("<n>Click here to <b>stop <n>keeping a", "<n>" + span + "x" + span + " chunk area loaded."))));
        }
        chestGui.setAction(12, new ActionChunkaltToggle(chunkAlt, ! chunkAlt.isPaused()));
        chestGui.getInventory().setItem(14, new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn")).withLore(Txt.parse(MUtil.list("<n>Click here to despawn this chunk alt"))));
        chestGui.setAction(14, new ActionChunkaltDespawn(faction, mplayer, chunkAlt, redirect));

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

}
