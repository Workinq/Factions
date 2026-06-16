package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsPaypalCheck extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsPaypalCheck()
    {
        // Aliases
        this.addAliases("see");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);

        if (faction != msenderFaction && ! Perm.PAYPAL_CHECK_OTHER.has(sender, true)) return;

        if ( ! MPerm.getPermPaypal().has(msender, faction, true)) return;

        String paypal = faction.getPaypal();

        // Inform
        if (paypal.equals("none")) throw new MassiveException().setMsg("%s <i>doesn't have a paypal set.", faction.describeTo(msender, true));
        msg("%s's <i>paypal is <a>%s<i>.", faction.describeTo(msender, true), paypal);
    }

}
