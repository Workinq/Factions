package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsUnignore extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsUnignore()
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
        if ( ! Perm.UNIGNORE.has(sender, true)) return;

        if (!msender.isIgnoring(mplayer))
        {
            msg("<b>You aren't ignoring %s<b>.", mplayer.describeTo(msender));
            return;
        }

        // Apply
        msender.unignore(mplayer);

        // Inform
        msg("<i>You're no longer ignoring %s<i>.", mplayer.describeTo(msender));
    }
}
