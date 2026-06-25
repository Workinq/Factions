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
        return (long) (MConf.get().taskRingFactionAlarmSeconds * TimeUnit.MILLIS_PER_SECOND);
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

            // Alarm
            sendFactionAlarmAlert(faction);
        }
    }

    public void sendFactionAlarmAlert(Faction faction)
    {
        // Loop - MPlayers
        for (MPlayer mplayer : faction.getMPlayersWhereOnline(true))
        {
            // Verify
            if (!mplayer.hasAlertNotifications()) continue;

            // Args
            Player player = mplayer.getPlayer();

            // Inform
            mplayer.msg("<b><bold>ALERT: %s <i>has sounded the alarm, get to the walls!", faction.describeTo(mplayer, true));
            if (player != null && MConf.get().alarmVolume > 0.0f) player.playSound(player.getLocation(), MConf.get().alarmSound, MConf.get().alarmVolume, MConf.get().alarmPitch);
        }
    }

}
