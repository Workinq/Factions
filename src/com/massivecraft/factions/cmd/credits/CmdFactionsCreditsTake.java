package com.massivecraft.factions.cmd.credits;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CmdFactionsCreditsTake extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsCreditsTake()
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

        if ( ! Perm.CREDITS_TAKE.has(sender, true)) return;

        if (amount < 0) amount *= -1;

        faction.takeCredits(amount);

        msg("<a>%s <i>credits have been deducted from %s<i>.", String.format("%,d", amount), faction.describeTo(msender));
    }

}
