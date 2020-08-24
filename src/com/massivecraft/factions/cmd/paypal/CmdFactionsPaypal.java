package com.massivecraft.factions.cmd.paypal;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsPaypal extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsPaypalSet cmdFactionsPaypalSet = new CmdFactionsPaypalSet();
    public CmdFactionsPaypalCheck cmdFactionsPaypalCheck = new CmdFactionsPaypalCheck();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsPaypal()
    {
        // Aliases
        this.setAliases("paypal");

        // Desc
        this.setDescPermission("factions.paypal");
    }

}
