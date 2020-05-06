package com.massivecraft.factions.cmd.power;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsPowerBoost extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsPowerBoostPlayer cmdFactionsPowerBoostPlayer = new CmdFactionsPowerBoostPlayer();
	public CmdFactionsPowerBoostFaction cmdFactionsPowerBoostFaction = new CmdFactionsPowerBoostFaction();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPowerBoost()
	{
		// Child
		this.addChild(this.cmdFactionsPowerBoostPlayer);
		this.addChild(this.cmdFactionsPowerBoostFaction);
	}
	
}
