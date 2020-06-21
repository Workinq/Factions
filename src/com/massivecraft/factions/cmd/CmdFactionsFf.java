package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.container.TypeList;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

import java.util.List;

public class CmdFactionsFf extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsFf()
    {
        // Parameters
        this.addParameter(TypeList.get(TypeString.get()), "message", true);

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
        List<String> messages = this.readArg();
        StringBuilder builder = new StringBuilder();

        // Message building
        for (String string : messages)
        {
            builder.append(string).append(" ");
        }

        // More Args
        String msg = builder.toString().trim();
        String message = String.format(MConf.get().chatFormat, msender.describeTo(msenderFaction), msg);

        // Loop - All Players
        for (MPlayer mplayer : MPlayerColl.get().getAllOnline())
        {
            // Verify - Spying
            if (mplayer.isSpying())
            {
                mplayer.msg(MConf.get().spyChatFormat, msender.describeTo(mplayer, true), Chat.FACTION.getName(), msg);
            }

            // Verify - Faction
            if (mplayer.getFaction() != msenderFaction) continue;

            // Sad times :(
            if (mplayer.isIgnoring(msender)) continue;

            // Inform
            mplayer.msg(message);
        }

        // Log
        Factions.get().log(" [Faction Chat] " + me.getName() + " sent a message: " + msg);
    }

}
