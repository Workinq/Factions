package com.massivecraft.factions.cmd.alt;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdFactionsAlt extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsAltDeinvite cmdFactionsAltDeinvite = new CmdFactionsAltDeinvite();
    public CmdFactionsAltInvite cmdFactionsAltInvite = new CmdFactionsAltInvite();
    public CmdFactionsAltJoin cmdFactionsAltJoin = new CmdFactionsAltJoin();
    public CmdFactionsAltInviteList cmdFactionsAltInviteList = new CmdFactionsAltInviteList();

}
