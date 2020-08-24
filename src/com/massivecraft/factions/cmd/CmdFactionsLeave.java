package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;

public class CmdFactionsLeave extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsLeave()
	{
		// Aliases
		this.setAliases("leave");

		// Desc
		this.setDescPermission("factions.leave");

		// Requirements
		this.addRequirements(ReqHasFaction.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override public void perform() { msender.leave(); }

}
