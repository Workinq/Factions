package com.massivecraft.factions.engine;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsRelationChange;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EngineScoreboard extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineScoreboard i = new EngineScoreboard();
    public static EngineScoreboard get() { return i; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        // Task
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                // Resend
                resendTab(event.getPlayer());
            }
        }.runTaskLater(Factions.get(), 20L);

        // Update
        this.updateTab(event.getPlayer());
    }

    @EventHandler
    public void onFactionCreate(EventFactionsCreate event)
    {
        // Verify
        if (event.getMPlayer().getPlayer() == null) return;

        // Update
        this.updateTab(event.getMPlayer().getPlayer());

        // Resend
        this.resendTab(event.getMPlayer().getPlayer());
    }

    @EventHandler
    public void onFactionDisband(EventFactionsDisband event)
    {
        if (event.getMPlayer().getPlayer() != null)
        {
            // Update
            this.updateTab(event.getMPlayer().getPlayer());
        }

        // Loop - Players
        for (Player player : event.getFaction().getOnlinePlayers())
        {
            // Update
            this.updateTab(player);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        // Resend
        this.resendTab(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMembershipChange(EventFactionsMembershipChange event)
    {
        // Task
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                // Resend
                resendTab(event.getMPlayer().getPlayer());

                // Loop - Players
                for (Player player : event.getNewFaction().getOnlinePlayers())
                {
                    // Update
                    updateTab(player);
                }
            }
        }.runTaskLater(Factions.get(), 3L);
    }

    @EventHandler
    public void onRelationChange(EventFactionsRelationChange event)
    {
        // Task
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                // Loop - Faction
                for (Player player : event.getFaction().getOnlinePlayers())
                {
                    updateTab(player);
                }

                // Loop - Other faction
                for (Player player : event.getOtherFaction().getOnlinePlayers())
                {
                    updateTab(player);
                }
            }
        }.runTaskLater(Factions.get(), 3L);
    }

    public void updateTab(Player player)
    {
        // Verify
        if (player == null || !player.isOnline()) return;

        // Args
        Faction faction = MPlayer.get(player).getFaction();

        // Loop
        for (Player target : Bukkit.getServer().getOnlinePlayers())
        {
            Scoreboard scoreboard = target.getScoreboard();

            // Team
            if (scoreboard != Bukkit.getScoreboardManager().getMainScoreboard() && target != player)
            {
                Faction targetFaction = MPlayer.get(target).getFaction();
                Team enemy = this.getTeam(scoreboard, "factions-enemy", MConf.get().colorEnemy);
                Team ally = this.getTeam(scoreboard, "factions-ally", MConf.get().colorAlly);
                Team truce = this.getTeam(scoreboard, "factions-truce", MConf.get().colorTruce);
                Team member = this.getTeam(scoreboard, "factions-member", MConf.get().colorMember);
                Team neutral = this.getTeam(scoreboard, "factions-neutral", MConf.get().colorNeutral);
                Team wilderness = this.getTeam(scoreboard, "factions-wilderness", MConf.get().colorWilderness);
                Team focus = this.getTeam(scoreboard, "factions-focused", MConf.get().colorFocused);

                if (targetFaction.isPlayerFocused(player.getUniqueId()))
                {
                    focus.addEntry(player.getName());
                }
                else if (faction == null || faction.isNone())
                {
                    wilderness.addEntry(player.getName());
                }
                else
                {
                    Rel relation = faction.getRelationTo(targetFaction);
                    switch (relation)
                    {
                        case TRUCE:
                            truce.addEntry(player.getName());
                            continue;
                        case ALLY:
                            ally.addEntry(player.getName());
                            continue;
                        case ENEMY:
                            enemy.addEntry(player.getName());
                            continue;
                        case NEUTRAL:
                            neutral.addEntry(player.getName());
                            continue;
                        case MEMBER:
                            member.addEntry(player.getName());
                    }
                }
            }
        }
    }

    public void resendTab(Player player)
    {
        // Verify
        if (player == null || !player.isOnline()) return;

        // Args
        MPlayer mplayer = MPlayer.get(player);
        Faction faction = mplayer.getFaction();
        Scoreboard scoreboard = player.getScoreboard();

        // Team
        if (scoreboard != Bukkit.getScoreboardManager().getMainScoreboard())
        {
            Team enemy = this.getTeam(scoreboard, "factions-enemy", MConf.get().colorEnemy);
            Team ally = this.getTeam(scoreboard, "factions-ally", MConf.get().colorAlly);
            Team truce = this.getTeam(scoreboard, "factions-truce", MConf.get().colorTruce);
            Team member = this.getTeam(scoreboard, "factions-member", MConf.get().colorMember);
            Team neutral = this.getTeam(scoreboard, "factions-neutral", MConf.get().colorNeutral);
            Team wilderness = this.getTeam(scoreboard, "factions-wilderness", MConf.get().colorWilderness);
            Team focus = this.getTeam(scoreboard, "factions-focused", MConf.get().colorFocused);

            for (Player target : Bukkit.getOnlinePlayers())
            {
                if (player == target)
                {
                    member.addEntry(target.getName());
                }
                else if (faction.isPlayerFocused(target.getUniqueId()))
                {
                    focus.addEntry(target.getName());
                }
                else
                {
                    Faction targetFaction = MPlayer.get(target).getFaction();
                    if (targetFaction == null || targetFaction.isNone())
                    {
                        wilderness.addEntry(target.getName());
                    }
                    else
                    {
                        Rel relationTo = targetFaction.getRelationTo(faction);
                        switch (relationTo)
                        {
                            case TRUCE:
                                truce.addEntry(target.getName());
                                continue;
                            case ALLY:
                                ally.addEntry(target.getName());
                                continue;
                            case ENEMY:
                                enemy.addEntry(target.getName());
                                continue;
                            case NEUTRAL:
                                neutral.addEntry(target.getName());
                                continue;
                            case MEMBER:
                                member.addEntry(target.getName());
                        }
                    }
                }
            }
        }
    }

    private Team getTeam(Scoreboard scoreboard, String teamName, ChatColor color)
    {
        // Args
        Team team = scoreboard.getTeam(teamName);

        // Verify
        if (team == null)
        {
            // Create
            team = scoreboard.registerNewTeam(teamName);

            // Apply
            team.setPrefix(color.toString());
        }

        // Return
        return team;
    }

}
