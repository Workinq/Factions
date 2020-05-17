package com.massivecraft.factions.cmd.shard;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsShardsChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanTrue;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CmdFactionsShardsAddFaction extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShardsAddFaction()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.addParameter(TypeBooleanTrue.get(), "silent", "false");

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.SHARDS_ADD_FACTION));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);
        int amount = this.readArg(1);
        boolean silent = this.readArg(false);

        // Verify
        if (faction.isSystemFaction())
        {
            msg("<b>You can only add shards to normal factions.");
            return;
        }

        // Check Negative
        if (amount < 0) amount *= -1;

        // Event
        EventFactionsShardsChange event = new EventFactionsShardsChange(faction, amount);
        event.run();
        if (event.isCancelled()) return;

        // Apply
        faction.addShards(event.getShards());

        // Inform
        msg("%s <i>gave <h>%,dx <i>shards to %s<i>.", msender.describeTo(msender, true), amount, faction.describeTo(msender));
        if ( ! silent) faction.msg("<g>Your faction <i>received <h>%,d <i>shards.", amount);
    }

}
