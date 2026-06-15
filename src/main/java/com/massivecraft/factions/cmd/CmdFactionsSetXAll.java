package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public abstract class CmdFactionsSetXAll extends CmdFactionsSetX
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSetXAll(boolean claim)
	{
		// Super
		super(claim);
		
		// Parameters
		this.addParameter(TypeString.get(), "all|map", "all");
		this.addParameter(TypeFaction.get(), "faction", "you");
		if (claim)
		{
			// Optional (required from console) so it can follow the optional all|map/faction args.
			// Defaults to the sender's own faction via getNewFaction().
			this.addParameter(TypeFaction.get(), true, "newfaction", "you");
			this.setFactionArgIndex(2);
		}
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public Faction getOldFaction() throws MassiveException
	{
		// Default to the sender's own faction when omitted.
		return this.readArgAt(1, msenderFaction);
	}
	
}
