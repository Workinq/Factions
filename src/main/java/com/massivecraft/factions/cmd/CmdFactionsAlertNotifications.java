package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsAlertNotifications extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsAlertNotifications()
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
        boolean target = this.readArg( ! msender.hasAlertNotifications());

        // Apply
        msender.setAlertNotifications(target);

        // Inform
        String desc = Txt.parse(msender.hasAlertNotifications() ? "<g>ENABLED" : "<b>DISABLED");
        String messageYou = Txt.parse("<i>%s %s <i>alert notifications.", msender.describeTo(msender, true), desc);

        msender.message(messageYou);
    }

}
