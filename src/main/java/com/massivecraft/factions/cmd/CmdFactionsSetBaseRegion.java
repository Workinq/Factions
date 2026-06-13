package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
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

        // Set the core chunk and compute the base region from the surrounding claimed land.
        faction.setCoreChunk(PS.valueOf(me.getLocation()).getChunk(true));
        faction.recalculateBaseRegion();

        // Verify
        if ( ! faction.hasBaseRegion())
        {
            faction.setCoreChunk(null);
            msg("<b>You must be standing in your faction's claimed territory to set your base region.");
            return;
        }

        // Inform
        msg("<g>Successfully set your base region.");
    }

}
