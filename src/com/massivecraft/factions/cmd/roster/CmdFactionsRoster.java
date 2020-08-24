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
    public CmdFactionsRosterView cmdFactionsRosterView = new CmdFactionsRosterView();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRoster()
    {
        // Aliases
        this.setAliases("roster");

        // Desc
        this.setDescPermission("factions.roster");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsRosterView.execute(sender, new ArrayList<>());
    }

}
