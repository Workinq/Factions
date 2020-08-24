package com.massivecraft.factions.cmd.access;

public class CmdFactionsAccessView extends CmdFactionsAccessAbstract
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	public CmdFactionsAccessView()
	{
		// Aliases
		this.addAliases("view");

		// Desc
		this.setDescPermission("factions.access.view");
	}

	@Override
	public void innerPerform()
	{
		this.sendAccessInfo();
	}
	
}
