package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.toggle.CmdFactionsToggleShield;
import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsShield extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsShieldSet cmdFactionsShieldSet = new CmdFactionsShieldSet();
    public CmdFactionsShieldView cmdFactionsShieldView = new CmdFactionsShieldView();
    public CmdFactionsShieldClear cmdFactionsShieldClear = new CmdFactionsShieldClear();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShield()
    {
        // Aliases
        this.setAliases("shield", "forcefield");

        // Desc
        this.setDescPermission("factions.shield");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsShieldSet.execute(sender, new ArrayList<>());
    }

}
