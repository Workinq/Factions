package com.massivecraft.factions.cmd.perm;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsPerm extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsPermList cmdFactionsPermList = new CmdFactionsPermList();
	public CmdFactionsPermShow cmdFactionsPermShow = new CmdFactionsPermShow();
	public CmdFactionsPermSet cmdFactionsPermSet = new CmdFactionsPermSet();
	public CmdFactionsPermGui cmdFactionsPermGui = new CmdFactionsPermGui();

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsPerm()
	{
		// Aliases
		this.setAliases("perm");

		// Desc
		this.setDescPermission("factions.perm");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		this.cmdFactionsPermGui.execute(sender, new ArrayList<>());
	}

}
