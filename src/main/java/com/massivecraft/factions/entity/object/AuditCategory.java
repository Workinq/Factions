package com.massivecraft.factions.entity.object;

import org.bukkit.Material;

public enum AuditCategory
{
	MEMBERSHIP("Membership", Material.PLAYER_HEAD),  // join / leave / kick / move
	INVITE("Invites", Material.PAPER),               // invite / uninvite
	BAN("Bans", Material.IRON_BARS),                 // ban / unban
	MUTE("Mute", Material.NOTE_BLOCK),               // mute / unmute
	ROLE("Roles", Material.GOLDEN_HELMET),           // promote / demote / leader transfer / title
	TERRITORY("Territory", Material.GRASS_BLOCK),    // claim / unclaim
	MONEY("Economy", Material.GOLD_INGOT),           // deposit / withdraw / transfer / income
	CHEST("Containers", Material.CHEST),             // chest / container transactions
	LIFECYCLE("Faction", Material.TOTEM_OF_UNDYING), // create / disband
	INFO("Info", Material.NAME_TAG),                 // name / description / motd
	FLAG("Flags", Material.LEVER),                   // flag changes
	PERM("Perms", Material.REDSTONE),                // perm changes
	RELATION("Relations", Material.IRON_SWORD),      // ally / enemy / truce / neutral
	HOME("Home", Material.RED_BED),                  // sethome / unsethome
	STRIKE("Strikes", Material.BARRIER),             // admin strikes
	WARP("Warps", Material.ENDER_PEARL),             // warp create / delete

	// END OF LIST
	;

	private final String label;
	private final Material icon;

	AuditCategory(String label, Material icon)
	{
		this.label = label;
		this.icon = icon;
	}

	public String getLabel() { return this.label; }
	public Material getIcon() { return this.icon; }
}
