package com.massivecraft.factions.cmd.mute;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.FactionMute;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.util.IdUtil;

import java.util.Collection;

public class CmdFactionsMute extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsMute()
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
        if ( ! MPerm.getPermMute().has(msender, msenderFaction, true)) return;

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

            if (msenderFaction.isMuted(mplayer))
            {
                msender.msg("%s<i> is already muted.",mplayer.describeTo(msenderFaction));
                continue;
            }

            msenderFaction.msg("%s<i> muted %s<i>", msender.describeTo(msenderFaction, true), mplayer.describeTo(msenderFaction));
            FactionMute factionMute = new FactionMute(mplayer.getId(),msender.getId(),System.currentTimeMillis());
            msenderFaction.mute(factionMute);

            if (MConf.get().logFactionMute)
            {
                Factions.get().log(msender.getDisplayName(IdUtil.getConsole()) + " muted " + mplayer.getName() + " in the faction " + msenderFaction.getName());
            }
        }
    }

}
