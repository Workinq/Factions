package com.massivecraft.factions.cmd.access;

import com.massivecraft.factions.Acc;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.TerritoryAccess;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;

import java.util.Collections;


public class CmdFactionsAccessClear extends CmdFactionsAccessAbstract
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsAccessClear()
    {
        this.addParameter(Acc.ALL, new TypeEnum<>(Acc.class), "access");
        this.addRequirements(RequirementHasPerm.get(Perm.ACCESS_CLEAR));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void innerPerform() throws MassiveException
    {
        // MPerm
        if ( ! MPerm.getPermAccess().has(msender, hostFaction, true)) return;

        Acc arg = this.readArg();

        switch(arg)
        {
            case FACTIONS:

                if ( ! Perm.ACCESS_FACTION.has(sender, true)) return;

                // Clear factions' access to chunk
                ta = TerritoryAccess.valueOf(hostFaction.getId(), false, Collections.<String>emptySet(), ta.getPlayerIds());

                break;

            case PLAYERS:

                if ( ! Perm.ACCESS_PLAYER.has(sender, true)) return;

                // Clear players' access to chunk
                ta = ta.withPlayerIds(Collections.<String>emptySet());

                break;

            case ALL:

                if ( ! Perm.ACCESS_PLAYER.has(sender, true) || ! Perm.ACCESS_FACTION.has(sender, true)) return;

                // Clear players' and factions' access to chunk
                ta = TerritoryAccess.valueOf(hostFaction.getId(), false, Collections.<String>emptySet(), Collections.<String>emptySet());

                break;
        }

        //TODO: Must send two commands to get change in db
        // Send the new Territory Access to change in db
        BoardColl.get().setTerritoryAccessAt(chunk, ta);

        //Inform
        this.sendAccessInfo();
    }

}
