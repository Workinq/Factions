package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.object.AuditAction;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.store.Entity;

import java.util.Map;

public class AuditEntry extends Entity<AuditEntry>
{
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //

	@Override
	public AuditEntry load(AuditEntry that)
	{
		super.load(that);
		return this;
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private long createdMillis = 0L;
	public long getCreatedMillis() { return this.createdMillis; }
	public void setCreatedMillis(long createdMillis) { this.createdMillis = createdMillis; }

	private String actorId = null;
	public String getActorId() { return this.actorId; }
	public void setActorId(String actorId) { this.actorId = actorId; }

	private String actorName = null;
	public String getActorName() { return this.actorName; }
	public void setActorName(String actorName) { this.actorName = actorName; }

	private AuditCategory category = AuditCategory.LIFECYCLE;
	public AuditCategory getCategory() { return this.category; }
	public void setCategory(AuditCategory category) { this.category = category; }

	private AuditAction action = AuditAction.CREATE;
	public AuditAction getAction() { return this.action; }
	public void setAction(AuditAction action) { this.action = action; }

	private String factionId = null;
	public String getFactionId() { return this.factionId; }
	public void setFactionId(String factionId) { this.factionId = factionId; }

	private String factionName = null;
	public String getFactionName() { return this.factionName; }
	public void setFactionName(String factionName) { this.factionName = factionName; }

	private String targetId = null;
	public String getTargetId() { return this.targetId; }
	public void setTargetId(String targetId) { this.targetId = targetId; }

	private String targetName = null;
	public String getTargetName() { return this.targetName; }
	public void setTargetName(String targetName) { this.targetName = targetName; }

	private Map<String, String> details = new MassiveMap<>();
	public Map<String, String> getDetails() { return this.details; }
	public String getDetail(String key) { return this.details.get(key); }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public AuditEntry() {}

}
