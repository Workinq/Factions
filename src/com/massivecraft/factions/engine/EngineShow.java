package com.massivecraft.factions.engine;

import com.massivecraft.factions.comparator.ComparatorMPlayerRole;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.event.EventFactionsFactionShowAsync;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.util.TimeUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.PriorityLines;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.*;
import java.util.Map.Entry;

public class EngineShow extends Engine
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String BASENAME = "factions";
	public static final String BASENAME_ = BASENAME+"_";
	
	public static final String SHOW_ID_FACTION_ID = BASENAME_ + "id";
	public static final String SHOW_ID_FACTION_DESCRIPTION = BASENAME_ + "description";
	public static final String SHOW_ID_FACTION_DISCORD = BASENAME_ + "discord";
	public static final String SHOW_ID_FACTION_AGE = BASENAME_ + "age";
	public static final String SHOW_ID_FACTION_SHIELD = BASENAME_ + "shield";
	public static final String SHOW_ID_FACTION_STRIKES = BASENAME_ + "strikes";
	public static final String SHOW_ID_FACTION_FLAGS = BASENAME_ + "flags";
	public static final String SHOW_ID_FACTION_POWER = BASENAME_ + "power";
	public static final String SHOW_ID_FACTION_BANK = BASENAME_ + "bank";
	public static final String SHOW_ID_FACTION_FOLLOWERS = BASENAME_ + "followers";
	public static final String SHOW_ID_FACTION_ALTS = BASENAME_ + "alts";
	
	public static final int SHOW_PRIORITY_FACTION_ID = 1000;
	public static final int SHOW_PRIORITY_FACTION_DESCRIPTION = 2000;
	public static final int SHOW_PRIORITY_FACTION_DISCORD = 3000;
	public static final int SHOW_PRIORITY_FACTION_AGE = 4000;
	public static final int SHOW_PRIORITY_FACTION_SHIELD = 5000;
	public static final int SHOW_PRIORITY_FACTION_STRIKES = 6000;
	public static final int SHOW_PRIORITY_FACTION_FLAGS = 8000;
	public static final int SHOW_PRIORITY_FACTION_POWER = 9000;
	public static final int SHOW_PRIORITY_FACTION_BANK = 10000;
	public static final int SHOW_PRIORITY_FACTION_FOLLOWERS = 11000;
	public static final int SHOW_PRIORITY_FACTION_ALTS = 12000;

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineShow i = new EngineShow();
	public static EngineShow get() { return i; }

	// -------------------------------------------- //
	// FACTION SHOW
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onFactionShow(EventFactionsFactionShowAsync event)
	{
		final int tableCols = 4;
		final CommandSender sender = event.getSender();
		final MPlayer mplayer = event.getMPlayer();
		final Faction faction = event.getFaction();
		final boolean normal = faction.isNormal();
		final Map<String, PriorityLines> idPriorityLiness = event.getIdPriorityLiness();
		String none = Txt.parse("<silver><italic>none");

		// ID
		if (mplayer.isOverriding())
		{
			show(idPriorityLiness, SHOW_ID_FACTION_ID, SHOW_PRIORITY_FACTION_ID, "ID", faction.getId());
		}

		// DESCRIPTION
		show(idPriorityLiness, SHOW_ID_FACTION_DESCRIPTION, SHOW_PRIORITY_FACTION_DESCRIPTION, "Description", faction.getDescriptionDesc());

		// SECTION: NORMAL
		if (normal)
		{
			// DISCORD
			show(idPriorityLiness, SHOW_ID_FACTION_DISCORD, SHOW_PRIORITY_FACTION_DISCORD, "Discord", faction.getDiscord());

			// AGE
			long ageMillis = System.currentTimeMillis() - faction.getCreatedAtMillis();
			String ageString = TimeUtil.formatTime(ageMillis, true);
			show(idPriorityLiness, SHOW_ID_FACTION_AGE, SHOW_PRIORITY_FACTION_AGE, "Age", ageString);

			// SHIELD
			Calendar now = Calendar.getInstance();
			boolean active = faction.isShieldedAt(now.get(Calendar.HOUR_OF_DAY)) && MOption.get().isShield();
			String shieldString = Txt.parse(active ? "<g><bold>ACTIVE" : "<b><bold>INACTIVE");
			show(idPriorityLiness, SHOW_ID_FACTION_SHIELD, SHOW_PRIORITY_FACTION_SHIELD, "Shield", shieldString);

			// STRIKES
			int strikes = faction.getStrikes().size();
			int strikePoints = faction.getStrikePoints();
			show(idPriorityLiness, SHOW_ID_FACTION_STRIKES, SHOW_PRIORITY_FACTION_STRIKES, "Strikes / Points", Txt.parse("%d/%d", strikes, strikePoints));

			// FLAGS
			// We display all editable and non default ones. The rest we skip.
			List<String> flagDescs = new LinkedList<>();
			for (Entry<MFlag, Boolean> entry : faction.getFlags().entrySet())
			{
				final MFlag mflag = entry.getKey();
				if (mflag == null) continue;

				final Boolean value = entry.getValue();
				if (value == null) continue;

				if ( ! mflag.isInteresting(value)) continue;

				String flagDesc = Txt.parse(value ? "<g>" : "<b>") + mflag.getName();
				flagDescs.add(flagDesc);
			}
			String flagsDesc = Txt.parse("<silver><italic>default");
			if ( ! flagDescs.isEmpty())
			{
				flagsDesc = Txt.implode(flagDescs, Txt.parse(" <i>| "));
			}
			show(idPriorityLiness, SHOW_ID_FACTION_FLAGS, SHOW_PRIORITY_FACTION_FLAGS, "Flags", flagsDesc);

			// POWER
			double powerBoost = faction.getPowerBoost();
			String boost = (powerBoost == 0.0) ? "" : (powerBoost > 0.0 ? " (bonus: " : " (penalty: ") + powerBoost + ")";
			String powerDesc = Txt.parse("%d/%d/%d%s", faction.getLandCount(), faction.getPowerRounded(), faction.getPowerMaxRounded(), boost);
			show(idPriorityLiness, SHOW_ID_FACTION_POWER, SHOW_PRIORITY_FACTION_POWER, "Land / Power / Maxpower", powerDesc);

			// SECTION: ECON
			if (Econ.isEnabled())
			{
				// BANK
				if (MConf.get().bankEnabled)
				{
					double bank = Money.get(faction);
					String bankDesc = Txt.parse("<h>%s", Money.format(bank, true));
					show(idPriorityLiness, SHOW_ID_FACTION_BANK, SHOW_PRIORITY_FACTION_BANK, "Bank", bankDesc);
				}
			}
		}

		// FOLLOWERS
		List<String> followerLines = new ArrayList<>();

		List<String> followerNamesOnline = new ArrayList<>();
		List<String> followerNamesOffline = new ArrayList<>();

		List<MPlayer> followers = faction.getMPlayers();
		Collections.sort(followers, ComparatorMPlayerRole.get());
		for (MPlayer follower : followers)
		{
			if (follower.isAlt()) continue;

			if (follower.isOnline(sender))
			{
				followerNamesOnline.add(follower.getNameAndTitle(mplayer));
			}
			else if (normal)
			{
				// For the non-faction we skip the offline members since they are far to many (infinite almost)
				followerNamesOffline.add(follower.getNameAndTitle(mplayer));
			}
		}

		String headerFollowersOnline = Txt.parse("<a>Followers Online (%s):", followerNamesOnline.size());
		followerLines.add(headerFollowersOnline);
		if (followerNamesOnline.isEmpty())
		{
			followerLines.add(none);
		}
		else
		{
			followerLines.addAll(table(followerNamesOnline, tableCols));
		}

		if (normal)
		{
			String headerFollowersOffline = Txt.parse("<a>Followers Offline (%s):", followerNamesOffline.size());
			followerLines.add(headerFollowersOffline);
			if (followerNamesOffline.isEmpty())
			{
				followerLines.add(none);
			}
			else
			{
				followerLines.addAll(table(followerNamesOffline, tableCols));
			}
		}
		idPriorityLiness.put(SHOW_ID_FACTION_FOLLOWERS, new PriorityLines(SHOW_PRIORITY_FACTION_FOLLOWERS, followerLines));

		// ALTS
		List<String> altLines = new ArrayList<>();

		List<String> altNamesOnline = new ArrayList<>();
		List<String> altNamesOffline = new ArrayList<>();

		List<MPlayer> alts = faction.getMPlayers();
		Collections.sort(alts, ComparatorMPlayerRole.get());
		for (MPlayer alt : alts)
		{
			if ( ! alt.isAlt()) continue;

			if (alt.isOnline(sender))
			{
				altNamesOnline.add(alt.getNameAndTitle(mplayer));
			}
			else if (normal)
			{
				// For the non-faction we skip the offline members since they are far to many (infinite almost)
				altNamesOffline.add(alt.getNameAndTitle(mplayer));
			}
		}

		String headerAltsOnline = Txt.parse("<a>Alts Online (%s):", altNamesOnline.size());
		altLines.add(headerAltsOnline);
		if (altNamesOnline.isEmpty())
		{
			altLines.add(none);
		}
		else
		{
			altLines.addAll(table(altNamesOnline, tableCols));
		}

		if (normal)
		{
			String headerAltsOffline = Txt.parse("<a>Alts Offline (%s):", altNamesOffline.size());
			altLines.add(headerAltsOffline);
			if (altNamesOffline.isEmpty())
			{
				altLines.add(none);
			}
			else
			{
				altLines.addAll(table(altNamesOffline, tableCols));
			}
		}
		idPriorityLiness.put(SHOW_ID_FACTION_ALTS, new PriorityLines(SHOW_PRIORITY_FACTION_ALTS, altLines));
	}

	public static String show(String key, String value)
	{
		return Txt.parse("<a>%s: <i>%s", key, value);
	}

	public static PriorityLines show(int priority, String key, String value)
	{
		return new PriorityLines(priority, show(key, value));
	}

	public static void show(Map<String, PriorityLines> idPriorityLiness, String id, int priority, String key, String value)
	{
		idPriorityLiness.put(id, show(priority, key, value));
	}

	public static List<String> table(List<String> strings, int cols)
	{
		List<String> ret = new ArrayList<>();

		StringBuilder row = new StringBuilder();
		int count = 0;

		Iterator<String> iter = strings.iterator();
		while (iter.hasNext())
		{
			String string = iter.next();
			row.append(string);
			count++;

			if (iter.hasNext() && count != cols)
			{
				row.append(Txt.parse(" <i>| "));
			}
			else
			{
				ret.add(row.toString());
				row = new StringBuilder();
				count = 0;
			}
		}

		return ret;
	}

}
