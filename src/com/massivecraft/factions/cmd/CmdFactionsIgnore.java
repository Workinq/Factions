package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsIgnore extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsIgnore()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        MPlayer mplayer = this.readArg();

        // Perms
        if ( ! Perm.IGNORE.has(sender, true)) return;

        if (mplayer.getFaction() != msenderFaction)
        {
            msg("%s <b>is not in your faction.", mplayer.describeTo(msender, true));
            return;
        }

        if (msender.isIgnoring(mplayer))
        {
            msg("<b>You are already ignoring %s<b>.", mplayer.describeTo(msender));
            return;
        }

        // Apply
        msender.ignore(mplayer);

        // Inform
        msg("<i>You are now ignoring %s<i>.", mplayer.describeTo(msender));
    }

}
