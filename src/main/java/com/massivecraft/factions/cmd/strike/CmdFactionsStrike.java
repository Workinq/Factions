package com.massivecraft.factions.cmd.strike;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdFactionsStrike extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsStrikeAdd cmdFactionsStrikeAdd = new CmdFactionsStrikeAdd();
    public CmdFactionsStrikeRemove cmdFactionsStrikeRemove = new CmdFactionsStrikeRemove();
    public CmdFactionsStrikeList cmdFactionsStrikeList = new CmdFactionsStrikeList();

}
