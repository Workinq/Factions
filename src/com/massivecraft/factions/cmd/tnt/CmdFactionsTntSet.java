package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CmdFactionsTntSet extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsTntSet()
    {
        // Parameters
        this.addParameter(TypeInteger.get(), "amount");
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Permissions
        this.addRequirements(RequirementHasPerm.get(Perm.TNT_SET));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);
        int amount = this.readArg();

        // Perm
        if (faction != msenderFaction && ! Perm.TNT_SET_ANY.has(sender, true) ) return;

        // Apply
        faction.setTnt(amount);

        // Inform
        msg("<i>Set %s's<i> TNT balance to <h>%,d<i>.", faction.describeTo(msender, true), faction.getTnt());
    }

}
