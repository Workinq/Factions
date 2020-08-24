package com.massivecraft.factions.cmd.mute;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.util.IdUtil;

import java.util.Collection;

public class CmdFactionsUnmute extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsUnmute()
    {
        // Aliases
        this.setAliases("unmute");

        // Desc
        this.setDescPermission("factions.unmute");

        // Parameters
        this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MPerm.getPermMute().has(msender, msenderFaction, true) ) return;

        // Args
        Collection<MPlayer> mplayers = this.readArg();
        for (MPlayer mplayer : mplayers)
        {
            if (mplayer == msender)
            {
                msg("<b>You cannot unmute yourself.");
                continue;
            }

            if (mplayer.getFaction() != msenderFaction)
            {
                msender.msg("%s<i> is not apart of the faction.",mplayer.describeTo(msenderFaction));
                continue;
            }

            if ( ! msenderFaction.isMuted(mplayer) )
            {
                msender.msg("%s<i> is already unmuted.",mplayer.describeTo(msenderFaction));
                continue;
            }

            // Apply
            msenderFaction.unmute(mplayer);

            // Inform
            msenderFaction.msg("%s<i> unmuted %s<i>", msender.describeTo(msenderFaction, true), mplayer.describeTo(msenderFaction));

            // Log
            if (MConf.get().logFactionMute)
            {
                Factions.get().log(msender.getDisplayName(IdUtil.getConsole()) + " unmuted " + mplayer.getName() + " from the faction " + msenderFaction.getName());
            }
        }
    }
}
