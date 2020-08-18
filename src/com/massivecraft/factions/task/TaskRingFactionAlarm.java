package com.massivecraft.factions.task;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.TimeUnit;
import org.bukkit.entity.Player;

public class TaskRingFactionAlarm extends ModuloRepeatTask
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static TaskRingFactionAlarm i = new TaskRingFactionAlarm();
    public static TaskRingFactionAlarm get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public long getDelayMillis()
    {
        // The interval is determined by the MConf rather than being set with setDelayMillis.
        return (long) (MConf.get().taskRingFactionAlarmMinutes * TimeUnit.MILLIS_PER_MINUTE);
    }

    @Override
    public void invoke(long now)
    {
        // Loop - Factions
        for (Faction faction : FactionColl.get().getAll())
        {
            // Verify
            if (faction.isSystemFaction()) continue;
            if (!faction.isAlarmEnabled()) continue;

            // Loop - MPlayers
            for (MPlayer mplayer : faction.getMPlayersWhereAlt(false))
            {
                // Verify
                if (!mplayer.hasAlertNotifications()) continue;

                // Args
                Player player = mplayer.getPlayer();

                // Inform
                mplayer.msg("<b><bold>ALERT: %s <i>has sounded the alarm, get to the walls!", faction.describeTo(mplayer, true));
                player.playSound(player.getLocation(), MConf.get().alarmSound, 1.0f, 1.0f);
            }
        }
    }

}
