package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.engine.EngineScoreboard;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdFactionsFocus extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsFocus()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
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

        // MPerm
        if ( ! MPerm.getPermFocus().has(msender, msenderFaction, true) ) return;

        // Verify
        if (mplayer.getFaction() == msenderFaction)
        {
            throw new MassiveException().setMsg("<b>You cannot focus players on your faction.");
        }

        // Focus / Un-focus
        if (msenderFaction.isPlayerFocused(mplayer.getUuid()))
        {
            msenderFaction.setFocusedPlayer(null);
            msenderFaction.msg("%s <i>has been unfocused by %s<i>.", mplayer.describeTo(msenderFaction, true), msender.describeTo(msenderFaction));
        }
        else
        {
            msenderFaction.setFocusedPlayer(mplayer.getUuid());
            msenderFaction.msg("%s <i>has been focused by %s<i>.", mplayer.describeTo(msenderFaction, true), msender.describeTo(msenderFaction));
        }

        // Update scoreboard
        for (Player player : msenderFaction.getOnlinePlayers())
        {
            EngineScoreboard.get().resendTab(player);
        }
    }

}
