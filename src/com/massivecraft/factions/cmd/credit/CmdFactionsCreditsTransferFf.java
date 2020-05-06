package com.massivecraft.factions.cmd.credit;

import com.massivecraft.factions.EconomyParticipator;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqBankCommandsEnabled;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

public class CmdFactionsCreditsTransferFf extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsCreditsTransferFf()
	{
		// Fields
		this.setSetupEnabled(false);

		// Aliases
		this.addAliases("ff");

		// Parameters
		this.addParameter(TypeInteger.get(), "amount");
		this.addParameter(TypeFaction.get(), "faction");
		this.addParameter(TypeFaction.get(), "faction");

		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.CREDITS_F2F));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		int amount = this.readArg();
		Faction from = this.readArg();
		Faction to = this.readArg();

		if ( ! MPerm.getPermCredits().has(msender, from, true)) return;

		if (to.isSystemFaction())
		{
			msg("<b>You can't send credits to a system faction.");
			return;
		}

		// Verify
		if (amount < 0) amount *= -1;

		// Check if faction has enough credits
		if (from.getCredits() < amount)
		{
			msg("%s <i>doesn't have <a>%s <i>credits.", from.describeTo(msender), String.format("%,d", amount));
			return;
		}

		// Transfer credits
		from.takeCredits(amount);
		to.addCredits(amount);

		from.msg("<a>%s<i> credits were transferred from <h>%s<i> to <h>%s<i>.", String.format("%,d", amount), from.describeTo(from), to.describeTo(from));
		to.msg("<a>%s<i> credits were received from <h>%s<i>.", String.format("%,d", amount), from.describeTo(to));

		if (MConf.get().logCreditsTransactions)
		{
			Factions.get().log(ChatColor.stripColor(Txt.parse("%s transferred %s credits from the faction \"%s\" to the faction \"%s\"", msender.getName(), String.format("%,d", amount), from.describeTo(null), to.describeTo(null))));
		}
	}
	
}
