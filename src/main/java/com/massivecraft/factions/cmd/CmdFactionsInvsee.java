package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.gui.InvseeMenu;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsInvsee extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsInvsee()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        MPlayer mplayer = this.readArg();

        if ( ! MPerm.getPermInvsee().has(msender, msenderFaction, true) ) return;

        if (mplayer == msender)
        {
            throw new MassiveException().setMsg("<b>You cannot perform /f invsee on yourself.");
        }

        if (mplayer.getPlayer() == null || ! mplayer.isOnline())
        {
            throw new MassiveException().setMsg("<b>You can only use /f invsee on online players.");
        }

        if (mplayer.getFaction() != msenderFaction &&  ! msender.isOverriding())
        {
            throw new MassiveException().setMsg("<b>You can only use /f invsee on players in your faction.");
        }

        // Apply
        new InvseeMenu(me, mplayer.getPlayer()).open();

        // Inform
        msg("<i>Viewing %s's <i>inventory.", mplayer.describeTo(msender));
    }

}
