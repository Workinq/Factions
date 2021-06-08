package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsLogin extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsLogin()
    {
        // Aliases
        this.addAliases("logins");

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
        boolean target = this.readArg( ! msender.getLogins());

        // Apply
        msender.setLogins(target);

        // Inform
        String desc = Txt.parse(msender.getLogins() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>member logins.", msender.describeTo(msender, true), desc);
        String messageLog = Txt.parse("<i>%s %s <i>member logins.", msender.getDisplayName(IdUtil.getConsole()), desc);

        msender.message(messageYou);
    }

}
