package com.massivecraft.factions.cmd.strike;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsStrike extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsStrikeAdd cmdFactionsStrikeAdd = new CmdFactionsStrikeAdd();
    public CmdFactionsStrikeRemove cmdFactionsStrikeRemove = new CmdFactionsStrikeRemove();
    public CmdFactionsStrikeList cmdFactionsStrikeList = new CmdFactionsStrikeList();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsStrike()
    {
        // Aliases
        this.setAliases("strike");

        // Desc
        this.setDescPermission("factions.strike");
    }

}
