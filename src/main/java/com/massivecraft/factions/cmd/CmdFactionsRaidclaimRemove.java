package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;

import java.util.Collections;

public class CmdFactionsRaidclaimRemove extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRaidclaimRemove()
	{
		this.addAliases("remove");
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementHasPerm.get(Perm.RAIDCLAIM_REMOVE));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		PS chunk = PS.valueOf(me.getLocation()).getChunk(true);
		if ( ! BoardColl.get().isRaidClaim(chunk)) throw new MassiveException().setMsg("<b>You are not standing on a raid claim.");

		Faction faction = BoardColl.get().getFactionAt(chunk);
		if (faction != msenderFaction && ! msender.isOverriding()) throw new MassiveException().setMsg("<b>This raid claim does not belong to your faction.");

		msender.tryClaim(FactionColl.get().getNone(), Collections.singleton(chunk), null, null);
		msg("<g>Raid claim removed.");
	}

}
