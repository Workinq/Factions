package com.massivecraft.factions.task;

import com.massivecraft.factions.entity.AuditEntryColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.TimeUnit;

/**
 * Periodically prunes the audit log to the configured retention bounds.
 * Auto-discovered (the {@code task} package is scanned) and gated by the task server so only one
 * node prunes in a shared-database cluster.
 */
public class TaskAuditPrune extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //

	private static TaskAuditPrune i = new TaskAuditPrune();
	public static TaskAuditPrune get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public long getDelayMillis()
	{
		return (long) (MConf.get().auditPruneIntervalMinutes * TimeUnit.MILLIS_PER_MINUTE);
	}

	@Override
	public void invoke(long now)
	{
		if (!MassiveCore.isTaskServer()) return;
		if (!MConf.get().auditEnabled) return;
		AuditEntryColl.get().pruneAll();
	}
}
