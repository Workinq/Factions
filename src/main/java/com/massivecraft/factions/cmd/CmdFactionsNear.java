package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdFactionsNear extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsNear()
	{
		// Aliases
		this.addAliases("near", "nearby");

		// Requirements
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(RequirementHasPerm.get(Perm.NEAR));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		Location origin = me.getLocation();
		int radius = MConf.get().nearRadius;
		long radiusSquared = (long) radius * radius;

		// Collect online, visible, non-alt faction members within radius (same world).
		Map<MPlayer, Double> nearby = new HashMap<>();
		for (MPlayer member : msenderFaction.getMPlayersWhereOnlineTo(msender))
		{
			if (member == msender) continue;
			if (member.isAlt()) continue;

			Player player = member.getPlayer();
			if (player == null) continue;

			Location location = player.getLocation();
			if ( ! origin.getWorld().equals(location.getWorld())) continue;

			double distanceSquared = origin.distanceSquared(location);
			if (distanceSquared > radiusSquared) continue;

			nearby.put(member, distanceSquared);
		}

		// None
		if (nearby.isEmpty())
		{
			msg("<i>No faction members are nearby.");
			return;
		}

		// Sort nearest first
		List<MPlayer> sorted = new ArrayList<>(nearby.keySet());
		sorted.sort(Comparator.comparingDouble(nearby::get));

		// Render
		List<String> entries = new ArrayList<>();
		for (MPlayer member : sorted)
		{
			long distance = Math.round(Math.sqrt(nearby.get(member)));
			entries.add(member.describeTo(msender, true) + Txt.parse("<i> (<h>%d<i>)", distance));
		}

		msg("<i>Nearby faction members: %s", Txt.implodeCommaAnd(entries, Txt.parse("<i>, "), Txt.parse(" <i>and ")));
	}

}
