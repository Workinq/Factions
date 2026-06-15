package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsChunkalt extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsChunkaltGui cmdFactionsChunkAltGui = new CmdFactionsChunkaltGui();
    public CmdFactionsChunkaltKillall cmdFactionsChunkAltKillAll = new CmdFactionsChunkaltKillall();

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsChunkAltGui.execute(sender, new ArrayList<>());
    }

}
