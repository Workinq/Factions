package com.massivecraft.factions.cmd.loot;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsLootBalance extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsLootBalance()
    {
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

        if (faction != msenderFaction && ! Perm.LOOT_BALANCE_ANY.has(sender, true)) return;

        if ( ! MPerm.getPermLoot().has(msender, faction, true)) return;

        msg("%s's<i> loot rewards balance is <h>%,d<i>.", faction.describeTo(msender, true), faction.getLootRewards());
    }

}
