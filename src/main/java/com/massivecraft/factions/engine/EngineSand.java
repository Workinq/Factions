package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.gui.SandAltEditMenu;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Set;

public class EngineSand extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineSand i = new EngineSand();
    public static EngineSand get() { return i; }

    @EventHandler
    public void unclaim(EventFactionsChunksChange event)
    {
        for (PS chunk : event.getOldChunkFaction().keySet())
        {
            // Args
            Faction faction = event.getOldChunkFaction().get(chunk);
            Set<SandAlt> sandAlts = faction.getSandAltsInChunk(chunk);

            // Loop - Sand Alts
            for (SandAlt sandAlt : sandAlts)
            {
                // Args
                Location location = sandAlt.getLocation();

                // Apply
                faction.despawnSandAlt(sandAlt);

                // Inform
                faction.msg(Txt.parse("<g>A sand alt at <i>x: <h>%,d <i>y: <h>%,d <i>z: <h>%,d <i>world: <h>%s <i>was despawned due to land change.", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()));
            }
        }
    }

    @EventHandler
    public void unload(ChunkUnloadEvent event)
    {
        // Args
        PS chunk = PS.valueOf(event.getChunk());

        // Loop - Faction (PREDICATE: Faction has sand alts)
        for (Faction faction : FactionColl.get().getAll(faction -> ! faction.getSandAlts().isEmpty()))
        {
            // Loop - Sand Alts
            for (SandAlt sandAlt : faction.getSandAlts())
            {
                // Args
                PS location = sandAlt.getPs().getChunk(true);

                // Verify
                if (chunk.equals(location))
                {
                    // Apply - keep the chunk loaded so sand alts keep working (ChunkUnloadEvent is no longer cancellable)
                    event.getChunk().setForceLoaded(true);
                }
            }
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

        // Verify - Sand Alt
        SandAlt sandAlt = faction.getSandAltAt(ps);
        if (sandAlt == null) return;

        // MPerm
        if ( ! MPerm.getPermSandalt().has(mplayer, faction, true)) return;

        // Open
        new SandAltEditMenu(player, mplayer, faction, sandAlt).open();
    }

}
