package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import net.minecraft.server.v1_8_R3.MobSpawnerAbstract;
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

		// Toggle shields option
		MOption.get().setShields(enable);

		// Inform command sender
		msender.getSender().sendMessage(ChatColor.YELLOW + "You have " + (enable ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED") + ChatColor.YELLOW + " shields for all factions.");
	}

}
