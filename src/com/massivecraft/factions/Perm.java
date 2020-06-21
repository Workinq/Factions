package com.massivecraft.factions;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	ACCESS,
	ACCESS_VIEW,
	ACCESS_PLAYER,
	ACCESS_FACTION,
	ACCESS_CLEAR,
	OVERRIDE,
	BASECOMMAND,
	CLAIM,
	CLAIM_ONE,
	CLAIM_AUTO,
	CLAIM_FILL,
	CLAIM_SQUARE,
	CLAIM_CIRCLE,
	CLAIM_ALL,
	CLAIM_LINE,
	CREATE,
	DESCRIPTION,
	DISBAND,
	EXPANSIONS,
	FACTION,
	FLAG,
	FLAG_LIST,
	FLAG_SET,
	FLAG_SHOW,
	HOME,
	INVITE,
	INVITELIST,
	INVITELIST_OTHER,
	DEINVITE,
	JOIN,
	JOIN_OTHERS,
	KICK,
	LEAVE,
	LIST,
	MAP,
	MONEY,
	MONEY_BALANCE,
	MONEY_BALANCE_ANY,
	MONEY_DEPOSIT,
	MONEY_F2F,
	MONEY_F2P,
	MONEY_P2F,
	MONEY_WITHDRAW,
	MOTD,
	OPEN,
	PERM,
	PERM_LIST,
	PERM_SET,
	PERM_SHOW,
	PERM_GUI,
	PLAYER,
	POWERBOOST,
	POWERBOOST_PLAYER,
	POWERBOOST_FACTION,
	POWERBOOST_SET,
	RANK,
	RANK_SHOW,
	RANK_ACTION,
	RELATION,
	RELATION_SET,
	RELATION_CHECK,
	RELATION_LIST,
	RELATION_WISHES,
	SEECHUNK,
	SEECHUNKOLD,
	SETHOME,
	SETPOWER,
	STATUS,
	NAME,
	TITLE,
	TITLE_COLOR,
	TERRITORYTITLES,
	UNCLAIM,
	UNCLAIM_ONE,
	UNCLAIM_AUTO,
	UNCLAIM_FILL,
	UNCLAIM_SQUARE,
	UNCLAIM_CIRCLE,
	UNCLAIM_ALL,
	UNCLAIM_LINE,
	UNSETHOME,
	UNSTUCK,
	CONFIG,
	CLEAN,
	VERSION,
	CHEST,
	CHEST_OPEN,
	CHEST_LOG,
	TNT,
	TNT_BALANCE,
	TNT_BALANCE_ANY,
	TNT_DEPOSIT,
	TNT_SET,
	TNT_SET_ANY,
	TNT_WITHDRAW,
	TNT_FILL,
	TNT_UNFILL,
	STEALTH,
	FLY,
	FLY_ANY,
	WARP,
	WARPLIST,
	WARPLIST_OTHER,
	SETWARP,
	DELWARP,
	DISCORD,
	DISCORD_SET,
	DISCORD_CHECK,
	DISCORD_CHECK_OTHER,
	PAYPAL,
	PAYPAL_SET,
	PAYPAL_CHECK,
	PAYPAL_CHECK_OTHER,
	BAN,
	UNBAN,
	BANLIST,
	BANLIST_OTHER,
	LOCATION,
	CHAT,
	SPY,
	INSPECT,
	INSPECT_ANY,
	LAST_INSPECTED,
	IGNORE,
	UNIGNORE,
	ALT,
	ALT_INVITE,
	ALT_INVITELIST,
	ALT_INVITELIST_OTHERS,
	ALT_DEINVITE,
	ALT_JOIN,
	ALT_JOIN_OTHERS,
	MISSION,
	UPGRADE,
	CREDITS,
	CREDITS_BALANCE,
	CREDITS_BALANCE_ANY,
	CREDITS_F2F,
	CREDITS_ADD,
	CREDITS_TAKE,
	CREDITS_SET,
	STRIKE,
	STRIKE_ADD,
	STRIKE_CHECK,
	STRIKE_CHECK_OTHER,
	STRIKE_LIST,
	STRIKE_REMOVE,
	SETBASEREGION,
	SHIELD,
	SHIELD_SET,
	SHIELD_VIEW,
	SHIELD_VIEW_ANY,
	SHIELD_TOGGLE,
	FOCUS,
	UNFOCUS,
	BALTOP,
	INVSEE,
	/*SHARDS,
	SHARDS_BALANCE,
	SHARDS_BALANCE_ANY,
	SHARDS_SHOP,
	SHARDS_ADD,
	SHARDS_ADD_PLAYER,
	SHARDS_ADD_FACTION,
	SPAWNER_UPGRADE,*/
	ROSTER,
	ROSTER_ADD,
	ROSTER_REMOVE,
	ROSTER_SETRANK,
	ROSTER_LIST,
	ROSTER_LIST_ANY,
	DRAIN,
	DRAIN_TOGGLE,
	DRAIN_TOGGLE_ANY,
	SANDALT,
	SANDALT_GUI,
	SANDALT_KILLALL,
	LOGIN,
	FF,
	TOGGLE,
	TOGGLE_FLY,
	TOGGLE_GRACE,
	TOGGLE_SHIELDS,
	LOOT,
	LOOT_ADD,
	LOOT_CLAIM,
	LOOT_BALANCE,
	LOOT_BALANCE_ANY,

	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String id;
	@Override public String getId() { return this.id; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Perm()
	{
		this.id = PermissionUtil.createPermissionId(Factions.get(), this);
	}
	
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public boolean has(Permissible permissible, boolean verboose)
	{
		return PermissionUtil.hasPermission(permissible, this, verboose);
	}
	
	public boolean has(Permissible permissible)
	{
		return PermissionUtil.hasPermission(permissible, this);
	}
	
}
