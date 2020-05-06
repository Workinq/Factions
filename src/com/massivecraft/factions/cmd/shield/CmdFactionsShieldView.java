package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsShieldView extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShieldView()
    {
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

        if (faction.getShieldString() == null)
        {
            msg("%s <i>does not have a shield set.", faction.describeTo(msender, true));
        }
        else
        {
            String shieldString = faction.getShieldString();
            msg("%s's <i>shield is active from the following hours: %s", faction.describeTo(msender, true), shieldString.substring(0, shieldString.indexOf('(') - 2));
        }
    }

}
