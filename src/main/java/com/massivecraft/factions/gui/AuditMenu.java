package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.AuditEntry;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.factions.util.AuditFormat;
import com.massivecraft.factions.util.AuditUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.PagedGui;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.massivecraft.factions.entity.object.AuditCategory.*;

public class AuditMenu extends PagedGui<AuditEntry>
{
	private final Faction faction;
	private Set<AuditCategory> filter;

	public AuditMenu(Player player, Faction faction, boolean adminScope)
	{
		super(player, 6, title(faction, adminScope));
		this.faction = faction;
	}

	private static String title(Faction faction, boolean adminScope)
	{
		String scope = faction == null ? "Server" : ChatColor.stripColor(faction.getName());
		return adminScope ? Txt.parse("<gray>Audit Log <silver>(%s)", scope) : Txt.parse("<gray>%s Log", scope);
	}

	@Override
	protected void build()
	{
		super.build();

		filterTab(1, Material.BOOK, "All", null);
		filterTab(2, MEMBERSHIP.getIcon(), "Members", group(MEMBERSHIP, INVITE, BAN, MUTE));
		filterTab(3, ROLE.getIcon(), "Roles", group(ROLE));
		filterTab(4, TERRITORY.getIcon(), "Land", group(TERRITORY));
		filterTab(5, MONEY.getIcon(), "Economy", group(MONEY, CHEST));
		filterTab(6, FLAG.getIcon(), "Settings", group(FLAG, PERM, RELATION, HOME, INFO));
		filterTab(7, STRIKE.getIcon(), "Admin", group(LIFECYCLE, STRIKE, WARP));

		button((getRows() - 1) * 9 + 4, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
	}

	@Override
	protected List<AuditEntry> items()
	{
		List<AuditEntry> all = this.faction == null
			? AuditUtil.queryGlobal(null, null, 0)
			: AuditUtil.query(this.faction, null, null, 0);
		if (this.filter == null) return all;

		List<AuditEntry> ret = new MassiveList<>();
		for (AuditEntry entry : all) if (this.filter.contains(entry.getCategory())) ret.add(entry);
		return ret;
	}

	@Override
	protected ItemStack icon(AuditEntry entry)
	{
		AuditCategory cat = entry.getCategory() != null ? entry.getCategory() : LIFECYCLE;
		return new ItemBuilder(cat.getIcon())
			.name(AuditFormat.describe(entry, getPlayer()))
			.withLore(Txt.parse(MUtil.list(
				"<i>By: " + AuditFormat.actorName(entry, getPlayer()),
				"<i>When: <h>" + AuditFormat.age(entry) + "<i> ago",
				"<i>Category: <h>" + cat.getLabel())));
	}

	@Override
	protected void onPick(Player player, AuditEntry entry)
	{
		player.sendMessage(AuditFormat.describe(entry, player));
	}

	private void filterTab(int slot, Material material, String name, Set<AuditCategory> group)
	{
		ItemStack icon = new ItemBuilder(material)
			.name(Txt.parse("<k>%s", name))
			.withLore(Txt.parse(MUtil.list("<n>Click to filter")));
		button(slot, icon, ctx -> { this.filter = group; this.refreshItems(); });
	}

	private static Set<AuditCategory> group(AuditCategory... cats)
	{
		Set<AuditCategory> set = EnumSet.noneOf(AuditCategory.class);
		set.addAll(Arrays.asList(cats));
		return set;
	}
}
