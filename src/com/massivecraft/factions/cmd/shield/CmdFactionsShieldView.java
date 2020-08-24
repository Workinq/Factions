package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;

import java.util.Calendar;

public class CmdFactionsShieldView extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShieldView()
    {
        // Aliases
        this.addAliases("view");

        // Desc
        this.setDescPermission("factions.shield.view");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg(msenderFaction);

        if (faction != msenderFaction && ! Perm.SHIELD_VIEW_ANY.has(sender, true)) return;

        if ( ! faction.isShielded() )
        {
            msg("%s <i>does not have a shield set.", faction.describeTo(msender, true));
        }
        else
        {
            // Command
            final CmdFactionsShieldSet cmdFactionsShieldSet = CmdFactions.get().cmdFactionsShield.cmdFactionsShieldSet;

            // Args
            Calendar calendar = cmdFactionsShieldSet.getFreshCalendar();
            calendar.set(Calendar.HOUR_OF_DAY, faction.getShieldedHour());
            String from = cmdFactionsShieldSet.getTime(calendar);
            Calendar clone = (Calendar) calendar.clone();
            clone.add(Calendar.HOUR_OF_DAY, MConf.get().shieldHours);
            String to = cmdFactionsShieldSet.getTime(clone);

            // Inform
            msg("%s's <i>shield is active from <h>%s <i>to <h>%s<i>.", faction.describeTo(msender, true), from, to);
        }
    }

}
