package com.massivecraft.factions.cmd.vault;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsVault extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsVaultOpen cmdFactionsVaultOpen = new CmdFactionsVaultOpen();
    public CmdFactionsVaultLog cmdFactionsVaultLog = new CmdFactionsVaultLog();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsVault()
    {
        // Aliases
        this.addAliases("chest", "pv");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsVaultOpen.execute(sender, new ArrayList<>());
    }

}
