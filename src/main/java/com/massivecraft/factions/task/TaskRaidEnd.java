package com.massivecraft.factions.task;

import com.massivecraft.factions.engine.EngineRaid;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.TimeUnit;

public class TaskRaidEnd extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //

	private static TaskRaidEnd i = new TaskRaidEnd();
	public static TaskRaidEnd get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public long getDelayMillis()
	{
		return (long) (MConf.get().taskRaidEndMinutes * TimeUnit.MILLIS_PER_MINUTE);
	}

	@Override
	public void invoke(long now)
	{
		EngineRaid.get().endExpiredRaids(now);
	}

}
