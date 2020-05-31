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
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

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
        // Scoreboard
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);

        // Health Tag
        Objective health = scoreboard.registerNewObjective("health", "health");
        health.setDisplayName(Txt.parse(MConf.get().healthBarFormat));
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);

        // Loop - Players
        for (Player target : Bukkit.getServer().getOnlinePlayers())
        {
            // Apply
            scoreboard.getObjective("health").getScore(target.getName()).setScore((int) target.getHealth());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        // Health Tag
        this.playerJoin(event.getPlayer());

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
        if (event.getMPlayer().getPlayer() == null)
        {
            // Do nothing
        }
        else
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
        if (player == null) return;
        if ( ! player.isOnline() ) return;

        // Args
        MPlayer mplayer = MPlayer.get(player);
        if (mplayer == null) return; // WHY THE FUCK DOES THIS HAPPEN??

        Faction mfaction = mplayer.getFaction();
        if (mfaction == null) return; // This should never happen.

        // Loop - Players
        for (Player target : Bukkit.getServer().getOnlinePlayers())
        {
            // Args
            Scoreboard scoreboard = target.getScoreboard();
            if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard() || target == player)
            {
                // Do nothing
            }
            else
            {
                // Args
                Faction faction = MPlayer.get(target).getFaction();

                // Teams
                Team enemy = this.getTeam(scoreboard, "fac-enemy", MConf.get().colorEnemy.toString());
                Team ally = this.getTeam(scoreboard, "fac-ally", MConf.get().colorAlly.toString());
                Team truce = this.getTeam(scoreboard, "fac-truce", MConf.get().colorTruce.toString());
                Team member = this.getTeam(scoreboard, "fac-member", MConf.get().colorMember.toString());
                Team neutral = this.getTeam(scoreboard, "fac-neutral", MConf.get().colorNeutral.toString());
                Team wilderness = this.getTeam(scoreboard, "fac-wild", MConf.get().colorWilderness.toString());
                Team focus = this.getTeam(scoreboard, "fac-focus", MConf.get().colorFocused.toString());

                // Relations
                if (faction.isPlayerFocused(player.getUniqueId().toString())) // Focus
                {
                    focus.addEntry(player.getName());
                }
                else
                {
                    // Set
                    this.setTeam(faction, enemy, ally, truce, member, neutral, wilderness, player, mfaction);
                }
            }
        }
    }

    public void resendTab(Player player)
    {
        // Verify
        if (player == null) return;
        if ( ! player.isOnline() ) return;

        // Args
        MPlayer mPlayer = MPlayer.get(player);
        Faction faction = mPlayer.getFaction();
        Scoreboard scoreboard = player.getScoreboard();

        // Verify - Scoreboard
        if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) return;

        // Teams
        Team enemy = this.getTeam(scoreboard, "fac-enemy", MConf.get().colorEnemy.toString());
        Team ally = this.getTeam(scoreboard, "fac-ally", MConf.get().colorAlly.toString());
        Team truce = this.getTeam(scoreboard, "fac-truce", MConf.get().colorTruce.toString());
        Team member = this.getTeam(scoreboard, "fac-member", MConf.get().colorMember.toString());
        Team neutral = this.getTeam(scoreboard, "fac-neutral", MConf.get().colorNeutral.toString());
        Team wilderness = this.getTeam(scoreboard, "fac-wild", MConf.get().colorWilderness.toString());
        Team focus = this.getTeam(scoreboard, "fac-focus", MConf.get().colorFocused.toString());

        // Loop - Players
        for (Player target : Bukkit.getOnlinePlayers())
        {
            // Focus - Takes priority
            if (faction.isPlayerFocused(target.getUniqueId().toString()))
            {
                focus.addEntry(target.getName());
                continue;
            }

            // Self - isn't this already in setTeam?
            /*if (player == target)
            {
                member.addEntry(target.getName());
                continue;
            }*/

            Faction targetFaction = MPlayer.get(target).getFaction();
            this.setTeam(faction, enemy, ally, truce, member, neutral, wilderness, target, targetFaction);
        }
    }

    private void setTeam(Faction faction, Team enemy, Team ally, Team truce, Team member, Team neutral, Team wilderness, Player target, Faction targetFaction)
    {
        // Wilderness
        if (targetFaction == null || targetFaction.isNone())
        {
            wilderness.addEntry(target.getName());
            return;
        }

        Rel relationTo = targetFaction.getRelationTo(faction);
        switch (relationTo)
        {
            case TRUCE:
                truce.addEntry(target.getName());
                return;
            case ALLY:
                ally.addEntry(target.getName());
                return;
            case ENEMY:
                enemy.addEntry(target.getName());
                return;
            case MEMBER:
                member.addEntry(target.getName());
                return;
            case NEUTRAL:
            default:
                neutral.addEntry(target.getName());
        }
    }

    private Team getTeam(Scoreboard scoreboard, String teamName, String string)
    {
        // Args
        Team team = scoreboard.getTeam(teamName);

        // Verify
        if (team == null)
        {
            // Create
            team = scoreboard.registerNewTeam(teamName);

            // Apply
            team.setPrefix(string);
        }

        // Return
        return team;
    }

}