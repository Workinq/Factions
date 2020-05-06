package com.massivecraft.factions.cmd.credit;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsCredits extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsCreditsBalance cmdMoneyBalance = new CmdFactionsCreditsBalance();
	public CmdFactionsCreditsTransferFf cmdMoneyTransferFf = new CmdFactionsCreditsTransferFf();
	public CmdFactionsCreditsAdd cmdFactionsCreditsAdd = new CmdFactionsCreditsAdd();
	public CmdFactionsCreditsTake cmdFactionsCreditsTake = new CmdFactionsCreditsTake();
	public CmdFactionsCreditsSet cmdFactionsCreditsSet = new CmdFactionsCreditsSet();
	
}
