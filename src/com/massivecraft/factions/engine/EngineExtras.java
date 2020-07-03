package com.massivecraft.factions.engine;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.ps.PS;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;
import java.util.Set;

import static com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;

public class EngineExtras extends Engine {

    private static EngineExtras i = new EngineExtras();
    public static EngineExtras get() { return i; }

    @EventHandler
    public void onCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
    {
        IEssentials essentials = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        String command = event.getMessage().toLowerCase();

        if (command.startsWith("/home") || command.startsWith("/homes") || command.startsWith("/ehome") || command.startsWith("/ehomes") || command.startsWith("/essentials:home") || command.startsWith("/essentials:ehome") || command.startsWith("/essentials:ehomes"))
        {
            // Verify - Args Length
            if (command.split(" ").length <= 1) return;

            IUser user = essentials.getUser(event.getPlayer());
            Location to = null;

            try
            {
                to = user.getHome(command.split(" ")[1]);
            }
            catch (Exception ignored)
            {
            }

            // Verify - Home
            if (to == null) return;

            // Args
            Player player = event.getPlayer();
            MPlayer mplayer = MPlayer.get(player);
            Faction faction = BoardColl.get().getFactionAt(PS.valueOf(to));

            // Verify - Wilderness or own faction
            if (faction.isNone() || faction == mplayer.getFaction()) return;

            // Cancel
            event.setCancelled(true);

            // Inform
            mplayer.msg("<b>You can't teleport there as it's in forbidden territory.");
        }
    }

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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if ( ! (event.getDamager() instanceof Player) ) return;

        Player damager = (Player) event.getDamager();
        if (damager == null) return;

        if (MPlayer.get(damager).isAlt()) event.setCancelled(true);
    }

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

    @EventHandler
    public void onPlaceSpawner(BlockPlaceEvent event)
    {
        if (event.getBlockPlaced().getType() != Material.MOB_SPAWNER) return;

        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if (mplayer.isOverriding()) return;

        Faction faction = mplayer.getFaction();
        if (faction.isSystemFaction()) return;

        PS chunk = PS.valueOf(event.getBlockPlaced().getChunk());
        if (BoardColl.get().getFactionAt(chunk).isSystemFaction()) return;

        if ( ! faction.hasBaseRegion() ) {
            mplayer.msg("<b>You can't place spawners until you've set a base region using /f setbaseregion.");
            event.setCancelled(true);
            return;
        }

        if ( ! (faction.hasVault())) {
            mplayer.msg("<b>You can't place spawners until you've set a vault using /f vault set.");
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

    /*@EventHandler
    public void onLandChange(EventFactionsChunksChange event)
    {
        Map<Faction, Set<PS>> oldFactionChunks = event.getOldFactionChunks();

        for (Faction faction : oldFactionChunks.keySet())
        {
            Set<PS> pss = oldFactionChunks.get(faction);

            if ( ! faction.hasBaseRegion() ) continue;
            if (pss.contains(faction.getCoreChunk()))
            {
                faction.setCoreChunk(null);
                faction.setBaseRegion(new MassiveSet<>());

                faction.msg("<b>Your faction base region has been unset as the core chunk was unclaimed.");
            }
        }
    }*/

    /*@EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event)
    {
        if (event.getEntity() instanceof TNTPrimed)
        {
            PS explosionLocation = PS.valueOf(event.getLocation());
            Faction factionBreached = BoardColl.get().getFactionAt(explosionLocation);
            PS sourceLocation = PS.valueOf(((TNTPrimed) event.getEntity()).getSourceLoc());
            Faction factionBreaching = BoardColl.get().getFactionAt(sourceLocation);
            if (factionBreaching.isSystemFaction())
            {
                return;
            }
            if (factionBreached == factionBreaching)
            {
                return;
            }
            if (factionBreached.getFactionAttackingId() == null)
            {
                factionBreached.setFactionAttacking(factionBreaching.getId(), System.currentTimeMillis());
            }
            else if (factionBreached.getFactionAttackingId().equals(factionBreaching.getId()))
            {
                factionBreached.setFactionAttacking(factionBreaching.getId(), System.currentTimeMillis());
            }
        }
    }*/

}
