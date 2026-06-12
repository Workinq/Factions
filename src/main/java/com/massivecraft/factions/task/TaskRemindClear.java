package com.massivecraft.factions.task;

import com.massivecraft.massivecore.ModuloRepeatTask;

public class TaskRemindClear extends ModuloRepeatTask
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static TaskRemindClear i = new TaskRemindClear();
    public static TaskRemindClear get() { return i; }

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
    }

}
