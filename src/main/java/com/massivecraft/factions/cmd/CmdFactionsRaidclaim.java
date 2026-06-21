package com.massivecraft.factions.cmd;

import com.massivecraft.factions.ClaimType;
import com.massivecraft.factions.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.ArrayList;

public class CmdFactionsRaidclaim extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsSetOne cmdFactionsRaidclaimOne = new CmdFactionsSetOne(true);
	public CmdFactionsSetAt cmdFactionsRaidclaimAt = new CmdFactionsSetAt(true);
	public CmdFactionsSetFill cmdFactionsRaidclaimFill = new CmdFactionsSetFill(true);
	public CmdFactionsSetSquare cmdFactionsRaidclaimSquare = new CmdFactionsSetSquare(true);
	public CmdFactionsSetCircle cmdFactionsRaidclaimCircle = new CmdFactionsSetCircle(true);
	public CmdFactionsSetCorner cmdFactionsRaidclaimCorner = new CmdFactionsSetCorner(true);
	public CmdFactionsSetLine cmdFactionsRaidclaimLine = new CmdFactionsSetLine(true);
	public CmdFactionsRaidclaimRemove cmdFactionsRaidclaimRemove = new CmdFactionsRaidclaimRemove();

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRaidclaim()
	{
		CmdFactionsSetX[] variants = {
			this.cmdFactionsRaidclaimOne,
			this.cmdFactionsRaidclaimAt,
			this.cmdFactionsRaidclaimFill,
			this.cmdFactionsRaidclaimSquare,
			this.cmdFactionsRaidclaimCircle,
			this.cmdFactionsRaidclaimCorner,
			this.cmdFactionsRaidclaimLine,
		};
		for (CmdFactionsSetX variant : variants)
		{
			variant.setClaimType(ClaimType.RAID);
			variant.addRequirements(RequirementHasPerm.get(Perm.RAIDCLAIM));
		}
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		this.cmdFactionsRaidclaimOne.execute(sender, new ArrayList<>());
	}

}
