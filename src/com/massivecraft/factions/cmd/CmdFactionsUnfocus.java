package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.req.ReqHasFocusedPlayer;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.engine.EngineScoreboard;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdFactionsUnfocus extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsUnfocus()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player", "focused");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
        this.addRequirements(ReqHasFaction.get());
        this.addRequirements(ReqHasFocusedPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        MPlayer mplayer = this.readArg(MPlayer.get(msenderFaction.getFocusedPlayer()));

        if ( ! MPerm.getPermFocus().has(this.msender, this.msenderFaction, true) ) return;

        if (msenderFaction.isPlayerFocused(mplayer.getUuid()))
        {
            msenderFaction.setFocusedPlayer(null);
            msenderFaction.msg("%s <i>has been unfocused by %s<i>.", mplayer.describeTo(msenderFaction, true), msender.describeTo(msenderFaction));
        }
        else
        {
            msg("%s <b>does not currently have a player focused.", this.msenderFaction.describeTo(mplayer, true));
        }
        for (Player player : msenderFaction.getOnlinePlayers())
        {
            EngineScoreboard.get().resendTab(player);
        }
    }

}
