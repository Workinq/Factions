package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.*;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsSetBaseRegion extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSetBaseRegion()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg(msenderFaction);

        if ( ! MOption.get().isGrace() && faction.hasBaseRegion() && ! msender.isOverriding() ) {
            msg("<b>You can't set your base region as grace has been disabled.");
            return;
        }

        if ( ! MPerm.getPermBaseregion().has(msender, faction, true) ) return;

        // Land check
        if (BoardColl.get().getFactionAt(PS.valueOf(me)) != faction)
        {
            msg("<b>You can only set your base region in your own faction territory.");
            return;
        }

        // Args
        MassiveSet<PS> chunks = this.getChunks();

        // Verify
        if (chunks.isEmpty())
        {
            msg("<b>You must have a 25x25 chunk claim to set your base region.");
            return;
        }

        // Apply
        faction.setBaseRegion(chunks);

        // Inform
        msg("<g>Successfully set your base region.");
    }

    public MassiveSet<PS> getChunks()
    {
        // Common Startup
        final PS chunk = PS.valueOf(me.getLocation()).getChunk(true);
        final MassiveSet<PS> chunks = new MassiveSet<>();

        chunks.add(chunk); // The center should come first for pretty messages

        int radiusZero = 34; // 50x50 chunk radius

        for (int dx = -radiusZero; dx <= radiusZero; dx++)
        {
            for (int dz = -radiusZero; dz <= radiusZero; dz++)
            {
                int x = chunk.getChunkX() + dx;
                int z = chunk.getChunkZ() + dz;

                chunks.add(chunk.withChunkX(x).withChunkZ(z));
            }
        }

        return chunks;
    }

}
