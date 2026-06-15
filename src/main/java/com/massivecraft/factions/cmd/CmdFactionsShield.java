package com.massivecraft.factions.cmd;

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
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsShieldSet.execute(sender, new ArrayList<>());
    }

}
