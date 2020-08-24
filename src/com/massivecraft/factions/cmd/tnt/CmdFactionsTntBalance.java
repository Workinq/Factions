package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsTntBalance extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsTntBalance()
    {
        // Aliases
        this.addAliases("balance");

        // Desc
        this.setDescPermission("factions.tnt.balance");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg(msenderFaction);

        if (faction != msenderFaction && ! Perm.TNT_BALANCE_ANY.has(sender, true)) return;

        if ( ! MPerm.getPermTnt().has(msender, faction, true)) return;

        msender.msg("%s's<i> TNT balance is <h>%,d<i>.", faction.describeTo(msender, true), faction.getTnt());
    }

}
