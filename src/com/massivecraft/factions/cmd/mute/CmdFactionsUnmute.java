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

public class CmdFactionsUnmute extends FactionsCommand {
    public CmdFactionsUnmute() {
        // Parameters
        this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);
    }

    @Override
    public void perform() throws MassiveException {
        if ( ! MPerm.getPermMute().has(msender, msenderFaction, true)) return;
        // Args
        Collection<MPlayer> mplayers = this.readArg();
        for (MPlayer mplayer : mplayers) {
            if (mplayer.getFaction() != msenderFaction) {
                msender.msg("%s<i> is not apart of the faction.",mplayer.describeTo(msenderFaction));
                continue;
            }
            if(!msenderFaction.isMuted(mplayer)) {
                msender.msg("%s<i> is already unmuted.",mplayer.describeTo(msenderFaction));
                continue;
            }
            msenderFaction.msg("%s<i> unmuted %s<i>", msender.describeTo(msenderFaction, true), mplayer.describeTo(msenderFaction));
            msenderFaction.unmute(mplayer);

            if (MConf.get().logFactionMute) {
                Factions.get().log(msender.getDisplayName(IdUtil.getConsole()) + " unmuted " + mplayer.getName() + " from the faction " + msenderFaction.getName());
            }
        }
    }
}
