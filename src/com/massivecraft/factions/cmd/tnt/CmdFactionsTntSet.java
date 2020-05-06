package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CmdFactionsTntSet extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsTntSet()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
        this.addParameter(TypeInteger.get(), "amount", "0");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArgAt(0, msenderFaction);

        if (faction != msenderFaction && ! Perm.TNT_SET_ANY.has(sender, true)) return;

        int amount = this.readArgAt(1);
        faction.setTnt(amount);
        this.msg("<i>Set %s's<i> TNT balance to <h>%,d<i>.", faction.describeTo(msender, true), faction.getTnt());
    }

}
