package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsAlarm extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsAlarm()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
        this.addParameter(TypeBooleanYes.get(), "on/off", "flip");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);
        boolean target = this.readArg( ! faction.isAlarmEnabled() );

        // Verify - Permission
        if (faction != msenderFaction && ! Perm.ALARM_ANY.has(sender, true)) return;

        // Verify - Faction
        if (faction.isSystemFaction())
        {
            throw new MassiveException().setMsg("%s <b>must be in a faction to toggle the faction alarm.", msender.describeTo(msender, true));
        }

        // MPerm
        if ( ! MPerm.getPermAlarm().has(msender, faction, true) ) return;

        // Apply
        faction.setAlarmEnabled(target);

        // Inform
        String desc = Txt.parse(msender.isStealth() ? "<g>ENABLED" : "<b>DISABLED");
        String messageFaction = Txt.parse("<i>%s %s <i>the faction alarm.", msender.describeTo(faction, true), desc);
        faction.msg(messageFaction);
    }

}
