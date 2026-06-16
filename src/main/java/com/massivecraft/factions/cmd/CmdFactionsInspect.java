package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.integration.coreprotect.IntegrationCoreProtect;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsInspect extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsInspect()
    {
        // Parameters
        this.addParameter(TypeBooleanYes.get(), "on/off", "flip");

        // Requirements
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! IntegrationCoreProtect.get().isActive()) throw new MassiveException().setMsg("<b>Inspecting faction land is currently disabled.");

        if ( ! MPerm.getPermInspect().has(msender, msenderFaction, true)) return;

        // Args
        boolean target = this.readArg(!msender.isInspecting());

        // Check & Inform
        if (target)
        {
            if (msender.isInspecting()) throw new MassiveException().setMsg("<b>You already have faction inspect mode enabled.");
        }
        else
        {
            if (!msender.isInspecting()) throw new MassiveException().setMsg("<b>You already have inspect mode disabled.");
        }

        // Apply
        msender.setInspecting(target);

        // Inform
        String desc = Txt.parse(msender.isInspecting() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>inspect mode.", msender.describeTo(msender, true), desc);

        msender.message(messageYou);
    }

}
