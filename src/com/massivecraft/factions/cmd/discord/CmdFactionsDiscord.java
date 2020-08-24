package com.massivecraft.factions.cmd.discord;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsDiscord extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsDiscordSet cmdFactionsDiscordSet = new CmdFactionsDiscordSet();
    public CmdFactionsDiscordCheck cmdFactionsDiscordCheck = new CmdFactionsDiscordCheck();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDiscord()
    {
        // Aliases
        this.setAliases("discord");

        // Desc
        this.setDescPermission("factions.discord");
    }

}
