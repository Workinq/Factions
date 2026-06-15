package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsToggleShield;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsToggleShield extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsToggleShield()
	{
		// Parameters
		this.addParameter(!MConf.get().shield, TypeBooleanYes.get(), "yes/no");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Args
		boolean target = this.readArg( ! MConf.get().shield );

		// Event
		EventFactionsToggleShield event = new EventFactionsToggleShield(sender, target);
		event.run();
		if (event.isCancelled()) return;
		target = event.isActive();

		// Apply
		MConf.get().shield = target; MConf.get().changed();

		// Inform
		String desc = Txt.parse(MConf.get().shield ? "<g>ENABLED" : "<b>DISABLED");

		String messageYou = Txt.parse("<i>%s %s <i>faction shields.", msender.describeTo(msender, true), desc);
		String messageLog = Txt.parse("<i>%s %s <i>faction shields.", msender.getDisplayName(IdUtil.getConsole()), desc);

		msender.message(messageYou);
		Factions.get().log(messageLog);
	}

}
