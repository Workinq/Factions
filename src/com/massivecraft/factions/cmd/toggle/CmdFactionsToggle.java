package com.massivecraft.factions.cmd.toggle;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdFactionsToggle extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsToggleFly cmdFactionsToggleFly = new CmdFactionsToggleFly();
    public CmdFactionsToggleGrace cmdFactionsToggleGrace = new CmdFactionsToggleGrace();
    public CmdFactionsToggleShield cmdFactionsToggleShield = new CmdFactionsToggleShield();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsToggle()
    {
        // Aliases
        this.setAliases("toggle");

        // Desc
        this.setDescPermission("factions.toggle");

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.TOGGLE));
    }

}
