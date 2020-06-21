package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsShield extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsShieldSet cmdFactionsShieldSet = new CmdFactionsShieldSet();
    public CmdFactionsShieldView cmdFactionsShieldView = new CmdFactionsShieldView();
    public CmdFactionsShieldToggle cmdFactionsShieldToggle = new CmdFactionsShieldToggle();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShield()
    {
        // Aliases
        this.addAliases("forcefield");
    }

}
