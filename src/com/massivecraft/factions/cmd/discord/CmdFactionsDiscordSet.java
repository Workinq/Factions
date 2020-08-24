package com.massivecraft.factions.cmd.discord;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsDiscordSet extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDiscordSet()
    {
        // Aliases
        this.addAliases("set");

        // Desc
        this.setDescPermission("factions.discord.set");

        // Parameters
        this.addParameter(TypeString.get(), "discord");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        String discord = this.readArg();

        // MPerm
        if ( ! MPerm.getPermDiscord().has(msender, msenderFaction, true) ) return;

        // Apply
        msenderFaction.setDiscord(discord);

        // Inform
        msenderFaction.msg("%s <i>set the faction discord to <a>%s<i>.", msender.describeTo(msenderFaction, true), discord);
    }

}
