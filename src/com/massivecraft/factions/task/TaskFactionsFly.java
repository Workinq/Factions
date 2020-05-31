package com.massivecraft.factions.task;

import com.massivecraft.factions.engine.EngineFly;
import com.massivecraft.factions.entity.*;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TaskFactionsFly extends ModuloRepeatTask
{

    private static TaskFactionsFly i = new TaskFactionsFly();
    public static TaskFactionsFly get() { return i; }

    @Override
    public long getDelayMillis()
    {
        // 1 Second
        return 1000L;
    }

    @Override
    public void invoke(long now)
    {
        if ( ! MOption.get().isFlight() ) return;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
            MPlayer mplayer = MPlayer.get(player);
            if (EngineFly.get().hasFlyBypass(player)) continue;

            Faction hostFaction = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));
            if (hostFaction == null) hostFaction = FactionColl.get().getNone();

            if (player.isFlying() || player.getAllowFlight())
            {
                if (mplayer.isOverriding()) continue;
                if (player.getLocation().getBlockY() > MConf.get().maxFlyHeight)
                {
                    EngineFly.get().disableFlight(player, "<b>Your faction flight has been disabled since you reached the maximum height.");
                }
                else if (hostFaction.isNone())
                {
                    if (player.hasPermission("factions.fly.any")) continue;
                    EngineFly.get().disableFlight(player, "<b>Your faction flight has been disabled since you can't fly here.");
                }
                else if ( ! MPerm.getPermFly().has(mplayer, hostFaction, false) )
                {
                    EngineFly.get().disableFlight(player, "<b>Your faction flight has been disabled since you can't fly here.");
                }
                else
                {
                    if ( ! EngineFly.get().isEnemyNear(mplayer, player, hostFaction) ) continue;
                    EngineFly.get().disableFlight(player, "<b>Your flight has been disabled since you are in enemy territory or near an enemy.");
                }
            }
            else
            {
                if (EngineFly.get().playersWithFlyDisabled.contains(player.getUniqueId().toString()) || player.getAllowFlight() || EngineFly.get().isEnemyNear(mplayer, player, hostFaction) || (!MPerm.getPermFly().has(mplayer, hostFaction, false) && (!hostFaction.isNone() || !player.hasPermission("factions.wildfly")))) continue;
                EngineFly.get().enableFlight(player, null);
            }
        }
    }

}
