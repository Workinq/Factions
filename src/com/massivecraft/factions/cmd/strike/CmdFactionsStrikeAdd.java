package com.massivecraft.factions.cmd.strike;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.FactionStrike;
import com.massivecraft.factions.event.EventFactionsStrikeAdd;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.container.TypeList;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

import java.util.List;

public class CmdFactionsStrikeAdd extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsStrikeAdd()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction");
        this.addParameter(TypeInteger.get(), "points");
        this.addParameter(TypeString.get(), "reason", true);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg();
        int points = this.readArg();
        String reason = this.readArg();

        // Event
        EventFactionsStrikeAdd event = new EventFactionsStrikeAdd(sender, faction, new FactionStrike(System.currentTimeMillis(), points, reason, me.getUniqueId().toString()));
        event.run();
        if (event.isCancelled()) return;

        // Apply
        faction.addStrike(event.getNewStrike());

        // Inform
        faction.msg("%s <i>was striked <h>%s <i>points for <h>%s<i>.", faction.describeTo(faction), String.valueOf(points), reason);
        msg("<i>You striked %s <i>with the reason <h>%s<i>.", faction.describeTo(msender), reason);
    }

}
