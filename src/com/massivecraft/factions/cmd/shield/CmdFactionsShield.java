package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsShield extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsShieldSet cmdFactionsShieldSet = new CmdFactionsShieldSet();
    public CmdFactionsShieldView cmdFactionsShieldView = new CmdFactionsShieldView();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShield()
    {
        // Aliases
        this.addAliases("forcefield");
    }

}