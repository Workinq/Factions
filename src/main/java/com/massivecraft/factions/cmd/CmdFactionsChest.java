package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsChest extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsChestOpen cmdFactionsChestOpen = new CmdFactionsChestOpen();
    public CmdFactionsChestLog cmdFactionsChestLog = new CmdFactionsChestLog();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsChest()
    {
        // Aliases
        this.addAliases("pv");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsChestOpen.execute(sender, new ArrayList<>());
    }

}
