package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsChunkaltKillall extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsChunkaltKillall()
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

        // Despawn all chunk alts for a certain faction
        if (fac != null)
        {
            fac.despawnAllChunkAlts();

            // Inform
            msg("%s <i>despawned <g>ALL <i>chunk alts for %s<i>.", msender.describeTo(msender, true), fac.describeTo(msender));
            return;
        }

        // Despawn all chunk alts for all factions
        for (Faction faction : FactionColl.get().getAll(faction -> ! faction.getChunkAlts().isEmpty()))
        {
            faction.despawnAllChunkAlts();
        }

        // Inform
        msg("%s <i>despawned <g>ALL <i>chunk alts for <g>ALL <i>factions.", msender.describeTo(msender, true));
    }

}
