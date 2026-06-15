package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsSandAlt extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsSandAltGui cmdFactionsSandAltGui = new CmdFactionsSandAltGui();
    public CmdFactionsSandAltKillAll cmdFactionsSandAltKillAll = new CmdFactionsSandAltKillAll();

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsSandAltGui.execute(sender, new ArrayList<>());
    }

}
