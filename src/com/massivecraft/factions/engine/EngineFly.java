package com.massivecraft.factions.engine;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.*;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class EngineFly extends Engine
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public MassiveSet<UUID> playersWithFlyDisabled = new MassiveSet<>();

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineFly i = new EngineFly();
    public static EngineFly get() { return i; }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerToggleFly(PlayerToggleFlightEvent event)
    {
        if ( ! MOption.get().isFlight() ) return;
        Player player = event.getPlayer();
        if (playersWithFlyDisabled.contains(player.getUniqueId())) return;
        if (! event.isFlying() || MUtil.isntPlayer(player) || this.hasFlyBypass(player)) return;

        MPlayer mplayer = MPlayer.get(player);
        Faction hostFaction = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));

        if  // MPerm check
        (
                this.isEnemyNear(mplayer, player, hostFaction) // Enemy Check
                || (hostFaction.isNone() && ! (player.hasPermission("factions.wildfly") || player.hasPermission("factions.fly.any"))) // Wild fly check
                || ( ! hostFaction.isNone() && ! MPerm.getPermFly().has(mplayer, hostFaction, false))
        )
        {
            event.setCancelled(true);
            player.setAllowFlight(false);
            return;
        }

        mplayer.setWasFlying(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if ( ! MOption.get().isFlight() ) return;

        Player player = event.getPlayer();
        if (MUtil.isntPlayer(player) || this.hasFlyBypass(player)) return;

        MPlayer mplayer = MPlayer.get(player);
        Faction hostFaction = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));
        if (hostFaction == null) hostFaction = FactionColl.get().getNone();

        boolean hasPerm = MPerm.getPermFly().has(mplayer, hostFaction, false);
        if (hostFaction.isNone())
        {
            if ( ! (player.hasPermission("factions.fly.any") || player.hasPermission("factions.wildfly")) )
            {
                player.setAllowFlight(false);
                this.disableFlight(player, "<b>Your faction flight has been disabled since you can't fly here.");
            }
            return;
        }
        if ( ! hasPerm )
        {
            this.disableFlight(player, "<b>Your faction flight has been disabled since you can't fly here.");
        }
    }

    public void chunkChangeFlight(MPlayer mplayer, Player player, PS chunkTo)
    {
        if (MUtil.isntPlayer(player) || this.hasFlyBypass(player)) return;

        Faction factionTo = BoardColl.get().getFactionAt(chunkTo);
        if (factionTo == null) factionTo = FactionColl.get().getNone();

        if (factionTo.isNone())
        {
            if ( ! (player.hasPermission("factions.wildfly") || player.hasPermission("factions.fly.any")) )
            {
                this.disableFlight(player, "<b>Your faction flight has been disabled since you can't fly here.");
            }
        }
        else if ( ! MPerm.getPermFly().has(mplayer, factionTo, false) )
        {
            this.disableFlight(player, "<b>Your faction flight has been disabled since you can't fly here.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPearl(PlayerInteractEvent event)
    {
        if (!MOption.get().isFlight() || !MConf.get().usePearlsFlying || event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        if (MUtil.isntPlayer(player)) return;

        if (event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL && player.isFlying())
        {
            event.setCancelled(true);
            MixinMessage.get().msgOne(player, "<b>You can't use ender pearls while flying.");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void negateNextFall(EntityDamageEvent event)
    {
        if (!MOption.get().isFlight() || !(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (MUtil.isntPlayer(player) || event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        MPlayer mplayer = MPlayer.get(player);
        if (mplayer.wasFlying())
        {
            mplayer.setWasFlying(false);
            event.setCancelled(true);
        }
    }

    public void disableFlight(Player player, String message)
    {
        if (MUtil.isntPlayer(player) || this.hasFlyBypass(player)) return;
        MPlayer mplayer = MPlayer.get(player);

        if (player.isFlying())
        {
            player.setFlying(false);
            player.setVelocity(new Vector(0, 0, 0));
            player.setFallDistance(0.0f);
            mplayer.setWasFlying(true);
            if (message != null)
            {
                MixinMessage.get().msgOne(player, message);
            }
        }
        player.setAllowFlight(false);
    }

    public void enableFlight(Player player, String message)
    {
        player.setAllowFlight(true);
        if (message != null)
        {
            MixinMessage.get().msgOne(player, message);
        }
    }

    public boolean hasFlyBypass(Player player)
    {
        return player.getGameMode() != GameMode.SURVIVAL || player.hasPermission("factions.fly.override");
    }

    public boolean isEnemyNear(MPlayer mplayer, Player player, Faction hostFaction)
    {
        for (Player target : Bukkit.getServer().getOnlinePlayers())
        {
            if (target.getWorld() != player.getWorld()) continue;
            Location playerLocation = player.getLocation();
            Location pLocation = target.getLocation();
            double distance = Math.pow(playerLocation.getX() - pLocation.getX(), 2.0) + Math.pow(playerLocation.getZ() - pLocation.getZ(), 2.0);

            if (distance > MConf.get().flyXZCheck * MConf.get().flyXZCheck) continue;
            if (Math.abs(playerLocation.getY() - pLocation.getY()) > MConf.get().flyYCheck) continue;
            if (target == player || this.hasFlyBypass(target) || !player.canSee(target)) continue;
            if (target.isDead()) continue;

            MPlayer mtarget = MPlayer.get(target);
            if (mtarget == null || mtarget.getFaction().getId().equals(mplayer.getFaction().getId())) continue;
            if (mtarget.isStealth()) continue;
            if (mtarget.getRelationTo(hostFaction) == Rel.ENEMY || mtarget.getRelationTo(mplayer) == Rel.ENEMY)
            {
                return true;
            }
        }
        return false;
    }

}
