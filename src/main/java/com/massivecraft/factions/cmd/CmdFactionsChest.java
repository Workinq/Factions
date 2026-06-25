package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsChest extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsChestOpen cmdFactionsChestOpen = new CmdFactionsChestOpen();
    public CmdFactionsChestLog cmdFactionsChestLog = new CmdFactionsChestLog();
    public CmdFactionsChestSee cmdFactionsChestSee = new CmdFactionsChestSee();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsChest()
    {
        // Aliases
        this.addAliases("pv", "vault");

        // Parameters
        this.setOverflowSensitive(false);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsChestOpen.execute(sender, this.getArgs());
    }

}
