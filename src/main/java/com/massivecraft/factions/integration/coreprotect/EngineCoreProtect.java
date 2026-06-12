package com.massivecraft.factions.integration.coreprotect;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.pager.Msonifier;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class EngineCoreProtect extends Engine
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public final Set<Material> INTERACT_BLOCKS = EnumSet.of(Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.DISPENSER, Material.NOTE_BLOCK, Material.CHEST, Material.FURNACE, Material.OAK_DOOR, Material.LEVER, Material.STONE_BUTTON, Material.REPEATER, Material.OAK_TRAPDOOR, Material.OAK_FENCE_GATE, Material.BREWING_STAND, Material.OAK_BUTTON, Material.ANVIL, Material.TRAPPED_CHEST, Material.COMPARATOR, Material.HOPPER, Material.DROPPER);

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final EngineCoreProtect i = new EngineCoreProtect();
    public static EngineCoreProtect get() { return i; }

    // -------------------------------------------- //
    // LISTENER
    // -------------------------------------------- //

    @EventHandler
    public void onInspect(PlayerInteractEvent event)
    {
        // Args
        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();
        MPlayer mplayer = MPlayer.get(player);

        // Verify
        if (clickedBlock == null  || clickedBlock.getType() == Material.AIR) return;
        if (!mplayer.isInspecting()) return;

        event.setCancelled(true);

        if (! IntegrationCoreProtect.get().isActive())
        {
            mplayer.msg("<b>Inspecting faction land is currently disabled.");
            return;
        }

        Faction me = mplayer.getFaction();
        Faction at = BoardColl.get().getFactionAt(PS.valueOf(clickedBlock));
        if (me != at && ! Perm.INSPECT_ANY.has(player, true)) return;

        if ( ! MPerm.getPermInspect().has(mplayer, me, true)) return;

        Block toInspect = (event.getAction() == Action.LEFT_CLICK_BLOCK ? clickedBlock : clickedBlock.getRelative(event.getBlockFace()));
        inspectBlock(mplayer, toInspect);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFactionKick(EventFactionsMembershipChange event)
    {
        MPlayer mplayer = event.getMPlayer();
        if (mplayer.isInspecting()) mplayer.setInspecting(false);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if (mplayer.isInspecting()) mplayer.setInspecting(false);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if (mplayer.isInspecting()) mplayer.setInspecting(false);
    }

    private void inspectBlock(MPlayer mplayer, Block block)
    {
        if ( ! this.canInspect() )
        {
            mplayer.msg("<b>You cannot inspect blocks at the moment, please try again in a few seconds.");
            return;
        }

        // Look up the full history for this block via the official CoreProtect API.
        CoreProtectAPI api = this.getApi();
        List<String[]> lookup = api.blockLookup(block, 0);
        if (lookup == null || lookup.isEmpty())
        {
            mplayer.msg("<b>No data was found for that block.");
            return;
        }

        long nowSeconds = System.currentTimeMillis() / 1000L;
        List<Mson> inspectData = new ArrayList<>();
        List<String> rawData = new ArrayList<>();
        for (String[] value : lookup)
        {
            CoreProtectAPI.ParseResult result = api.parseResult(value);

            String player = result.getPlayer();
            String action = result.getActionString();
            Material type = result.getType();
            String material = type != null ? type.name().toLowerCase().replace('_', ' ') : "?";
            String time = formatAgo(Math.max(0L, nowSeconds - result.getTimestamp()));

            rawData.add(ChatColor.stripColor(Txt.parse("%s %s %s %s", player, action, material, time)));
            inspectData.add(Mson.mson(Txt.parse("<a>%s <i>%s <a>%s <n>%s", player, action, material, time)));
        }

        mplayer.setLastInspected(String.join("\n", rawData));

        // Pager
        Pager<Mson> pager = new Pager<>(CmdFactions.get().cmdFactionsLastInspected, "Inspect Log", 1, inspectData, (Msonifier<Mson>) (item, index) -> inspectData.get(index));
        pager.setSender(mplayer.getSender());

        // Send pager
        pager.message();
    }

    private boolean canInspect()
    {
        if (ConfigHandler.converterRunning) return false;
        if (ConfigHandler.purgeRunning) return false;
        return this.getApi() != null;
    }

    private CoreProtectAPI getApi()
    {
        if ( ! (Bukkit.getPluginManager().getPlugin("CoreProtect") instanceof CoreProtect coreProtect)) return null;
        CoreProtectAPI api = coreProtect.getAPI();
        return (api != null && api.isEnabled()) ? api : null;
    }

    private static String formatAgo(long seconds)
    {
        if (seconds < 60L) return seconds + "s ago";
        long minutes = seconds / 60L;
        if (minutes < 60L) return minutes + "m ago";
        long hours = minutes / 60L;
        if (hours < 24L) return hours + "h ago";
        return (hours / 24L) + "d ago";
    }

}
