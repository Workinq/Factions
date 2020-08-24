package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsDrainToggle extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDrainToggle()
    {
        // Aliases
        this.setAliases("drainToggle");

        // Desc
        this.setDescPermission("factions.drain.toggle");

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
        boolean target = this.readArg( ! msender.isDrainToggled() );

        // Apply
        msender.setDrainToggled(target);

        // Inform
        String desc = Txt.parse(msender.isDrainToggled() ? "<g>ENABLED" : "<b>DISABLED");
        String messageYou = Txt.parse("<i>%s %s <i>balance draining.", msender.describeTo(msender, true), desc);

        msender.message(messageYou);
    }

}
