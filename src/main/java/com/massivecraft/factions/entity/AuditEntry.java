package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.object.AuditAction;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.store.Entity;

import java.util.Map;

/**
 * One persisted audit-log record. Stored in its own {@link AuditEntryColl} (one entity per entry)
 * so entries survive faction disband and can be queried globally.
 * <p>
 * Fields are structured (ids + cached display names) and rendered at view time by
 * {@code AuditFormat}, so renamed players and disbanded factions still read sensibly.
 */
public class AuditEntry extends Entity<AuditEntry>
{
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //

	// Plain accessor-based copy; no EntityInternalMap fields to merge.
	@Override
	public AuditEntry load(AuditEntry that)
	{
		super.load(that);
		return this;
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	// When the action happened (epoch millis). Primary sort key.
	private long createdMillis = 0L;
	public long getCreatedMillis() { return this.createdMillis; }
	public void setCreatedMillis(long createdMillis) { this.createdMillis = createdMillis; }

	// Who performed the action. UUID string for a player; null for an automated/server action.
	private String actorId = null;
	public String getActorId() { return this.actorId; }
	public void setActorId(String actorId) { this.actorId = actorId; }

	// Cached actor display name at log time (player may be renamed/purged later).
	private String actorName = null;
	public String getActorName() { return this.actorName; }
	public void setActorName(String actorName) { this.actorName = actorName; }

	// Coarse bucket (filter tab + icon).
	private AuditCategory category = AuditCategory.LIFECYCLE;
	public AuditCategory getCategory() { return this.category; }
	public void setCategory(AuditCategory category) { this.category = category; }

	// Fine action within the category.
	private AuditAction action = AuditAction.CREATE;
	public AuditAction getAction() { return this.action; }
	public void setAction(AuditAction action) { this.action = action; }

	// The faction this entry belongs to. Stored by id (+ cached name) so it survives disband.
	private String factionId = null;
	public String getFactionId() { return this.factionId; }
	public void setFactionId(String factionId) { this.factionId = factionId; }

	private String factionName = null;
	public String getFactionName() { return this.factionName; }
	public void setFactionName(String factionName) { this.factionName = factionName; }

	// Optional target: a member (kick/ban/promote/...) or the other faction (relations). id + cached name.
	private String targetId = null;
	public String getTargetId() { return this.targetId; }
	public void setTargetId(String targetId) { this.targetId = targetId; }

	private String targetName = null;
	public String getTargetName() { return this.targetName; }
	public void setTargetName(String targetName) { this.targetName = targetName; }

	// Flexible structured payload (amounts, old/new values, coords, item summaries...).
	private Map<String, String> details = new MassiveMap<>();
	public Map<String, String> getDetails() { return this.details; }
	public String getDetail(String key) { return this.details.get(key); }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public AuditEntry() {}

	// isDefault() intentionally NOT overridden -> stays false, so no entry is ever dropped as "default".
}
