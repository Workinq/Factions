package com.massivecraft.factions.cmd.sand;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdFactionsSandAltKillAll extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSandAltKillAll()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "all");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction fac = this.readArg(null);

        // Despawn all sand alts for a certain faction
        if (fac != null)
        {
            fac.despawnAllSandAlts();

            // Inform
            msg("%s <i>despawned <g>ALL <i>sand alts for %s<i>.", msender.describeTo(msender, true), fac.describeTo(msender));
            return;
        }

        // Despawn all sand alts for all factions
        for (Faction faction : FactionColl.get().getAll(faction -> ! faction.getSandAlts().isEmpty()))
        {
            faction.despawnAllSandAlts();
        }

        // Inform
        msg("%s <i>despawned <g>ALL <i>sand alts for <g>ALL <i>factions.", msender.describeTo(msender, true));
    }

}
