package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsRoster extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsRosterAdd cmdFactionsRosterAdd = new CmdFactionsRosterAdd();
    public CmdFactionsRosterSetrank cmdFactionsRosterSetrank = new CmdFactionsRosterSetrank();
    public CmdFactionsRosterList cmdFactionsRosterList = new CmdFactionsRosterList();

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsRosterList.execute(sender, new ArrayList<>());
    }

}
