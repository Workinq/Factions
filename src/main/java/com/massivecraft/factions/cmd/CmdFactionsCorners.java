package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.World;


public class CmdFactionsCorners extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	// The vanilla world border size of an unconfigured world.
	private static final double DEFAULT_BORDER_SIZE = 59_999_968.0D;

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsCorners()
	{
		// Aliases
		this.addAliases("corners");

		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.CORNERS));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		boolean found = false;

		for (World world : Bukkit.getWorlds())
		{
			if ( ! MConf.get().worldsClaimingEnabled.contains(world.getName())) continue;
			if (world.getWorldBorder().getSize() >= DEFAULT_BORDER_SIZE) continue;

			found = true;

			int[] bounds = CmdFactionsSetCorner.getCornerChunkBounds(world);
			int minX = bounds[0];
			int maxX = bounds[1];
			int minZ = bounds[2];
			int maxZ = bounds[3];

			message(Txt.titleize(world.getName()));
			msg("<k>NW: <reset>%s", describeCorner(world, minX, minZ));
			msg("<k>NE: <reset>%s", describeCorner(world, maxX, minZ));
			msg("<k>SW: <reset>%s", describeCorner(world, minX, maxZ));
			msg("<k>SE: <reset>%s", describeCorner(world, maxX, maxZ));
		}

		if ( ! found)
		{
			msg("<i>No claim-enabled worlds with a configured border were found.");
		}
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	private String describeCorner(World world, int chunkX, int chunkZ)
	{
		PS chunk = PS.valueOf(world.getName(), chunkX, chunkZ);
		Faction faction = BoardColl.get().getFactionAt(chunk);
		return faction.describeTo(msender, true);
	}

}
