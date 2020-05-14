package com.massivecraft.factions.cmd.sand;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
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
        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.SANDALT_KILLALL));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        for (Faction faction : FactionColl.get().getAll(faction -> ! faction.getSandAlts().isEmpty()))
        {
            faction.despawnAllSandAlts();
        }
        msg("%s <i>despawned <g>ALL <i>sand alts for <g>ALL <i>factions.", msender.describeTo(msender, true));
    }

}
