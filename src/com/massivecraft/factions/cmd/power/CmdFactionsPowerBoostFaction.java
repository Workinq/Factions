package com.massivecraft.factions.cmd.power;

import com.massivecraft.factions.cmd.type.TypeFaction;

public class CmdFactionsPowerBoostFaction extends CmdFactionsPowerBoostAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPowerBoostFaction()
	{
		super(TypeFaction.get(), "faction");

		// Aliases
		this.addAliases("faction");

		// Desc
		this.setDescPermission("factions.powerboost.faction");
	}
	
}
