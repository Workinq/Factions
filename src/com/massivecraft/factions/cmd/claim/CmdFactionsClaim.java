package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;
import java.util.Collections;

public class CmdFactionsClaim extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsSetOne cmdFactionsClaimOne = new CmdFactionsSetOne(true);
	public CmdFactionsSetAt cmdFactionsSetAt = new CmdFactionsSetAt(true);
	public CmdFactionsSetAuto cmdFactionsClaimAuto = new CmdFactionsSetAuto(true);
	public CmdFactionsSetFill cmdFactionsClaimFill = new CmdFactionsSetFill(true);
	public CmdFactionsSetSquare cmdFactionsClaimSquare = new CmdFactionsSetSquare(true);
	public CmdFactionsSetCircle cmdFactionsClaimCircle = new CmdFactionsSetCircle(true);
	public CmdFactionsSetLine cmdFactionsSetLine = new CmdFactionsSetLine(true);
	public CmdFactionsSetAll cmdFactionsClaimAll = new CmdFactionsSetAll(true);

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsClaim()
	{
		// Aliases
		this.setAliases("claim");

		// Desc
		this.setDescPermission("factions.claim");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		cmdFactionsClaimOne.execute(sender, new ArrayList<>());
	}

}
