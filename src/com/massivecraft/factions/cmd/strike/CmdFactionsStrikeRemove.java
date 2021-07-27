package com.massivecraft.factions.cmd.strike;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.FactionStrike;
import com.massivecraft.factions.event.EventFactionsStrikeRemove;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanTrue;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsStrikeRemove extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsStrikeRemove()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction");
        this.addParameter(TypeString.get(), "strike");
        this.addParameter(TypeBooleanTrue.get(), "silent", "false");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg();
        String strikeId = this.readArg();
        boolean silent = this.readArg(false);

        FactionStrike strike = faction.getStrikeFromId(strikeId);
        if (strike == null)
        {
            msg("<b>There is no strike with the ID <h>%s<b>.", strikeId);
            return;
        }

        // Event
        EventFactionsStrikeRemove event = new EventFactionsStrikeRemove(sender, faction, strike);
        event.run();
        if (event.isCancelled()) return;

        // Apply
        faction.deleteStrike(event.getNewStrike());

        // Inform
        if ( ! silent) faction.msg("<i>A strike on your faction has been removed.");
        msg("%s <i>removed the strike <h>%s <n>(%s) <i>from %s<i>.", msender.describeTo(msender, true), event.getNewStrike().getMessage(), event.getNewStrike().getStrikeId(), faction.describeTo(msender));
    }

}
