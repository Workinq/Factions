package com.massivecraft.factions.cmd.access;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsAccess extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsAccessView cmdFactionsAccessView = new CmdFactionsAccessView();
	public CmdFactionsAccessPlayer cmdFactionsAccessPlayer = new CmdFactionsAccessPlayer();
	public CmdFactionsAccessFaction cmdFactionsAccessFaction = new CmdFactionsAccessFaction();
	public CmdFactionsAccessClear cmdFactionsAccessClear = new CmdFactionsAccessClear();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsAccess()
	{
		// Requirements
		this.addRequirements(RequirementIsPlayer.get());
	}
	
}
