package com.massivecraft.factions.engine;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EngineExtras extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineExtras i = new EngineExtras();
    public static EngineExtras get() { return i; }

    // -------------------------------------------- //
    // CLAIM SQUARE
    // -------------------------------------------- //

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        String message = event.getMessage();
        if (StringUtils.startsWithIgnoreCase(message, "/f claim") || StringUtils.startsWithIgnoreCase(message, "/f unclaim"))
        {
            String[] args = message.split(" ");
            if (args.length < 3) return;
            try
            {
                int radius = Integer.parseInt(args[2]);
                if (radius > 1)
                {
                    event.setMessage(args[0] + " " + args[1] + " square " + radius);
                }
            } catch (NumberFormatException ignored)
            {
            }
        }
    }

    // -------------------------------------------- //
    // UNCLAIM ALL
    // -------------------------------------------- //

    @EventHandler
    public void onUnclaimAll(PlayerCommandPreprocessEvent event)
    {
        String message = event.getMessage();
        if (StringUtils.startsWithIgnoreCase(message, "/f unclaimall"))
        {
            String[] args = message.split(" ");

            if (args.length != 2) return;

            event.setMessage(args[0] + " unclaim all all " + event.getPlayer().getName());
        }
    }

    // -------------------------------------------- //
    // TNT FILL
    // -------------------------------------------- //

    @EventHandler
    public void onTntFill(PlayerCommandPreprocessEvent event)
    {
        String message = event.getMessage();
        if (StringUtils.startsWithIgnoreCase(message, "/f tntfill"))
        {
            String[] args = message.split(" ");

            if (args.length != 2) return;

            event.setMessage(args[0] + " tnt fill 576 20");
        }
    }

    // -------------------------------------------- //
    // TNT UNFILL
    // -------------------------------------------- //

    @EventHandler
    public void onTntUnfill(PlayerCommandPreprocessEvent event)
    {
        String message = event.getMessage();
        if (StringUtils.startsWithIgnoreCase(message, "/f tntunfill"))
        {
            String[] args = message.split(" ");

            if (args.length != 2) return;

            event.setMessage(args[0] + " tnt unfill 20");
        }
    }

    // -------------------------------------------- //
    // CHEST SEE
    // -------------------------------------------- //

    @EventHandler
    public void onChestSee(PlayerCommandPreprocessEvent event)
    {
        String message = event.getMessage();
        if (StringUtils.startsWithIgnoreCase(message, "/f chestsee") || StringUtils.startsWithIgnoreCase(message, "/f vaultsee"))
        {
            String[] args = message.split(" ");

            String arguments = args.length > 2 ? " " + String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "";
            event.setMessage(args[0] + " chest see" + arguments);
        }
    }

    // -------------------------------------------- //
    // ALT DAMAGE
    // -------------------------------------------- //

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if ( ! (event.getDamager() instanceof Player damager) ) return;
        if (MPlayer.get(damager).isAlt())
        {
            event.setCancelled(true);
        }
    }

    // -------------------------------------------- //
    // RESET FACTION DATA
    // -------------------------------------------- //

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMembershipChange(EventFactionsMembershipChange event)
    {
        event.getMPlayer().setChat(Chat.PUBLIC);

        MembershipChangeReason reason = event.getReason();
        if (reason == MembershipChangeReason.DISBAND || reason == MembershipChangeReason.KICK || reason == MembershipChangeReason.LEAVE)
        {
            event.getMPlayer().setAlt(false);
        }
    }

    // -------------------------------------------- //
    // SPAWNERS & BASE REGION
    // -------------------------------------------- //

    @EventHandler
    public void onPlaceSpawner(BlockPlaceEvent event)
    {
        if (event.getBlockPlaced().getType() != Material.SPAWNER) return;

        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if (mplayer.isOverriding()) return;

        Faction faction = mplayer.getFaction();
        if (faction.isSystemFaction()) return;

        PS chunk = PS.valueOf(event.getBlockPlaced().getChunk());
        if (BoardColl.get().getFactionAt(chunk).isSystemFaction()) return;

        if ( ! faction.hasBaseRegion() )
        {
            mplayer.msg("<b>You can't place spawners until you've set a base region using /f setbaseregion.");
            event.setCancelled(true);
            return;
        }

        if ( ! faction.getBaseRegion().contains(chunk) )
        {
            mplayer.msg("<b>You can only place spawners in your base region.");
            event.setCancelled(true);
            return;
        }

        if (BoardColl.get().getFactionAt(chunk) != faction)
        {
            mplayer.msg("<b>You can only place spawners in your own territory.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLandChange(EventFactionsChunksChange event)
    {
        Set<Faction> affected = new HashSet<>();
        affected.add(event.getNewFaction());
        affected.addAll(event.getOldFactionChunks().keySet());

        Bukkit.getScheduler().runTask(Factions.get(), () ->
        {
            for (Faction faction : affected)
            {
                if (faction == null || faction.isSystemFaction()) continue;
                if (faction.getCoreChunk() == null) continue;

                boolean unset = faction.recalculateBaseRegion();
                if (unset)
                {
                    faction.msg("<b>Your faction base region has been unset as the core chunk was unclaimed.");
                }
            }
        });
    }

}
