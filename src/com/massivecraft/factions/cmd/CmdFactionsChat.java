package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeChat;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;

public class CmdFactionsChat extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsChat()
    {
        // Aliases
        this.addAliases("c");

        // Parameters
        this.addParameter(new TypeEnum<>(Chat.class), "mode", "next");

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
        Chat current = msender.getChat();
        Chat chat = this.readArg(current.getNext());

        if (current == chat)
        {
            throw new MassiveException().addMsg("<b>Your chat mode is already set to <h>%s<b>.", current.getName());
        }

        // Apply
        msender.setChat(chat);

        // Inform
        msg("%s<i> set your chat mode from <h>%s<i> to <h>%s<i>.", msender.describeTo(msender, true), current.getName(), chat.getName());
    }

}
