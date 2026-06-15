package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsChunkAlt extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsChunkAltGui cmdFactionsChunkAltGui = new CmdFactionsChunkAltGui();
    public CmdFactionsChunkAltKillAll cmdFactionsChunkAltKillAll = new CmdFactionsChunkAltKillAll();

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsChunkAltGui.execute(sender, new ArrayList<>());
    }

}
