package com.massivecraft.factions.cmd.loot;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CmdFactionsLootAdd extends FactionsCommand
{

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsLootAdd()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
        this.addParameter(TypeInteger.get(), "amount", "1");

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.LOOT_ADD));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);
        int amount = this.readArg(1);

        // Apply
        faction.setLootRewards(faction.getLootRewards() + amount);

        // Inform
        msender.msg("<g>You gave <i>%,d <g>loot rewards to the faction <i>%s<g>.", amount, faction.getName());
    }

}
