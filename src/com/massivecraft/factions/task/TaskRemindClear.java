package com.massivecraft.factions.task;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ModuloRepeatTask;

public class TaskRemindClear extends ModuloRepeatTask
{

    private static TaskRemindClear i = new TaskRemindClear();
    public static TaskRemindClear get() { return i; }

    @Override
    public long getDelayMillis()
    {
        // 1 second
        return 1000L;
    }

    @Override
    public void invoke(long now)
    {
        for (Faction faction : FactionColl.get().getAll())
        {
//            if (faction.getNotificationTimeMinutes() == 0) continue;
//            if (faction.getLastCheckedMillis() + faction.getNotificationTimeMinutes() * 60000 >= System.currentTimeMillis()) continue;

            // faction.msg("");
        }
    }

}
