package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.cmd.type.TypeFactionNameStrict;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.store.MStore;
import org.bukkit.ChatColor;

public class CmdFactionsCreate extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsCreate()
	{
		// Aliases
		this.addAliases("new");
		
		// Parameters
		this.addParameter(TypeFactionNameStrict.get(), "name");
		
		// Requirements
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasntFaction.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		String newName = this.readArg();
		
		// Pre-Generate Id
		String factionId = MStore.createId();
		
		// Event
		EventFactionsCreate createEvent = new EventFactionsCreate(sender, factionId, newName);
		createEvent.run();
		if (createEvent.isCancelled()) return;
		
		// Apply
		Faction faction = FactionColl.get().create(factionId);
		faction.setName(newName);

		// Roster
		faction.addToRoster(msender);
		faction.setRosterRank(msender, Rel.LEADER);

		// Walls
		faction.setLastCheckedMillis(System.currentTimeMillis());

		// Roles
		msender.setRole(Rel.LEADER);
		msender.setFaction(faction);
		
		EventFactionsMembershipChange joinEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.CREATE);
		joinEvent.run();
		// NOTE: join event cannot be cancelled or you'll have an empty faction
		
		// Inform
		msg("<i>You created the faction %s", faction.getName(msender));
		message(Mson.mson(mson("You should now: ").color(ChatColor.YELLOW), CmdFactions.get().cmdFactionsDescription.getTemplate()));
		
		// Log
		if (MConf.get().logFactionCreate)
		{
			Factions.get().log(msender.getName() + " created a new faction: " + newName);
		}
	}
	
}
