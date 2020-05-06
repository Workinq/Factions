package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;

public class CmdFactionsRosterRemove extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterRemove()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");

        // Requirements
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        MPlayer mplayer = this.readArg();

        // Verify
        if ( ! MPerm.getPermRoster().has(msender, msenderFaction, true)) return;

        if ( ! msenderFaction.isInRoster(mplayer))
        {
            msg("%s <i>is not in the faction roster.", mplayer.describeTo(msender));
            return;
        }

        msenderFaction.removeFromRoster(mplayer);
        msg("%s <i>removed %s <i>from the faction roster.", msender.describeTo(msender, true), mplayer.describeTo(msender));
        msenderFaction.msg("%s <i>was removed to the faction roster.", mplayer.describeTo(msenderFaction, true));
    }

}
