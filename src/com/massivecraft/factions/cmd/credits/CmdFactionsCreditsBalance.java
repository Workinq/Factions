package com.massivecraft.factions.cmd.credits;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsCreditsBalance extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsCreditsBalance()
	{
		// Parameters
		this.addParameter(TypeFaction.get(), "faction", "you");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Faction faction = this.readArg(msenderFaction);
					
		if (faction != msenderFaction && ! Perm.CREDITS_BALANCE_ANY.has(sender, true)) return;

		if ( ! MPerm.getPermCredits().has(msender, faction, true)) return;

		msender.msg("<a>%s's<i> credit balance is <h>%,d<i>.", faction.describeTo(msender, true), faction.getCredits());
	}
	
}
