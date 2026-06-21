package com.massivecraft.factions.task;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.TimeUnit;

import java.util.Map.Entry;

public class TaskRaidClaimExpire extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //

	private static TaskRaidClaimExpire i = new TaskRaidClaimExpire();
	public static TaskRaidClaimExpire get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public long getDelayMillis()
	{
		return (long) (MConf.get().taskRaidClaimExpireMinutes * TimeUnit.MILLIS_PER_MINUTE);
	}

	@Override
	public void invoke(long now)
	{
		for (Entry<PS, Long> entry : BoardColl.get().getRaidClaims().entrySet())
		{
			Long expiry = entry.getValue();
			if (expiry == null || now < expiry) continue;

			PS chunk = entry.getKey();
			Faction faction = BoardColl.get().getFactionAt(chunk);
			BoardColl.get().removeAt(chunk);

			if (faction == null || ! faction.isNormal()) continue;
			if (faction.getCoreChunk() != null) faction.recalculateBaseRegion();
			faction.msg("<i>A raid claim at <h>%d, %d<i> has expired.", chunk.getChunkX(), chunk.getChunkZ());
		}
	}

}
