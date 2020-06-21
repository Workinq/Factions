package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.factions.entity.object.FactionStrike;
import com.massivecraft.factions.event.EventFactionsShieldToggle;
import com.massivecraft.factions.event.EventFactionsStrikeAdd;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import org.bukkit.ChatColor;

public class CmdFactionsShieldToggle extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsShieldToggle()
	{
		this.addParameter(!MOption.get().isShields(), TypeBooleanYes.get(), "yes/no");

		this.addRequirements(RequirementHasPerm.get(Perm.SHIELD_TOGGLE));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Read arguments
		boolean enable = this.readArg(!MOption.get().isShields());

		// Event
		EventFactionsShieldToggle event = new EventFactionsShieldToggle(sender);
		event.run();
		if (event.isCancelled()) return;

		// Toggle shields option
		MOption.get().setShields(enable);

		// Inform command sender
		msender.getSender().sendMessage(ChatColor.YELLOW + "You have " + (enable ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED") + ChatColor.YELLOW + " shields for all factions.");
	}

}
