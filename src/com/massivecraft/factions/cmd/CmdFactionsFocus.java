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
        // Aliases
        this.setAliases("focus");

        // Desc
        this.setDescPermission("factions.focus");

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
        MPlayer mplayer = this.readArg();

        if ( ! MPerm.getPermFocus().has(msender, msenderFaction, true) ) return;

        if (msenderFaction.isPlayerFocused(mplayer.getUuid().toString()))
        {
            msenderFaction.setFocusedPlayer(null);
            msenderFaction.msg("%s <i>has been unfocused by %s<i>.", mplayer.describeTo(msenderFaction, true), msender.describeTo(msenderFaction));
        }
        else
        {
            msenderFaction.setFocusedPlayer(mplayer.getUuid().toString());
            msenderFaction.msg("%s <i>has been focused by %s<i>.", mplayer.describeTo(msenderFaction, true), msender.describeTo(msenderFaction));
        }
        for (Player player : msenderFaction.getOnlinePlayers())
        {
            EngineScoreboard.get().resendTab(player);
        }
    }

}
