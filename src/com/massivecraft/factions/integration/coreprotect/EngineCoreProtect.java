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
import net.coreprotect.database.Database;
import net.coreprotect.database.Lookup;
import net.coreprotect.model.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class EngineCoreProtect extends Engine
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public final Set<Material> INTERACT_BLOCKS = EnumSet.of(Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.DISPENSER, Material.NOTE_BLOCK, Material.CHEST, Material.FURNACE, Material.BURNING_FURNACE, Material.WOODEN_DOOR, Material.LEVER, Material.STONE_BUTTON, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.TRAP_DOOR, Material.FENCE_GATE, Material.BREWING_STAND, Material.WOOD_BUTTON, Material.ANVIL, Material.TRAPPED_CHEST, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.HOPPER, Material.DROPPER);

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

        Connection connection = Database.getConnection(false);
        if (connection == null)
        {
            mplayer.msg("<b>You cannot inspect blocks at the moment, please try again in a few seconds.");
            return;
        }

        try (Statement statement = connection.createStatement())
        {
            boolean chest;
            String data;
            if (INTERACT_BLOCKS.contains(block.getType()))
            {
                data = Lookup.chest_transactions(statement, block.getLocation(), mplayer.getName(), 1, MConf.get().inspectResultLimit);
                chest = true;
            }
            else
            {
                data = Lookup.block_lookup(statement, block, mplayer.getPlayer().getName(), 0, 1, MConf.get().inspectResultLimit);
                chest = false;
            }
            if (!data.contains("\n"))
            {
                mplayer.msg("<b>No data was found for that block.");
                return;
            }
            mplayer.setLastInspected(data);
            List<Mson> inspectData = new ArrayList<>();
            String[] blockData = data.split("\n");
            for (String blockDatum : blockData)
            {
                String info = ChatColor.stripColor(blockDatum);

                if (info.contains("older data by typing") || info.contains("-----") || info.contains("CoreProtect")) continue;

                // Format: TIME - ACTION (e.g. 0.01/h ago - Kieraaaan placed cobblestone.)
                String[] dataSplit = info.split("-");
                String time = dataSplit[0];
                String actionString = dataSplit[1].replace(".", "");
                String[] actionSplit = actionString.split(" ");
                String player = ChatColor.stripColor(actionSplit[1]);
                String action = ChatColor.stripColor(actionSplit[2]);
                String material;

                if (chest)
                {
                    material = ChatColor.stripColor(actionSplit[3] + " " + actionSplit[4]);
                }
                else
                {
                    material = ChatColor.stripColor(actionSplit[3]);
                }

                info = Txt.parse("<a>%s <i>%s <a>%s <n>%s", player, action, material, time);
                inspectData.add(Mson.mson(info));
            }
            final Pager<Mson> pager = new Pager<>(CmdFactions.get().cmdFactionsLastInspected, "Inspect Log", 1, inspectData, new Msonifier<Mson>()
            {
                @Override
                public Mson toMson(Mson item, int index)
                {
                    return inspectData.get(index);
                }
            });
            // final Pager<Mson> pager = new Pager<>(CmdFactions.get().cmdFactionsLastInspected, "Inspect Log", 1, inspectData, (Msonifier<Mson>) (item, index) -> inspectData.get(index));
            pager.setSender(mplayer.getSender());

            // Send pager
            pager.message();
        }
        catch (SQLException e)
        {
            mplayer.msg("<b>You cannot inspect blocks at the moment, please try again in a few seconds.");
        }
    }

    private boolean canInspect()
    {
        if (Config.converter_running) return false;
        if (Config.purge_running) return false;
        return Database.getConnection(false) != null;
    }

}
