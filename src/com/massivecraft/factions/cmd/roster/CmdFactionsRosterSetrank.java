package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.cmd.type.TypeRel;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsRosterSetrank extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterSetrank()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
        this.addParameter(TypeRel.get(), "role");

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
        Rel rel = this.readArg();

        // Verify
        if ( ! MPerm.getPermRoster().has(msender, msenderFaction, true)) return;

        if ( ! msenderFaction.isInRoster(mplayer))
        {
            msg("%s <i>is not in the faction roster.", mplayer.describeTo(msender));
            return;
        }

        if (rel.isLessThan(Rel.RECRUIT) || rel.getValue() >= msender.getRole().getValue())
        {
            msg("<b>You can't set %s's <b>rank to %s.", mplayer.describeTo(msender), rel.getName());
            return;
        }

        msenderFaction.setRosterRank(mplayer, rel);
        msg("%s <i>changed %s's <i>roster rank to <h>%s<i>.", msender.describeTo(msender, true), mplayer.describeTo(msender), rel.getName());
    }

}