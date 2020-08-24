package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsSpy extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSpy()
    {
        // Aliases
        this.setAliases("spy");

        // Desc
        this.setDescPermission("factions.spy");

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
        boolean target = this.readArg( ! msender.isSpying());

        // Verify
        if ( ! Perm.SPY.has(sender, true)) return;

        // Apply
        msender.setSpying(target);

        // Inform
        String desc = Txt.parse(msender.isSpying() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>chat spy.", msender.describeTo(msender, true), desc);

        msender.message(messageYou);
    }

}
