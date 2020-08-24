package com.massivecraft.factions.cmd.rel;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsRelation extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsRelationSet cmdFactionsRelationSet = new CmdFactionsRelationSet();
	public CmdFactionsRelationList cmdFactionsRelationList = new CmdFactionsRelationList();
	public CmdFactionsRelationWishes cmdFactionsRelationWishes = new CmdFactionsRelationWishes();
	public CmdFactionsRelationCheck cmdFactionsRelationCheck = new CmdFactionsRelationCheck();

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRelation()
	{
		// Aliases
		this.setAliases("relation");

		// Desc
		this.setDescPermission("factions.relation");
	}

}
