package com.massivecraft.factions.cmd.toggle;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsToggleFly extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsToggleFly()
    {
        // Aliases
        this.addAliases("flight");

        // Parameters
        this.addParameter(TypeBooleanYes.get(), "on/off", "flip");

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.TOGGLE_FLY));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        boolean target = this.readArg( ! MOption.get().isFlight() );

        // Apply
        MOption.get().setFlight(target);

        // Inform
        String desc = Txt.parse(MOption.get().isFlight() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>server faction flight.", msender.describeTo(msender, true), desc);
        String messageLog = Txt.parse("<i>%s %s <i>server faction flight.", msender.getDisplayName(IdUtil.getConsole()), desc);

        msender.message(messageYou);
        Factions.get().log(messageLog);
    }

}
