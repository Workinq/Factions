package com.massivecraft.factions.cmd;

import com.massivecraft.factions.ClaimType;
import com.massivecraft.factions.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.ArrayList;

public class CmdFactionsRaidunclaim extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsSetOne cmdFactionsRaidunclaimOne = new CmdFactionsSetOne(false);
	public CmdFactionsSetAt cmdFactionsRaidunclaimAt = new CmdFactionsSetAt(false);
	public CmdFactionsSetFill cmdFactionsRaidunclaimFill = new CmdFactionsSetFill(false);
	public CmdFactionsSetSquare cmdFactionsRaidunclaimSquare = new CmdFactionsSetSquare(false);
	public CmdFactionsSetCircle cmdFactionsRaidunclaimCircle = new CmdFactionsSetCircle(false);
	public CmdFactionsSetLine cmdFactionsRaidunclaimLine = new CmdFactionsSetLine(false);

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRaidunclaim()
	{
		CmdFactionsSetX[] variants = {
			this.cmdFactionsRaidunclaimOne,
			this.cmdFactionsRaidunclaimAt,
			this.cmdFactionsRaidunclaimFill,
			this.cmdFactionsRaidunclaimSquare,
			this.cmdFactionsRaidunclaimCircle,
			this.cmdFactionsRaidunclaimLine,
		};
		for (CmdFactionsSetX variant : variants)
		{
			variant.setClaimType(ClaimType.RAID);
			variant.addRequirements(RequirementHasPerm.get(Perm.RAIDUNCLAIM));
		}
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		this.cmdFactionsRaidunclaimOne.execute(sender, new ArrayList<>());
	}

}
