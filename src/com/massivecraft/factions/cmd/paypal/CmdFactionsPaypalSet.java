package com.massivecraft.factions.cmd.paypal;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsPaypalSet extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsPaypalSet()
    {
        // Parameters
        this.addParameter(TypeString.get(), "email");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MPerm.getPermPaypal().has(msender, msenderFaction, true)) return;

        // Args
        String paypal = this.readArg();

        // Apply
        msenderFaction.setPaypal(paypal);

        // Inform
        msender.msg("%s <i>set the faction paypal to <a>%s<i>.", msender.describeTo(msender, true), paypal);
    }

}
