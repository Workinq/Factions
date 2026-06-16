package com.massivecraft.factions.util;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.AuditEntry;
import com.massivecraft.factions.entity.AuditEntryColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.AuditAction;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

/**
 * The single facade for the audit log: both writing entries ({@link #log}) and reading them
 * ({@link #query} / {@link #queryGlobal}). Keeping both here guarantees the chat command and the
 * GUI filter/sort identically.
 * <p>
 * {@link #log} is main-thread only by contract — every caller is a synchronous Bukkit event or
 * command. It does not self-reschedule, because a bounced call would read post-mutation state and
 * corrupt the "old value" snapshot the listeners capture at MONITOR priority.
 */
public final class AuditUtil
{
	private AuditUtil() {}

	// -------------------------------------------- //
	// LOG
	// -------------------------------------------- //

	public static AuditEntry log(AuditCategory category, AuditAction action, CommandSender actor,
								 Faction faction, String targetId, Map<String, String> details)
	{
		MConf conf = MConf.get();
		if (!conf.auditEnabled) return null;
		if (!isCategoryEnabled(conf, category)) return null;

		if (!Bukkit.isPrimaryThread())
		{
			Factions.get().log("AuditUtil.log called off the main thread for " + category + "/" + action + "; skipped.");
			return null;
		}

		AuditEntryColl coll = AuditEntryColl.get();
		if (!coll.isActive()) return null;

		AuditEntry entry = coll.create();
		entry.setCreatedMillis(System.currentTimeMillis());
		entry.setCategory(category);
		entry.setAction(action);

		if (actor != null)
		{
			entry.setActorId(IdUtil.getId(actor));
			entry.setActorName(IdUtil.getName(actor));
		}
		if (faction != null)
		{
			entry.setFactionId(faction.getId());
			entry.setFactionName(faction.getName());
		}
		applyTarget(entry, targetId);

		if (details != null && !details.isEmpty()) entry.getDetails().putAll(details);
		entry.changed();

		// Retention is enforced by TaskAuditPrune (periodic) and load-time pruning. We deliberately do
		// NOT trim per write here: that would be an O(n) scan on a hot path (e.g. every chest item move).
		return entry;
	}

	public static AuditEntry log(AuditCategory category, AuditAction action, CommandSender actor, Faction faction, String targetId)
	{
		return log(category, action, actor, faction, targetId, null);
	}

	public static AuditEntry log(AuditCategory category, AuditAction action, CommandSender actor, Faction faction)
	{
		return log(category, action, actor, faction, null, null);
	}

	// Fluent details builder: AuditUtil.details().put("amount", "500").map()
	public static Details details() { return new Details(); }

	public static final class Details
	{
		private final Map<String, String> map = new MassiveMap<>();
		public Details put(String key, String value) { this.map.put(key, value); return this; }
		public Map<String, String> map() { return this.map; }
	}

	// -------------------------------------------- //
	// QUERY (shared by chat command and GUI)
	// -------------------------------------------- //

	public static List<AuditEntry> query(Faction faction, AuditCategory category, MPlayer actor, int days)
	{
		String factionId = faction.getId();
		return filter(factionId, category, actor, days);
	}

	public static List<AuditEntry> queryGlobal(AuditCategory category, MPlayer actor, int days)
	{
		return filter(null, category, actor, days);
	}

	private static List<AuditEntry> filter(String factionId, AuditCategory category, MPlayer actor, int days)
	{
		String actorId = actor == null ? null : actor.getId();
		long cutoff = days > 0 ? System.currentTimeMillis() - (long) days * TimeUnit.MILLIS_PER_DAY : 0L;

		List<AuditEntry> ret = new MassiveList<>();
		for (AuditEntry entry : AuditEntryColl.get().getAll())
		{
			if (factionId != null && !factionId.equals(entry.getFactionId())) continue;
			if (category != null && entry.getCategory() != category) continue;
			if (actorId != null && !actorId.equals(entry.getActorId())) continue;
			if (cutoff > 0 && entry.getCreatedMillis() < cutoff) continue;
			ret.add(entry);
		}
		ret.sort(AuditEntryColl.NEWEST_FIRST);
		return ret;
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	private static boolean isCategoryEnabled(MConf conf, AuditCategory category)
	{
		Boolean enabled = conf.auditCategoryEnabled.get(category);
		return enabled == null || enabled;
	}

	private static void applyTarget(AuditEntry entry, String targetId)
	{
		entry.setTargetId(targetId);
		if (targetId == null) return;

		// Best-effort cached name: a player, else a faction, else leave null.
		String name = IdUtil.getName(targetId);
		if (name == null)
		{
			Faction faction = FactionColl.get().get(targetId);
			if (faction != null) name = faction.getName();
		}
		entry.setTargetName(name);
	}
}
