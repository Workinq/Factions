package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.cmd.type.TypeChat;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

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
        this.addParameter(TypeChat.get(), "mode", "next");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
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

        // Apply
        msender.setChat(chat);

        // Inform
        msg("%s<i> set your chat mode from <h>%s<i> to <h>%s<i>.", msender.describeTo(msender, true), current.getName(), chat.getName());
    }

}
