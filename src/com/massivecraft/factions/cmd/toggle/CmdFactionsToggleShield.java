package com.massivecraft.factions.cmd.toggle;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.factions.event.EventFactionsToggleShield;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
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
		// Aliases
		this.addAliases("shield");

		// Desc
		this.setDescPermission("factions.toggle.shield");

		// Parameters
		this.addParameter(!MOption.get().isShields(), TypeBooleanYes.get(), "yes/no");

		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.TOGGLE_SHIELD));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Args
		boolean target = this.readArg( ! MOption.get().isShields() );

		// Event
		EventFactionsToggleShield event = new EventFactionsToggleShield(sender, target);
		event.run();
		if (event.isCancelled()) return;
		target = event.isActive();

		// Apply
		MOption.get().setShields(target);

		// Inform
		String desc = Txt.parse(MOption.get().isShields() ? "<g>ENABLED" : "<b>DISABLED");

		String messageYou = Txt.parse("<i>%s %s <i>faction shields.", msender.describeTo(msender, true), desc);
		String messageLog = Txt.parse("<i>%s %s <i>faction shields.", msender.getDisplayName(IdUtil.getConsole()), desc);

		msender.message(messageYou);
		Factions.get().log(messageLog);
	}

}
