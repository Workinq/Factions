package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsAutoFly extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsAutoFly()
    {
        // Parameters
        this.addParameter(TypeBooleanYes.get(), "on/off", "flip");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        boolean target = this.readArg(!msender.isAutoFlying());

        // Check & Inform
        if (target)
        {
            if (msender.isAutoFlying())
            {
                msg("<b>You already have auto fly enabled.");
                return;
            }
        }
        else
        {
            if (!msender.isAutoFlying())
            {
                msg("<b>You already have auto fly disabled.");
                return;
            }
        }

        // Apply
        msender.setAutoFly(target);

        // Inform
        String desc = Txt.parse(msender.isAutoFlying() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>auto fly.", msender.describeTo(msender, true), desc);

        msender.message(messageYou);
    }

}
