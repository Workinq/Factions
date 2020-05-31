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

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsToggle()
    {
        // Child
        this.addChild(this.cmdFactionsToggleFly);
        this.addChild(this.cmdFactionsToggleGrace);

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.TOGGLE));
    }

}
