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
        MPlayer mPlayer = this.readArg();

        if (! MPerm.getPermFocus().has(msender, msenderFaction, true)) return;

        if (msenderFaction.isPlayerFocused(mPlayer.getUuid().toString()))
        {
            msenderFaction.setFocusedPlayer(null);
            msenderFaction.msg("%s <i>has been unfocused by %s<i>.", mPlayer.describeTo(msenderFaction, true), msender.describeTo(msenderFaction));
        }
        else
        {
            msenderFaction.setFocusedPlayer(mPlayer.getUuid().toString());
            msenderFaction.msg("%s <i>has been focused by %s<i>.", mPlayer.describeTo(msenderFaction, true), msender.describeTo(msenderFaction));
        }
        for (Player player : msenderFaction.getOnlinePlayers())
        {
            EngineScoreboard.get().resendTab(player);
        }
    }

}