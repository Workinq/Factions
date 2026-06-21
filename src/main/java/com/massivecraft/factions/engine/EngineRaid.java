package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class EngineRaid extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineRaid i = new EngineRaid();
	public static EngineRaid get() { return i; }

	private final transient Map<Faction, RaidPeriod> activeByDefender = new MassiveMap<>();

	// -------------------------------------------- //
	// DETECTION
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExplosion(EntityExplodeEvent event)
	{
		if ( ! MConf.get().raidClaimsEnabled) return;

		Entity entity = event.getEntity();
		if ( ! (entity instanceof TNTPrimed)) return;

		Location sourceLocation = ((TNTPrimed) entity).getSourceLoc();
		if (sourceLocation == null) return;

		PS source = PS.valueOf(sourceLocation);
		if ( ! BoardColl.get().isRaidClaim(source)) return;

		Faction raider = BoardColl.get().getFactionAt(source);
		if (raider == null || ! raider.isNormal()) return;

		RaidPeriod period = this.getActiveRaid(raider);

		PS explosion = PS.valueOf(event.getLocation()).getChunk(true);
		Faction defender = BoardColl.get().getFactionAt(explosion);
		if (defender != null && defender.isNormal() && defender != raider && this.isBreach(defender, explosion))
		{
			period = this.startOrRefresh(raider, defender);
		}

		if (period != null) period.tntUsed++;
	}

	private boolean isBreach(Faction defender, PS chunk)
	{
		if ( ! defender.hasBaseRegion()) return false;
		if ( ! defender.getBaseRegion().contains(chunk)) return false;
		if (MConf.get().shield && defender.isShieldedAt(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) return false;
		return true;
	}

	private RaidPeriod getActiveRaid(Faction raider)
	{
		for (RaidPeriod period : this.activeByDefender.values())
		{
			if (period.raider == raider) return period;
		}
		return null;
	}

	private RaidPeriod startOrRefresh(Faction raider, Faction defender)
	{
		long now = System.currentTimeMillis();

		RaidPeriod period = this.activeByDefender.get(defender);
		if (period == null)
		{
			period = new RaidPeriod(raider, defender, now);
			this.activeByDefender.put(defender, period);
			raider.msg("<i>Your faction is now raiding <h>%s<i>.", defender.getName());
			defender.msg("<b>Your faction is being raided by <h>%s<b>!", raider.getName());
			return period;
		}

		if (period.raider != raider) return null;

		period.lastActivityMillis = now;
		return period;
	}

	// -------------------------------------------- //
	// STATS
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		if (MUtil.isntPlayer(player)) return;
		if (PlayerUtil.isDuplicateDeathEvent(event)) return;

		Player killerPlayer = player.getKiller();
		if (killerPlayer == null) return;

		Faction victimFaction = MPlayer.get(player).getFaction();
		Faction killerFaction = MPlayer.get(killerPlayer).getFaction();

		RaidPeriod defenderRaid = this.activeByDefender.get(victimFaction);
		if (defenderRaid != null && defenderRaid.raider == killerFaction)
		{
			defenderRaid.raiderKills++;
			return;
		}

		RaidPeriod raiderRaid = this.activeByDefender.get(killerFaction);
		if (raiderRaid != null && raiderRaid.raider == victimFaction)
		{
			raiderRaid.defenderKills++;
		}
	}

	// -------------------------------------------- //
	// LIFECYCLE
	// -------------------------------------------- //

	public void endExpiredRaids(long now)
	{
		long cooldown = MConf.get().raidPeriodCooldownMillis;
		for (RaidPeriod period : new MassiveList<>(this.activeByDefender.values()))
		{
			if (now - period.lastActivityMillis < cooldown) continue;
			this.endRaid(period);
		}
	}

	private void endRaid(RaidPeriod period)
	{
		this.activeByDefender.remove(period.defender);

		long durationMillis = period.lastActivityMillis - period.startMillis;
		LinkedHashMap<TimeUnit, Long> unitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(durationMillis, TimeUnit.getAllButMillis()), 2);
		String duration = TimeDiffUtil.formatedMinimal(unitcounts, "<h>");

		period.raider.msg("<i>Your raid on <h>%s<i> ended after <h>%s<i> with <h>%d<i> TNT used, <h>%d<i> kills and <h>%d<i> deaths.", period.defender.getName(), duration, period.tntUsed, period.raiderKills, period.defenderKills);
		period.defender.msg("<i>The raid by <h>%s<i> ended after <h>%s<i> with <h>%d<i> TNT used, <h>%d<i> kills and <h>%d<i> deaths.", period.raider.getName(), duration, period.tntUsed, period.defenderKills, period.raiderKills);
	}

	// -------------------------------------------- //
	// RAID PERIOD
	// -------------------------------------------- //

	public static class RaidPeriod
	{
		public final Faction raider;
		public final Faction defender;
		public final long startMillis;
		public long lastActivityMillis;
		public long tntUsed = 0;
		public int raiderKills = 0;
		public int defenderKills = 0;

		public RaidPeriod(Faction raider, Faction defender, long now)
		{
			this.raider = raider;
			this.defender = defender;
			this.startMillis = now;
			this.lastActivityMillis = now;
		}
	}

}
