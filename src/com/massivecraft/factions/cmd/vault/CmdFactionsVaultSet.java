package com.massivecraft.factions.cmd.vault;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasntVault;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.object.Vault;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsVaultSet extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsVaultSet()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
        this.addRequirements(ReqHasntVault.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);
        PS ps = PS.valueOf(me);
        Faction at = BoardColl.get().getFactionAt(ps);

        // MPerm
        if ( ! MPerm.getPermVault().has(msender, faction, true) ) return;

        // Verify - Faction
        if (at != faction)
        {
            msg("<b>You must be in your own faction territory to set your faction vault.");
            return;
        }

        // Verify - Base Region
        if ( ! faction.hasBaseRegion() )
        {
            msg("<b>You must have a base region set to set your faction vault.");
            return;
        }

        if ( ! faction.getBaseRegion().contains(ps) )
        {
            msg("<b>You can only set your faction vault in your base region.");
            return;
        }

        // Apply
        Vault vault = new Vault(ps, false);
        faction.setVault(vault);

        // Inform
        msg("%s <g>successfully set the faction vault for %s<g>.", msender.describeTo(msender, true), faction.describeTo(msender));
    }

}
