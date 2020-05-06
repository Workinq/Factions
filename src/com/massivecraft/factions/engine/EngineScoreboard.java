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
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EngineScoreboard extends Engine
{

    private static EngineScoreboard i = new EngineScoreboard();
    public static EngineScoreboard get() { return i; }

    public void load()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            this.playerJoin(player);
        }
    }

    private void playerJoin(Player player)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        Objective health = scoreboard.registerNewObjective("health", "health");
        health.setDisplayName(Txt.parse(MConf.get().healthBarFormat));
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        for (Player target : Bukkit.getServer().getOnlinePlayers())
        {
            scoreboard.getObjective("health").getScore(target.getName()).setScore((int) target.getHealth());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        this.playerJoin(event.getPlayer());
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                resendTab(event.getPlayer());
            }
        }.runTaskLater(Factions.get(), 20L);
        this.updateTab(event.getPlayer());
    }

    @EventHandler
    public void onFactionCreate(EventFactionsCreate event)
    {
        if (event.getMPlayer().getPlayer() == null) return;

        this.updateTab(event.getMPlayer().getPlayer());
        this.resendTab(event.getMPlayer().getPlayer());
    }

    @EventHandler
    public void onFactionDisband(EventFactionsDisband event)
    {
        if (event.getMPlayer().getPlayer() != null)
        {
            this.updateTab(event.getMPlayer().getPlayer());
        }
        for (Player player : event.getFaction().getOnlinePlayers())
        {
            this.updateTab(player);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        this.resendTab(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMembershipChange(EventFactionsMembershipChange event)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                resendTab(event.getMPlayer().getPlayer());
                for (Player player : event.getNewFaction().getOnlinePlayers())
                {
                    updateTab(player);
                }
            }
        }.runTaskLater(Factions.get(), 3L);
    }

    @EventHandler
    public void onRelationChange(EventFactionsRelationChange event)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player player : event.getFaction().getOnlinePlayers())
                {
                    updateTab(player);
                }
                for (Player player : event.getOtherFaction().getOnlinePlayers())
                {
                    updateTab(player);
                }
            }
        }.runTaskLater(Factions.get(), 3L);
    }

    public void updateTab(Player player)
    {
        if (player == null) return;
        if (!player.isOnline()) return;

        Faction mFaction = MPlayer.get(player).getFaction();
        for (Player target : Bukkit.getServer().getOnlinePlayers())
        {
            Scoreboard scoreboard = target.getScoreboard();
            if (scoreboard != Bukkit.getScoreboardManager().getMainScoreboard() && target != player)
            {
                Faction faction = MPlayer.get(target).getFaction();
                Team enemy = this.getTeam(scoreboard, "fac-enemy", MConf.get().colorEnemy.toString());
                Team ally = this.getTeam(scoreboard, "fac-ally", MConf.get().colorAlly.toString());
                Team truce = this.getTeam(scoreboard, "fac-truce", MConf.get().colorTruce.toString());
                Team member = this.getTeam(scoreboard, "fac-member", MConf.get().colorMember.toString());
                Team neutral = this.getTeam(scoreboard, "fac-neutral", MConf.get().colorNeutral.toString());
                Team wilderness = this.getTeam(scoreboard, "fac-wild", MConf.get().colorWilderness.toString());
                Team focus = this.getTeam(scoreboard, "fac-focus", MConf.get().colorFocused.toString());
                if (faction.isPlayerFocused(player.getUniqueId().toString()))
                {
                    focus.addEntry(player.getName());
                }
                else if (mFaction == null || mFaction.isNone())
                {
                    wilderness.addEntry(player.getName());
                }
                else
                {
                    Rel relation = mFaction.getRelationTo(faction);
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
        if (player == null) return;
        if (!player.isOnline()) return;

        MPlayer mPlayer = MPlayer.get(player);
        Faction faction = mPlayer.getFaction();
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard != Bukkit.getScoreboardManager().getMainScoreboard()) {
            Team enemy = this.getTeam(scoreboard, "fac-enemy", MConf.get().colorEnemy.toString());
            Team ally = this.getTeam(scoreboard, "fac-ally", MConf.get().colorAlly.toString());
            Team truce = this.getTeam(scoreboard, "fac-truce", MConf.get().colorTruce.toString());
            Team member = this.getTeam(scoreboard, "fac-member", MConf.get().colorMember.toString());
            Team neutral = this.getTeam(scoreboard, "fac-neutral", MConf.get().colorNeutral.toString());
            Team wilderness = this.getTeam(scoreboard, "fac-wild", MConf.get().colorWilderness.toString());
            Team focus = this.getTeam(scoreboard, "fac-focus", MConf.get().colorFocused.toString());
            for (Player target : Bukkit.getOnlinePlayers())
            {
                if (player == target)
                {
                    member.addEntry(target.getName());
                }
                else if (faction.isPlayerFocused(target.getUniqueId().toString()))
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

    private Team getTeam(Scoreboard scoreboard, String teamName, String string)
    {
        Team team = scoreboard.getTeam(teamName);
        if (team == null)
        {
            team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(string);
        }
        return team;
    }

}
