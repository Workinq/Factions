package com.massivecraft.factions.cmd.shard;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsShardsBalance extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsShardsBalance()
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

		if (faction != msenderFaction && ! Perm.SHARDS_BALANCE_ANY.has(sender, true)) return;

		msender.msg("%s's<i> shard balance is <h>%,d<i>.", faction.describeTo(msender, true), faction.getShards());
	}
	
}
