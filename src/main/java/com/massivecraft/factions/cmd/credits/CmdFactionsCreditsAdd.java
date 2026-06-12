package com.massivecraft.factions.cmd.credits;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CmdFactionsCreditsAdd extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsCreditsAdd()
    {
        // Parameters
        this.addParameter(TypeInteger.get(), "amount");
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        int amount = this.readArg();
        Faction faction = this.readArg(msenderFaction);

        if ( ! Perm.CREDITS_ADD.has(sender, true)) return;

        if (amount < 0) amount *= -1;

        faction.addCredits(amount);

        msg("<a>%s <i>credits have been added to %s<i>.", String.format("%,d", amount), faction.describeTo(msender));
    }

}
