package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.AuditAction;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.factions.util.AuditUtil;
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
        // Parameters
        this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);

        // Requirements
        this.addRequirements(ReqHasFaction.get());
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
            // Same person as the sender?
            if (mplayer == msender) continue;

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

            AuditUtil.log(AuditCategory.MUTE, AuditAction.MUTE_REMOVE, sender, msenderFaction, mplayer.getId());

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
