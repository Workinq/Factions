package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.store.Coll;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AuditEntryColl extends Coll<AuditEntry>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static AuditEntryColl i = new AuditEntryColl();
	public static AuditEntryColl get() { return i; }

	// Newest first. Reused by queries and pruning.
	public static final Comparator<AuditEntry> NEWEST_FIRST =
		Comparator.comparingLong(AuditEntry::getCreatedMillis).reversed();

	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //

	@Override
	public void setActive(boolean active)
	{
		super.setActive(active); // initLoadAllFromRemote loads every entry into memory
		if (!active) return;
		// Load-time trim so steady-state memory matches the configured caps.
		this.pruneAll();
	}

	// -------------------------------------------- //
	// QUERIES (in-memory; n is bounded by retention)
	// -------------------------------------------- //

	public List<AuditEntry> getEntriesForFaction(String factionId)
	{
		return this.getAll(entry -> factionId.equals(entry.getFactionId()), NEWEST_FIRST);
	}

	public List<AuditEntry> getAllNewestFirst()
	{
		return this.getAll(NEWEST_FIRST);
	}

	public int countForFaction(String factionId)
	{
		int count = 0;
		for (AuditEntry entry : this.getAll()) if (factionId.equals(entry.getFactionId())) count++;
		return count;
	}

	// -------------------------------------------- //
	// RETENTION / PRUNE
	// -------------------------------------------- //

	// Three rules applied in order. Each prune = entry.detach() (removes from memory + deletes the file).
	public int pruneAll()
	{
		MConf conf = MConf.get();
		long now = System.currentTimeMillis();
		long maxAgeMillis = (long) conf.auditRetentionDays * 24L * 60L * 60L * 1000L;
		int perFactionCap = conf.auditMaxEntriesPerFaction;
		int globalCap = conf.auditGlobalMaxEntries;

		int removed = 0;

		// RULE 1: age.
		if (conf.auditRetentionDays > 0)
		{
			for (AuditEntry entry : new ArrayList<>(this.getAll()))
			{
				if (now - entry.getCreatedMillis() <= maxAgeMillis) continue;
				entry.detach();
				removed++;
			}
		}

		// RULE 2: per-faction cap (keep newest perFactionCap each).
		if (perFactionCap > 0)
		{
			Map<String, List<AuditEntry>> byFaction = new MassiveMap<>();
			for (AuditEntry entry : this.getAll())
			{
				byFaction.computeIfAbsent(entry.getFactionId(), key -> new ArrayList<>()).add(entry);
			}
			for (List<AuditEntry> group : byFaction.values())
			{
				if (group.size() <= perFactionCap) continue;
				group.sort(NEWEST_FIRST);
				for (int idx = perFactionCap; idx < group.size(); idx++)
				{
					group.get(idx).detach();
					removed++;
				}
			}
		}

		// RULE 3: global cap (keep newest globalCap).
		if (globalCap > 0)
		{
			List<AuditEntry> survivors = this.getAll(NEWEST_FIRST);
			for (int idx = globalCap; idx < survivors.size(); idx++)
			{
				survivors.get(idx).detach();
				removed++;
			}
		}

		return removed;
	}

	// Trim a single faction back to the per-faction cap. Called at write time.
	public void trimFaction(String factionId)
	{
		int cap = MConf.get().auditMaxEntriesPerFaction;
		if (cap <= 0) return;
		List<AuditEntry> mine = this.getEntriesForFaction(factionId); // newest first
		for (int idx = cap; idx < mine.size(); idx++) mine.get(idx).detach();
	}
}
