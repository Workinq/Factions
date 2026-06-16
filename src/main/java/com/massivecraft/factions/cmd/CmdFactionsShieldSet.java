package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.gui.ShieldSelectorMenu;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CmdFactionsShieldSet extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShieldSet()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg(msenderFaction);

        if ( ! MConf.get().grace && faction.hasShield() && ! msender.isOverriding() )
        {
            throw new MassiveException().setMsg("<b>You can't change your faction shield as grace has been disabled.");
        }

        if ( ! MPerm.getPermShield().has(msender, faction, true) ) return;

        new ShieldSelectorMenu(me, faction).open();
    }

    // -------------------------------------------- //
    // TIME HELPERS
    // -------------------------------------------- //
    // Public: also used by CmdFactionsShieldView and EngineShow to render the shield window.

    public String getTime(Calendar calendar)
    {
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0) return "Midnight";
        if (calendar.get(Calendar.HOUR_OF_DAY) == 12) return "Midday";

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(calendar.getTime());
    }

    public Calendar getFreshCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);
        return calendar;
    }

}
