package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeRank;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdFactionsRankViewAll extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRankViewAll()
	{
		// Aliases
		this.addAliases("rankviewall");

		// Parameters
		this.addParameter(TypeRank.get(), "rank");
		this.addParameter(TypeFaction.get(), "faction", "you");

		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.RANK_VIEWALL));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		Rel rank = this.readArgAt(0);
		Faction faction = this.readArgAt(1, msenderFaction);

		msg("<i>Players with the <h>%s <i>rank in <h>%s<i>:", rank.getName(), faction.getName());
		for (MPlayer mplayer : faction.getMPlayers())
		{
			if (mplayer.getRole() == rank) msg(mplayer.getName());
		}
	}

}
