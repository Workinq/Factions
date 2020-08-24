package com.massivecraft.factions.cmd.flag;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsFlag extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsFlagList cmdFactionsFlagList = new CmdFactionsFlagList();
	public CmdFactionsFlagShow cmdFactionsFlagShow = new CmdFactionsFlagShow();
	public CmdFactionsFlagSet cmdFactionsFlagSet = new CmdFactionsFlagSet();

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsFlag()
	{
		// Aliases
		this.setAliases("flag");

		// Desc
		this.setDescPermission("factions.flag");
	}

}
