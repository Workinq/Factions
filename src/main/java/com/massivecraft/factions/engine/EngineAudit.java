package com.massivecraft.factions.engine;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.AuditAction;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.factions.entity.object.FactionStrike;
import com.massivecraft.factions.event.*;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.factions.util.AuditUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Translates Factions events into {@link AuditUtil#log} calls. A thin, non-invasive listener:
 * every handler runs at MONITOR with ignoreCancelled, so only successful, non-cancelled actions
 * are recorded. Reading "old" state at MONITOR works because the command applies the mutation after
 * the event returns.
 * <p>
 * Auto-discovered through {@code super.getClassesActiveEngines()} — it is NOT registered explicitly.
 */
public class EngineAudit extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineAudit i = new EngineAudit();
	public static EngineAudit get() { return i; }

	// -------------------------------------------- //
	// MEMBERSHIP (DISBAND reason is covered by the single LIFECYCLE/DISBAND summary)
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMembershipChange(EventFactionsMembershipChange event)
	{
		MPlayer mplayer = event.getMPlayer();
		if (mplayer == null) return;
		MembershipChangeReason reason = event.getReason();
		if (reason == MembershipChangeReason.DISBAND) return;

		Faction oldFaction = mplayer.getFaction(); // pre-apply -> the source faction
		Faction newFaction = event.getNewFaction();

		switch (reason)
		{
			case JOIN:
				AuditUtil.log(AuditCategory.MEMBERSHIP, AuditAction.JOIN, event.getSender(), newFaction, mplayer.getId(),
					AuditUtil.details().put("fromFaction", oldFaction.getId()).map());
				break;
			case CREATE:
				AuditUtil.log(AuditCategory.MEMBERSHIP, AuditAction.JOIN_CREATE, event.getSender(), newFaction, mplayer.getId());
				break;
			case LEAVE:
				AuditUtil.log(AuditCategory.MEMBERSHIP, AuditAction.LEAVE, event.getSender(), oldFaction, mplayer.getId());
				break;
			case KICK:
				AuditUtil.log(AuditCategory.MEMBERSHIP, AuditAction.KICK, event.getSender(), oldFaction, mplayer.getId());
				break;
			case RANK:
				AuditUtil.log(AuditCategory.MEMBERSHIP, AuditAction.MOVE, event.getSender(), newFaction, mplayer.getId(),
					AuditUtil.details().put("fromFaction", oldFaction.getId()).map());
				break;
			default:
				break;
		}
	}

	// -------------------------------------------- //
	// TERRITORY (one summary entry per event)
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunksChange(EventFactionsChunksChange event)
	{
		Set<PS> chunks = event.getChunks();
		if (chunks.isEmpty()) return;

		Faction newFaction = event.getNewFaction();
		boolean unclaim = !newFaction.isNormal();
		Faction owner = unclaim ? dominantOldFaction(event) : newFaction;

		List<String> from = new MassiveList<>();
		for (Faction faction : event.getOldFactionChunks().keySet())
		{
			if (faction.isNone() || faction == newFaction) continue;
			from.add(faction.getName());
			if (from.size() >= 5) { from.add("…"); break; }
		}

		StringBuilder types = new StringBuilder();
		for (Map.Entry<EventFactionsChunkChangeType, Set<PS>> entry : event.getTypeChunks().entrySet())
		{
			if (types.length() > 0) types.append(",");
			types.append(entry.getKey().name()).append("=").append(entry.getValue().size());
		}

		Set<String> worlds = new MassiveSet<>();
		for (PS ps : chunks) worlds.add(ps.getWorld());

		PS first = chunks.iterator().next();
		String sample = first.getWorld() + " " + first.getChunkX() + "," + first.getChunkZ()
			+ (chunks.size() > 1 ? " (+" + (chunks.size() - 1) + " more)" : "");

		AuditUtil.log(AuditCategory.TERRITORY, unclaim ? AuditAction.UNCLAIM : AuditAction.CLAIM,
			event.getSender(), owner, null,
			AuditUtil.details()
				.put("count", String.valueOf(chunks.size()))
				.put("worlds", Txt.implode(worlds, ","))
				.put("sample", sample)
				.put("fromFactions", Txt.implode(from, ","))
				.put("types", types.toString())
				.map());
	}

	// -------------------------------------------- //
	// LIFECYCLE
	// -------------------------------------------- //

	// NOTE: faction CREATE is logged from CmdFactionsCreate (the faction does not exist yet when
	// EventFactionsCreate fires, so it cannot be resolved here).

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDisband(EventFactionsDisband event)
	{
		// Fired before faction.detach() and before per-member DISBAND events -> safe to read name/members.
		Faction faction = event.getFaction();
		List<MPlayer> members = faction.getMPlayers();

		StringBuilder ids = new StringBuilder();
		int cap = 0;
		for (MPlayer mplayer : members)
		{
			if (cap++ >= 20) { ids.append(",…"); break; }
			if (ids.length() > 0) ids.append(",");
			ids.append(mplayer.getId());
		}

		AuditUtil.log(AuditCategory.LIFECYCLE, AuditAction.DISBAND, event.getSender(), faction, null,
			AuditUtil.details()
				.put("name", faction.getName())
				.put("memberCount", String.valueOf(members.size()))
				.put("members", ids.toString())
				.map());
	}

	// -------------------------------------------- //
	// ROLES
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRankChange(EventFactionsRankChange event)
	{
		MPlayer mplayer = event.getMPlayer();
		if (mplayer == null) return;
		Faction faction = mplayer.getFaction();
		Rel newRank = event.getNewRank();
		Rel oldRank = mplayer.getRole(); // pre-apply

		if (newRank == Rel.LEADER)
		{
			MPlayer oldLeader = faction.getLeader();
			AuditUtil.log(AuditCategory.ROLE, AuditAction.LEADER_TRANSFER, event.getSender(), faction, mplayer.getId(),
				AuditUtil.details()
					.put("oldLeaderId", oldLeader == null ? "" : oldLeader.getId())
					.put("newRank", newRank.name())
					.map());
		}
		else
		{
			AuditUtil.log(AuditCategory.ROLE, AuditAction.RANK_SET, event.getSender(), faction, mplayer.getId(),
				AuditUtil.details().put("oldRank", oldRank.name()).put("newRank", newRank.name()).map());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTitleChange(EventFactionsTitleChange event)
	{
		MPlayer mplayer = event.getMPlayer();
		if (mplayer == null) return;
		AuditUtil.log(AuditCategory.ROLE, AuditAction.TITLE_SET, event.getSender(), mplayer.getFaction(), mplayer.getId(),
			AuditUtil.details().put("newTitle", String.valueOf(event.getNewTitle())).map());
	}

	// -------------------------------------------- //
	// INVITE / BAN
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInvitedChange(EventFactionsInvitedChange event)
	{
		MPlayer mplayer = event.getMPlayer();
		AuditUtil.log(AuditCategory.INVITE, event.isNewInvited() ? AuditAction.INVITE_ADD : AuditAction.INVITE_REVOKE,
			event.getSender(), event.getFaction(), mplayer == null ? null : mplayer.getId());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBanChange(EventFactionsBanChange event)
	{
		MPlayer mplayer = event.getMPlayer();
		AuditUtil.log(AuditCategory.BAN, event.isNewBanned() ? AuditAction.BAN_ADD : AuditAction.BAN_REMOVE,
			event.getSender(), event.getFaction(), mplayer == null ? null : mplayer.getId());
	}

	// -------------------------------------------- //
	// FLAG / PERM / RELATION
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onFlagChange(EventFactionsFlagChange event)
	{
		AuditUtil.log(AuditCategory.FLAG, AuditAction.FLAG_SET, event.getSender(), event.getFaction(), null,
			AuditUtil.details().put("flag", event.getFlag().getId()).put("value", String.valueOf(event.isNewValue())).map());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPermChange(EventFactionsPermChange event)
	{
		AuditUtil.log(AuditCategory.PERM, AuditAction.PERM_SET, event.getSender(), event.getFaction(), null,
			AuditUtil.details()
				.put("perm", event.getPerm().getId())
				.put("rel", event.getRel().name())
				.put("value", String.valueOf(event.getNewValue()))
				.map());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRelationChange(EventFactionsRelationChange event)
	{
		Faction other = event.getOtherFaction();
		AuditUtil.log(AuditCategory.RELATION, AuditAction.RELATION_SET, event.getSender(), event.getFaction(), other.getId(),
			AuditUtil.details().put("newRelation", event.getNewRelation().name()).put("otherName", other.getName()).map());
	}

	// -------------------------------------------- //
	// HOME
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onHomeChange(EventFactionsHomeChange event)
	{
		PS home = event.getNewHome();
		if (home == null)
		{
			AuditUtil.log(AuditCategory.HOME, AuditAction.HOME_UNSET, event.getSender(), event.getFaction());
		}
		else
		{
			AuditUtil.log(AuditCategory.HOME, AuditAction.HOME_SET, event.getSender(), event.getFaction(), null,
				AuditUtil.details().put("loc", describePs(home)).map());
		}
	}

	// -------------------------------------------- //
	// INFO: name / description / motd
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onNameChange(EventFactionsNameChange event)
	{
		AuditUtil.log(AuditCategory.INFO, AuditAction.NAME_SET, event.getSender(), event.getFaction(), null,
			AuditUtil.details().put("oldName", event.getFaction().getName()).put("newName", event.getNewName()).map());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDescriptionChange(EventFactionsDescriptionChange event)
	{
		AuditUtil.log(AuditCategory.INFO, AuditAction.DESCRIPTION_SET, event.getSender(), event.getFaction(), null,
			AuditUtil.details().put("newDescription", String.valueOf(event.getNewDescription())).map());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMotdChange(EventFactionsMotdChange event)
	{
		AuditUtil.log(AuditCategory.INFO, AuditAction.MOTD_SET, event.getSender(), event.getFaction(), null,
			AuditUtil.details().put("newMotd", String.valueOf(event.getNewMotd())).map());
	}

	// -------------------------------------------- //
	// STRIKE
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onStrikeAdd(EventFactionsStrikeAdd event)
	{
		FactionStrike strike = event.getNewStrike();
		AuditUtil.log(AuditCategory.STRIKE, AuditAction.STRIKE_ADD, event.getSender(), event.getFaction(), null,
			AuditUtil.details()
				.put("strikeId", strike.getStrikeId())
				.put("points", String.valueOf(strike.getPoints()))
				.put("reason", String.valueOf(strike.getMessage()))
				.put("issuedBy", String.valueOf(strike.getIssuedBy()))
				.map());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onStrikeRemove(EventFactionsStrikeRemove event)
	{
		FactionStrike strike = event.getNewStrike();
		AuditUtil.log(AuditCategory.STRIKE, AuditAction.STRIKE_REMOVE, event.getSender(), event.getFaction(), null,
			AuditUtil.details().put("strikeId", strike.getStrikeId()).put("points", String.valueOf(strike.getPoints())).map());
	}

	// -------------------------------------------- //
	// WARP
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWarpCreate(EventFactionsWarpCreate event)
	{
		AuditUtil.log(AuditCategory.WARP, AuditAction.WARP_CREATE, event.getSender(), event.getFaction(), null,
			AuditUtil.details()
				.put("warp", event.getNewWarp())
				.put("hasPassword", String.valueOf(event.getNewPassword() != null))
				.put("loc", event.getNewLocation() == null ? "" : describePs(event.getNewLocation()))
				.map());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWarpDelete(EventFactionsWarpDelete event)
	{
		AuditUtil.log(AuditCategory.WARP, AuditAction.WARP_DELETE, event.getSender(), event.getFaction(), null,
			AuditUtil.details().put("warp", event.getNewWarp()).map());
	}

	// -------------------------------------------- //
	// MONEY: spawner auto-sell income (bank in/out is logged from the money commands)
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMoneyChange(EventFactionsMoneyChange event)
	{
		AuditUtil.log(AuditCategory.MONEY, AuditAction.INCOME_SPAWNER, null, event.getFaction(), null,
			AuditUtil.details()
				.put("amount", String.valueOf(event.getMoney()))
				.put("source", "spawner")
				.put("loc", event.getSpawnerLocation() == null ? "" : event.getSpawnerLocation().toString())
				.map());
	}

	// -------------------------------------------- //
	// HELPERS
	// -------------------------------------------- //

	private static Faction dominantOldFaction(EventFactionsChunksChange event)
	{
		Faction best = event.getNewFaction();
		int bestN = -1;
		for (Map.Entry<Faction, Set<PS>> entry : event.getOldFactionChunks().entrySet())
		{
			if (entry.getKey().isNone()) continue;
			if (entry.getValue().size() > bestN) { best = entry.getKey(); bestN = entry.getValue().size(); }
		}
		return best;
	}

	private static String describePs(PS ps)
	{
		return ps.getWorld() + " " + coord(ps.getLocationX()) + "," + coord(ps.getLocationY()) + "," + coord(ps.getLocationZ());
	}

	private static String coord(Double value)
	{
		return value == null ? "?" : String.valueOf(value.intValue());
	}
}
