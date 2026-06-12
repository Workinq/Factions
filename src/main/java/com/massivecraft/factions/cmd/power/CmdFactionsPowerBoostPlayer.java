package com.massivecraft.factions.cmd.power;

import com.massivecraft.factions.cmd.type.TypeMPlayer;

public class CmdFactionsPowerBoostPlayer extends CmdFactionsPowerBoostAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPowerBoostPlayer()
	{
		super(TypeMPlayer.get(), "player");
	}
	
}
