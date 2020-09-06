package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;

public class CmdFactionsShieldClear extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShieldClear()
    {
        // Aliases
        this.addAliases("clear");

        // Desc
        this.setDescPermission("factions.shield.clear");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
        this.addParameter(TypeBooleanYes.get(), "silent", "yes");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg(msenderFaction);
        boolean silent = this.readArg(true);

        if ( ! MOption.get().isGrace() && faction.hasShield() && ! msender.isOverriding() )
        {
            throw new MassiveException().setMsg("<b>You can't clear %s's shield as grace has been disabled.", faction.describeTo(msender));
        }

        // Permission
        if (faction != msenderFaction && ! Perm.SHIELD_CLEAR_ANY.has(sender, true)) return;

        // MPerm
        if ( ! MPerm.getPermShield().has(msender, faction, true) ) return;

        // Apply
        faction.setShieldedHour(null);

        // Inform
        msender.msg("%s <i>cleared %s's <i>faction shield.", msender.describeTo(msender, true), faction.describeTo(msender));
        if ( ! silent )
        {
            faction.msg("<g>Your <i>faction shield has been cleared by %s<i>.", msender.describeTo(faction));
        }
    }

}
