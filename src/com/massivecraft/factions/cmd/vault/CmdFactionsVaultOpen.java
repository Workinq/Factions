package com.massivecraft.factions.cmd.vault;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsVaultOpen extends FactionsCommand {

    public CmdFactionsVaultOpen() {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    @Override
    public void perform() throws MassiveException {
        Faction faction = this.readArg(msenderFaction);

        if (faction != msenderFaction && ! Perm.VAULT_OPEN_ANY.has(sender, true)) return;

        if ( ! MPerm.getPermVault().has(msender, faction, true)) return;

        //TODO: open GUI
    }
}
