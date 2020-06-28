package com.massivecraft.factions.cmd.vault;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsVault extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsVaultSet cmdFactionsVaultSet = new CmdFactionsVaultSet();
    public CmdFactionsVaultOpen cmdFactionsVaultOpen = new CmdFactionsVaultOpen();

    @Override
    public void perform() throws MassiveException {
        cmdFactionsVaultOpen.execute(sender, new ArrayList<>());
    }

}
