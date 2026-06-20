package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.Set;


public class CmdFactionsSetCorner extends CmdFactionsSetXSimple
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsSetCorner(boolean claim)
	{
		// Super
		super(claim);

		// Aliases
		this.addAliases("corner");

		// Format
		this.setFormatOne("<h>%s<i> %s <h>%d <i>chunk %s<i> using corner.");
		this.setFormatMany("<h>%s<i> %s <h>%d <i>chunks near %s<i> using corner.");

		// Requirements
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementHasPerm.get(Perm.CLAIM_CORNER));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Set<PS> getChunks() throws MassiveException
	{
		PS playerChunk = PS.valueOf(me.getLocation()).getChunk(true);

		int[] bounds = getCornerChunkBounds(me.getWorld());
		int minChunkX = bounds[0];
		int maxChunkX = bounds[1];
		int minChunkZ = bounds[2];
		int maxChunkZ = bounds[3];

		int px = playerChunk.getChunkX();
		int pz = playerChunk.getChunkZ();

		boolean onX = px == minChunkX || px == maxChunkX;
		boolean onZ = pz == minChunkZ || pz == maxChunkZ;
		if ( ! onX || ! onZ)
		{
			throw new MassiveException().setMsg("<b>You must be standing in a corner to perform a corner claim.");
		}

		// Step inward toward the border center
		int stepX = px == minChunkX ? 1 : -1;
		int stepZ = pz == minChunkZ ? 1 : -1;

		int size = MConf.get().claimCornerSize;
		Faction newFaction = this.getNewFaction();
		Faction none = FactionColl.get().getNone();

		Set<PS> chunks = new MassiveSet<>();
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				PS chunk = playerChunk.withChunkX(px + i * stepX).withChunkZ(pz + j * stepZ);
				Faction here = BoardColl.get().getFactionAt(chunk);
				if (here != newFaction && here != none) continue;
				chunks.add(chunk);
			}
		}

		if (chunks.isEmpty())
		{
			throw new MassiveException().setMsg("<b>There is no unclaimed land to claim in this corner.");
		}

		return chunks;
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	public static int[] getCornerChunkBounds(World world)
	{
		WorldBorder border = world.getWorldBorder();
		Location center = border.getCenter();
		double half = border.getSize() / 2.0D;

		int minChunkX = ((int) Math.floor(center.getX() - half)) >> 4;
		int maxChunkX = ((int) Math.ceil(center.getX() + half) - 1) >> 4;
		int minChunkZ = ((int) Math.floor(center.getZ() - half)) >> 4;
		int maxChunkZ = ((int) Math.ceil(center.getZ() + half) - 1) >> 4;

		return new int[]{minChunkX, maxChunkX, minChunkZ, maxChunkZ};
	}

}
