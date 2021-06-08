package com.massivecraft.factions.cmd.toggle;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.factions.event.EventFactionsToggleGrace;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsToggleGrace extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsToggleGrace()
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
        boolean target = this.readArg( ! MOption.get().isGrace() );

        // Event
        EventFactionsToggleGrace event = new EventFactionsToggleGrace(sender, target);
        event.run();
        if (event.isCancelled()) return;
        target = event.isActive();

        // Apply
        MOption.get().setGrace(target);

        // Inform
        String desc = Txt.parse(MOption.get().isGrace() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>server grace period.", msender.describeTo(msender, true), desc);
        String messageLog = Txt.parse("<i>%s %s <i>server grace period.", msender.getDisplayName(IdUtil.getConsole()), desc);

        msender.message(messageYou);
        Factions.get().log(messageLog);
    }

}
