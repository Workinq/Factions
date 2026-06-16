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

        if (mplayer.getFaction() != msenderFaction) throw new MassiveException().setMsg("%s <b>is not in your faction.", mplayer.describeTo(msender, true));

        if (msender.isIgnoring(mplayer)) throw new MassiveException().setMsg("<b>You are already ignoring %s<b>.", mplayer.describeTo(msender));

        // Apply
        msender.ignore(mplayer);

        // Inform
        msg("<i>You are now ignoring %s<i>.", mplayer.describeTo(msender));
    }

}
