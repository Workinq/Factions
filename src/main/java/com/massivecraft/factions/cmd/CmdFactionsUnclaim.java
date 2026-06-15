package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsUnclaim extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsSetOne cmdFactionsUnclaimOne = new CmdFactionsSetOne(false);
	public CmdFactionsSetAt cmdFactionsSetAt = new CmdFactionsSetAt(false);
	public CmdFactionsSetAuto cmdFactionsUnclaimAuto = new CmdFactionsSetAuto(false);
	public CmdFactionsSetFill cmdFactionsUnclaimFill = new CmdFactionsSetFill(false);
	public CmdFactionsSetSquare cmdFactionsUnclaimSquare = new CmdFactionsSetSquare(false);
	public CmdFactionsSetCircle cmdFactionsUnclaimCircle = new CmdFactionsSetCircle(false);
	public CmdFactionsSetLine cmdFactionsSetLine = new CmdFactionsSetLine(false);
	public CmdFactionsSetAll cmdFactionsUnclaimAll = new CmdFactionsSetAll(false);

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		cmdFactionsUnclaimOne.execute(sender, new ArrayList<>());
	}

}
