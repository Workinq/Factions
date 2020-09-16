package com.massivecraft.factions.cmd;

import com.massivecraft.factions.engine.EngineFly;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsFly extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsFly()
    {
        // Aliases
        this.setAliases("fly");

        // Desc
        this.setDescPermission("factions.fly");

        // Parameters
        this.addParameter(TypeBooleanYes.get(), "on/off", "flip");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        boolean value = this.readArg( ! EngineFly.get().playersWithFlyDisabled.contains(me.getUniqueId()) );

        if (value)
        {
            if (EngineFly.get().playersWithFlyDisabled.contains(me.getUniqueId()))
            {
                msg("<b>You already have faction flight disabled.");
            }
            else
            {
                msg("<i>%s %s <i>faction flight.", msender.describeTo(msender, true), Txt.parse("<b>DISABLED"));
                EngineFly.get().playersWithFlyDisabled.add(me.getUniqueId());
            }
        }
        else if ( ! EngineFly.get().playersWithFlyDisabled.contains(me.getUniqueId()) )
        {
            msg("<b>You already have faction flight disabled.");
        }
        else
        {
            msg("<i>%s %s <i>faction flight.", msender.describeTo(msender, true), Txt.parse("<g>ENABLED"));
            EngineFly.get().playersWithFlyDisabled.remove(me.getUniqueId());
        }
    }

}
