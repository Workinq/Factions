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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EngineScoreboard extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineScoreboard i = new EngineScoreboard();
    public static EngineScoreboard get() { return i; }

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    private static final String TEAM_ENEMY = "factions-enemy";
    private static final String TEAM_ALLY = "factions-ally";
    private static final String TEAM_TRUCE = "factions-truce";
    private static final String TEAM_MEMBER = "factions-member";
    private static final String TEAM_NEUTRAL = "factions-neutral";
    private static final String TEAM_WILDERNESS = "factions-wilderness";
    private static final String TEAM_FOCUSED = "factions-focused";

    private static final long DELAY_JOIN = 20L;
    private static final long DELAY_CHANGE = 3L;

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Map<UUID, Scoreboard> managedBoards = new HashMap<>();

    // -------------------------------------------- //
    // ACTIVATE
    // -------------------------------------------- //

    @Override
    public void setActiveInner(boolean active)
    {
        if ( ! active)
        {
            this.releaseBoards();
            return;
        }
        if ( ! this.hasPlugin()) return;

        // Adopt players already online (eg on a reload), who never fire a join.
        Bukkit.getScheduler().runTaskLater(Factions.get(), () ->
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                this.ensureManaged(player);
                this.resendTab(player);
            }
        }, DELAY_JOIN);
    }

    // -------------------------------------------- //
    // LISTENERS
    // -------------------------------------------- //

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(Factions.get(), () ->
        {
            this.ensureManaged(player);
            this.resendTab(player);
            this.updateTab(player);
        }, DELAY_JOIN);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        this.managedBoards.remove(player.getUniqueId());

        String name = player.getName();
        for (Map.Entry<UUID, Scoreboard> entry : this.managedBoards.entrySet())
        {
            Player owner = Bukkit.getPlayer(entry.getKey());
            if (owner == null || owner.getScoreboard() != entry.getValue()) continue;

            Team team = entry.getValue().getEntryTeam(name);
            if (team != null) team.removeEntry(name);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        this.resendTab(event.getPlayer());
    }

    @EventHandler
    public void onFactionCreate(EventFactionsCreate event)
    {
        Player player = event.getMPlayer().getPlayer();
        if (player == null) return;

        Bukkit.getScheduler().runTaskLater(Factions.get(), () ->
        {
            this.ensureManaged(player);
            this.updateTab(player);
            this.resendTab(player);
        }, DELAY_CHANGE);
    }

    @EventHandler
    public void onFactionDisband(EventFactionsDisband event)
    {
        List<Player> members = event.getFaction().getOnlinePlayers();
        Bukkit.getScheduler().runTaskLater(Factions.get(), () ->
        {
            for (Player member : members) this.updateTab(member);
        }, DELAY_CHANGE);
    }

    @EventHandler
    public void onPlayerMembershipChange(EventFactionsMembershipChange event)
    {
        Player player = event.getMPlayer().getPlayer();
        Faction newFaction = event.getNewFaction();
        Bukkit.getScheduler().runTaskLater(Factions.get(), () ->
        {
            this.resendTab(player);
            for (Player member : newFaction.getOnlinePlayers()) this.updateTab(member);
        }, DELAY_CHANGE);
    }

    @EventHandler
    public void onRelationChange(EventFactionsRelationChange event)
    {
        Faction faction = event.getFaction();
        Faction other = event.getOtherFaction();
        Bukkit.getScheduler().runTaskLater(Factions.get(), () ->
        {
            for (Player player : faction.getOnlinePlayers()) this.updateTab(player);
            for (Player player : other.getOnlinePlayers()) this.updateTab(player);
        }, DELAY_CHANGE);
    }

    // -------------------------------------------- //
    // OWNERSHIP
    // -------------------------------------------- //

    private void ensureManaged(Player player)
    {
        if ( ! MConf.get().scoreboardEnabled) return;
        if (player == null || ! player.isOnline()) return;
        if (this.ownsBoard(player)) return;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        // Only take over a pristine main scoreboard, so we never wipe another plugin's display.
        Scoreboard main = manager.getMainScoreboard();
        if (player.getScoreboard() != main) return;
        if ( ! main.getObjectives().isEmpty() || ! main.getTeams().isEmpty()) return;

        Scoreboard board = manager.getNewScoreboard();
        player.setScoreboard(board);
        this.managedBoards.put(player.getUniqueId(), board);
    }

    private boolean ownsBoard(Player player)
    {
        Scoreboard ours = this.managedBoards.get(player.getUniqueId());
        if (ours == null) return false;
        if (ours != player.getScoreboard())
        {
            this.managedBoards.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    private void releaseBoards()
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard main = manager == null ? null : manager.getMainScoreboard();
        for (Map.Entry<UUID, Scoreboard> entry : this.managedBoards.entrySet())
        {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (main != null && player != null && player.getScoreboard() == entry.getValue())
            {
                player.setScoreboard(main);
            }
        }
        this.managedBoards.clear();
    }

    // -------------------------------------------- //
    // COLORING
    // -------------------------------------------- //

    public void updateTab(Player player)
    {
        if ( ! MConf.get().scoreboardEnabled) return;
        if (player == null || ! player.isOnline()) return;

        Faction faction = MPlayer.get(player).getFaction();
        UUID id = player.getUniqueId();
        String name = player.getName();

        for (Player target : Bukkit.getOnlinePlayers())
        {
            if (target == player || ! this.ownsBoard(target)) continue;

            Faction viewerFaction = MPlayer.get(target).getFaction();
            this.teamsFor(target.getScoreboard()).teamFor(viewerFaction, faction, id).addEntry(name);
        }
    }

    public void resendTab(Player player)
    {
        if ( ! MConf.get().scoreboardEnabled) return;
        if (player == null || ! player.isOnline()) return;
        if ( ! this.ownsBoard(player)) return;

        Faction viewerFaction = MPlayer.get(player).getFaction();
        RelationTeams teams = this.teamsFor(player.getScoreboard());

        for (Player target : Bukkit.getOnlinePlayers())
        {
            Team team;
            if (target == player)
            {
                team = teams.member();
            }
            else
            {
                Faction entityFaction = MPlayer.get(target).getFaction();
                team = teams.teamFor(viewerFaction, entityFaction, target.getUniqueId());
            }
            team.addEntry(target.getName());
        }
    }

    // -------------------------------------------- //
    // TEAMS
    // -------------------------------------------- //

    private RelationTeams teamsFor(Scoreboard board)
    {
        MConf conf = MConf.get();
        Map<Rel, Team> byRel = new EnumMap<>(Rel.class);
        byRel.put(Rel.ENEMY, this.getTeam(board, TEAM_ENEMY, Rel.ENEMY.getColor()));
        byRel.put(Rel.ALLY, this.getTeam(board, TEAM_ALLY, Rel.ALLY.getColor()));
        byRel.put(Rel.TRUCE, this.getTeam(board, TEAM_TRUCE, Rel.TRUCE.getColor()));
        byRel.put(Rel.MEMBER, this.getTeam(board, TEAM_MEMBER, Rel.MEMBER.getColor()));
        byRel.put(Rel.NEUTRAL, this.getTeam(board, TEAM_NEUTRAL, Rel.NEUTRAL.getColor()));
        Team wilderness = this.getTeam(board, TEAM_WILDERNESS, conf.colorWilderness);
        Team focus = this.getTeam(board, TEAM_FOCUSED, conf.colorFocused);
        return new RelationTeams(byRel, wilderness, focus);
    }

    private Team getTeam(Scoreboard board, String name, ChatColor color)
    {
        Team team = board.getTeam(name);
        if (team == null) team = board.registerNewTeam(name);
        String prefix = color.toString();
        if ( ! prefix.equals(team.getPrefix())) team.setPrefix(prefix);
        return team;
    }

    private static final class RelationTeams
    {
        private final Map<Rel, Team> byRel;
        private final Team wilderness;
        private final Team focus;

        private RelationTeams(Map<Rel, Team> byRel, Team wilderness, Team focus)
        {
            this.byRel = byRel;
            this.wilderness = wilderness;
            this.focus = focus;
        }

        private Team member()
        {
            return this.byRel.get(Rel.MEMBER);
        }

        private Team teamFor(Faction viewerFaction, Faction entityFaction, UUID entityId)
        {
            if (viewerFaction.isPlayerFocused(entityId)) return this.focus;
            if (entityFaction.isNone()) return this.wilderness;
            return this.byRel.getOrDefault(entityFaction.getRelationTo(viewerFaction), this.byRel.get(Rel.NEUTRAL));
        }
    }

}
