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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.massivecraft.factions.entity.object.AuditCategory.*;

public class AuditMenu extends PagedGui<AuditEntry>
{
	private final Faction faction;
	private Set<AuditCategory> filter;
	private String filterLabel = "All";

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

	private record Tab(int slot, Material material, String label, Set<AuditCategory> group) {}

	private List<Tab> tabs()
	{
		return MUtil.list(
			new Tab(1, Material.BOOKSHELF, "All", null),
			new Tab(2, Material.PLAYER_HEAD, "Members", group(MEMBERSHIP, INVITE, BAN, MUTE)),
			new Tab(3, Material.GOLDEN_HELMET, "Roles", group(ROLE)),
			new Tab(4, Material.FILLED_MAP, "Land", group(TERRITORY)),
			new Tab(5, Material.GOLD_INGOT, "Economy", group(MONEY, CHEST)),
			new Tab(6, Material.COMPARATOR, "Settings", group(FLAG, PERM, RELATION, HOME, INFO)),
			new Tab(7, Material.NETHERITE_SWORD, "Admin", group(LIFECYCLE, STRIKE, WARP)));
	}

	@Override
	protected void build()
	{
		super.build();

		button(0, infoIcon(), ctx -> {});
		for (Tab tab : tabs())
		{
			button(tab.slot(), tabIcon(tab), ctx -> selectFilter(tab.group(), tab.label()));
		}

		button((getRows() - 1) * 9 + 4, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
	}

	private void selectFilter(Set<AuditCategory> group, String label)
	{
		this.filter = group;
		this.filterLabel = label;
		this.refreshItems();
		this.setItem(0, infoIcon());
		for (Tab tab : tabs()) this.setItem(tab.slot(), tabIcon(tab));
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
		return head(entry)
			.name(AuditFormat.describe(entry, getPlayer()))
			.withLore(Txt.parse(MUtil.list(
				"<silver>" + cat.getLabel(),
				"",
				"<i>By <h>" + AuditFormat.actorName(entry, getPlayer()),
				"<i><h>" + AuditFormat.age(entry) + "<i> ago",
				"",
				"<n>Click to print in chat")));
	}

	@Override
	protected void onPick(Player player, AuditEntry entry)
	{
		player.sendMessage(AuditFormat.describe(entry, player));
	}

	private ItemStack infoIcon()
	{
		String scope = this.faction == null ? "Server" : ChatColor.stripColor(this.faction.getName());
		return new ItemBuilder(Material.BOOK)
			.name(Txt.parse("<k><bold>%s Audit Log", scope))
			.withLore(Txt.parse(MUtil.list(
				"<i>Filter: <h>" + this.filterLabel,
				"<i>Entries: <h>" + this.items().size(),
				"",
				"<n>Pick a category to filter",
				"<n>Click an entry to print it")));
	}

	private ItemStack tabIcon(Tab tab)
	{
		boolean active = this.filterLabel.equals(tab.label());
		ItemBuilder icon = new ItemBuilder(tab.material())
			.name(Txt.parse(active ? "<g><bold>%s" : "<k>%s", tab.label()))
			.withLore(Txt.parse(MUtil.list(active ? "<g>▶ Showing" : "<n>Click to filter")));
		if (active)
		{
			icon.enchantment(Enchantment.UNBREAKING);
			icon.flag(ItemFlag.HIDE_ENCHANTS);
		}
		return icon;
	}

	private ItemBuilder head(AuditEntry entry)
	{
		String actorId = entry.getActorId();
		if (actorId == null) return new ItemBuilder(Material.COMMAND_BLOCK);
		ItemBuilder item = new ItemBuilder(Material.PLAYER_HEAD);
		try { item.owner(Bukkit.getOfflinePlayer(UUID.fromString(actorId))); }
		catch (RuntimeException ignored) {}
		return item;
	}

	private static Set<AuditCategory> group(AuditCategory... cats)
	{
		Set<AuditCategory> set = EnumSet.noneOf(AuditCategory.class);
		set.addAll(List.of(cats));
		return set;
	}
}
