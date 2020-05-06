package com.massivecraft.factions.task;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TaskFactionsFly extends ModuloRepeatTask
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static TaskFactionsFly i = new TaskFactionsFly();
    public static TaskFactionsFly get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public long getDelayMillis()
    {
        // 1 second
        return 1000L;
    }

    @Override
    public void invoke(long now)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player == null || ! player.isOnline() || player.isDead()) continue;

            // Args
            PS standingAt = PS.valueOf(player);
            Faction at = BoardColl.get().getFactionAt(standingAt);
            MPlayer mPlayer = MPlayer.get(player);
            Faction faction = mPlayer.getFaction();

            // Auto Enable
            if ( ! mPlayer.isFlying())
            {
                if ( ! mPlayer.isAutoFlying()) continue; // Player has autofly disabled.
                if ( isEnemyNear(player, at)) continue; // An enemy is near the player.
                if (at.isNone() && Perm.FLY_ANY.has(player)) // Player has wild fly.
                {
                    mPlayer.setFlying(true);
                    continue;
                }
                if (faction.getId().equals(Factions.ID_NONE)) continue; // The player is in wilderness.
                if (at != faction) continue; // The factions aren't the same.

                mPlayer.setFlying(true);
                continue;
            }

            // Enemy Check
            if (isEnemyNear(player, at))
            {
                disableFlight(player, "<b>Your faction flight was disabled since enemies are nearby.");
                continue;
            }

            // Wild Fly
            if (at.isNone() && Perm.FLY_ANY.has(player)) continue;

            // Wilderness Check
            if (faction.isNone())
            {
                disableFlight(player, "<b>Your flight has been disabled since you don't have a faction.");
                continue;
            }

            // Land Check
            if (at != faction)
            {
                disableFlight(player, "<b>Your faction flight was disabled since you can't fly here.");
            }
        }
    }

    public boolean hasFlyBypass(Player player)
    {
        return player.getGameMode() != GameMode.SURVIVAL || Perm.FLY_ANY.has(player);
    }

    public void disableFlight(Player player, String message)
    {
        MPlayer mPlayer = MPlayer.get(player);

        if (MUtil.isntPlayer(player) || this.hasFlyBypass(player) || mPlayer.isOverriding()) return;

        player.setVelocity(new Vector(0, 0, 0));
        player.setFallDistance(0.0f);

        // Inform
        if (message != null)
        {
            MixinMessage.get().msgOne(mPlayer, message);
        }

        mPlayer.setFlying(false);
    }

    public boolean isEnemyNear(Player player, Faction faction)
    {
        for (Entity entity : player.getNearbyEntities(MConf.get().enemyCheckRadius, 256, MConf.get().enemyCheckRadius))
        {
            if (!(entity instanceof Player)) continue;

            Player target = (Player) entity;
            if (player == target || this.hasFlyBypass(target) || !player.canSee(target) || target.isDead()) continue;

            MPlayer mPlayer = MPlayer.get(target);
            if (mPlayer == null || mPlayer.getFaction() == faction || mPlayer.isStealth()) continue;

            if (mPlayer.getFaction().getRelationTo(faction) == Rel.ENEMY) return true;
        }
        return false;
    }

}
