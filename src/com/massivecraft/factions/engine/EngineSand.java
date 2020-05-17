package com.massivecraft.factions.engine;

import com.massivecraft.factions.action.ActionDespawnSandAlt;
import com.massivecraft.factions.action.ActionPrintSandAlt;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.factions.event.EventFactionsChunksChange;
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
                    // Apply
                    event.setCancelled(true);
                    event.getChunk().load();
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

        // Inventory
        player.openInventory(this.getEditGui(sandAlt, faction, mplayer));
    }

    public Inventory getEditGui(SandAlt sandAlt, Faction faction, MPlayer mplayer)
    {
        Inventory inventory = Bukkit.createInventory(null, 27, Txt.parse("<gray>Edit Sand Alt"));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        int radius = MConf.get().sandSpawnRadius;

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        // Stop/Start placing
        if (sandAlt.isPaused())
        {
            chestGui.getInventory().setItem(12, new ItemBuilder(Material.INK_SACK).name(Txt.parse("<g><bold>Start Placing")).durability(10).setLore(Txt.parse(MUtil.list("<n>Click here to <g>start <n>your alt from", "<n>printing sand in a " + radius + "x" + radius + "x" + radius + " radius."))));
        }
        else
        {
            chestGui.getInventory().setItem(12, new ItemBuilder(Material.INK_SACK).name(Txt.parse("<b><bold>Stop Placing")).durability(1).setLore(Txt.parse(MUtil.list("<n>Click here to <b>stop <n>your alt from", "<n>printing sand in a " + radius + "x" + radius + "x" + radius + " radius."))));
        }
        chestGui.setAction(12, new ActionPrintSandAlt(sandAlt, ! sandAlt.isPaused()));
        chestGui.getInventory().setItem(14, new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn")).setLore(Txt.parse(MUtil.list("<n>Click here to despawn this sand alt"))));
        chestGui.setAction(14, new ActionDespawnSandAlt(faction, mplayer, sandAlt));

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

}
