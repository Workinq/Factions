package com.massivecraft.factions.cmd.money;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqBankCommandsEnabled;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsMoneyBalance extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMoneyBalance()
	{
		// Aliases
		this.addAliases("balance");

		// Desc
		this.setDescPermission("factions.money.balance");

		// Parameters
		this.addParameter(TypeFaction.get(), "faction", "you");

		// Requirements
		this.addRequirements(ReqBankCommandsEnabled.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Faction faction = this.readArg(msenderFaction);

		if (faction != msenderFaction && ! Perm.MONEY_BALANCE_ANY.has(sender, true)) return;

		Econ.sendBalanceInfo(msender, faction);
	}
}
