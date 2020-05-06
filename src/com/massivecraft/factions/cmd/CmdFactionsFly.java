package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.task.TaskFactionsFly;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsFly extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsFly()
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
        if ( ! MConf.get().flyEnabled)
        {
            this.msg("<b>Faction flight is currently disabled and cannot be used.");
            return;
        }

        // Args
        boolean target = this.readArg(!msender.isFlying());

        // Check & Inform
        if (target)
        {
            if (TaskFactionsFly.get().isEnemyNear(me, msenderFaction))
            {
                msg("<b>You can't enable fly as there are enemies nearby.");
                return;
            }
            Faction at = BoardColl.get().getFactionAt(PS.valueOf(me));
            if ((at != msenderFaction && ! msenderFaction.isNone()) && ! Perm.FLY_ANY.has(sender))
            {
                msg("<b>You can only enable fly in your faction territory.");
                return;
            }
            if (msender.isFlying())
            {
                msg("<b>You already have faction fly enabled.");
                return;
            }
        }
        else
        {
            if (!msender.isFlying())
            {
                msg("<b>You already have faction fly disabled.");
                return;
            }
        }

        // Apply
        msender.setFlying(target);

        // Inform
        String desc = Txt.parse(msender.isFlying() ? "<g>ENABLED" : "<b>DISABLED");

        String messageYou = Txt.parse("<i>%s %s <i>faction flight.", msender.getDisplayName(msender), desc);

        msender.message(messageYou);
    }

}
