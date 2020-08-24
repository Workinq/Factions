package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsStealth extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsStealth()
    {
        // Aliases
        this.setAliases("stealth");

        // Desc
        this.setDescPermission("factions.stealth");

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
        boolean target = this.readArg( ! msender.isStealth());

        // Apply
        msender.setStealth(target);

        // Inform
        String desc = Txt.parse(msender.isStealth() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>stealth mode.", msender.describeTo(msender, true), desc);

        msender.message(messageYou);
    }

}
