package com.massivecraft.factions.cmd.vault;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsVaultSet extends FactionsCommand {

    public CmdFactionsVaultSet() {
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        Faction faction = this.readArg(msenderFaction);

        if ( ! MPerm.getPermVault().has(msender, faction, true)) return;

        // TODO: Setup vault location

    }

}
