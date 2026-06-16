package com.massivecraft.factions.entity.object;

import static com.massivecraft.factions.entity.object.AuditCategory.*;

/**
 * Fine-grained action recorded in an {@link AuditEntry}. Each constant declares its owning
 * {@link AuditCategory} so the logger cannot mismatch the two and the GUI can group entries.
 */
public enum AuditAction
{
	// MEMBERSHIP
	JOIN(MEMBERSHIP),
	JOIN_CREATE(MEMBERSHIP),
	LEAVE(MEMBERSHIP),
	KICK(MEMBERSHIP),
	MOVE(MEMBERSHIP),

	// INVITE
	INVITE_ADD(INVITE),
	INVITE_REVOKE(INVITE),

	// BAN
	BAN_ADD(BAN),
	BAN_REMOVE(BAN),

	// MUTE
	MUTE_ADD(MUTE),
	MUTE_REMOVE(MUTE),

	// ROLE
	RANK_SET(ROLE),
	LEADER_TRANSFER(ROLE),
	TITLE_SET(ROLE),

	// TERRITORY
	CLAIM(TERRITORY),
	UNCLAIM(TERRITORY),

	// MONEY
	DEPOSIT(MONEY),
	WITHDRAW(MONEY),
	TRANSFER_IN(MONEY),
	TRANSFER_OUT(MONEY),
	INCOME_SPAWNER(MONEY),

	// CHEST
	CHEST_PUT(CHEST),
	CHEST_TAKE(CHEST),

	// LIFECYCLE
	CREATE(LIFECYCLE),
	DISBAND(LIFECYCLE),

	// INFO
	NAME_SET(INFO),
	DESCRIPTION_SET(INFO),
	MOTD_SET(INFO),

	// FLAG
	FLAG_SET(FLAG),

	// PERM
	PERM_SET(PERM),

	// RELATION
	RELATION_SET(RELATION),

	// HOME
	HOME_SET(HOME),
	HOME_UNSET(HOME),

	// STRIKE
	STRIKE_ADD(STRIKE),
	STRIKE_REMOVE(STRIKE),

	// WARP
	WARP_CREATE(WARP),
	WARP_DELETE(WARP),
	;

	private final AuditCategory category;

	AuditAction(AuditCategory category)
	{
		this.category = category;
	}

	public AuditCategory getCategory() { return this.category; }
}
