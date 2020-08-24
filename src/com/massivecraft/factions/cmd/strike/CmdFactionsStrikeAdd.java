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
        // Aliases
        this.addAliases("add");

        // Desc
        this.setDescPermission("factions.strike.add");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction");
        this.addParameter(TypeInteger.get(), "points");
        this.addParameter(TypeList.get(TypeString.get()), "reason", true);

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.STRIKE_ADD));
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
        List<String> reason = this.readArg();

        StringBuilder builder = new StringBuilder();
        for (String string : reason)
        {
            builder.append(string).append(" ");
        }
        String reasonString = builder.toString().trim();

        // Event
        EventFactionsStrikeAdd event = new EventFactionsStrikeAdd(sender, faction, new FactionStrike(System.currentTimeMillis(), points, reasonString, me.getUniqueId().toString()));
        event.run();
        if (event.isCancelled()) return;

        // Apply
        faction.addStrike(event.getNewStrike());

        // Inform
        faction.msg("%s <i>was striked <h>%s <i>points for <h>%s<i>.", faction.describeTo(faction), String.valueOf(points), reasonString);
        msg("<i>You striked %s <i>with the reason <h>%s<i>.", faction.describeTo(msender), reasonString);
    }

}
