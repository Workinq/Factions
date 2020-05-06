package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsRoster extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsRosterAdd cmdFactionsRosterAdd = new CmdFactionsRosterAdd();
    public CmdFactionsRosterRemove cmdFactionsRosterRemove = new CmdFactionsRosterRemove();
    public CmdFactionsRosterSetrank cmdFactionsRosterSetrank = new CmdFactionsRosterSetrank();
    public CmdFactionsRosterList cmdFactionsRosterList = new CmdFactionsRosterList();

}
