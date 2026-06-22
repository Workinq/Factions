package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsRoster extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsRosterAdd cmdFactionsRosterAdd = new CmdFactionsRosterAdd();
    public CmdFactionsRosterRemove cmdFactionsRosterRemove = new CmdFactionsRosterRemove();
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
