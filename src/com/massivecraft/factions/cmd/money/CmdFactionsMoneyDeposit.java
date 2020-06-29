package com.massivecraft.factions.cmd.money;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqBankCommandsEnabled;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.object.FactionMoneyLog;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

public class CmdFactionsMoneyDeposit extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMoneyDeposit()
	{
		// Parameters
		this.addParameter(TypeDouble.get(), "amount");
		this.addParameter(TypeFaction.get(), "faction", "you");

		// Requirements
		this.addRequirements(ReqBankCommandsEnabled.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException {
		double amount = this.readArg();
		
		Faction faction = this.readArg(msenderFaction);
		
		boolean success = Econ.transferMoney(msender, msender, faction, amount);
		
		if (success && MConf.get().logMoneyTransactions) {
			Factions.get().log(ChatColor.stripColor(Txt.parse("%s deposited %s in the faction bank: %s", msender.getName(), Money.format(amount), faction.describeTo(null))));
		}
		if(success) {
			final FactionMoneyLog factionMoneyLog = new FactionMoneyLog(msender.getId(), "deposited", amount, System.currentTimeMillis());
			msenderFaction.logMoney(factionMoneyLog);
		}
	}
	
}
