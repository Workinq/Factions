package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsSandalt extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsSandaltGui cmdFactionsSandAltGui = new CmdFactionsSandaltGui();
    public CmdFactionsSandaltKillall cmdFactionsSandAltKillAll = new CmdFactionsSandaltKillall();

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsSandAltGui.execute(sender, new ArrayList<>());
    }

}
