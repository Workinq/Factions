package com.massivecraft.factions.cmd.shard;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsShardsChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanTrue;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class CmdFactionsShardsAddPlayer extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShardsAddPlayer()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player", "you");
        this.addParameter(TypeInteger.get(), "amount", "1");
        this.addParameter(TypeBooleanTrue.get(), "silent", "false");

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.SHARDS_ADD_PLAYER));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        MPlayer mplayer = this.readArg(msender);
        int amount = this.readArg(1);
        boolean silent = this.readArg(false);

        // Verify
        if ( ! mplayer.hasFaction())
        {
            msg("%s <b>must belong to a faction to add shards.", mplayer.describeTo(msender));
            return;
        }

        // Check Negative
        if (amount < 0) amount *= -1;

        Faction faction = mplayer.getFaction();
        if (faction.isSystemFaction())
        {
            msg("<b>You can't add shards to a system faction.");
            return;
        }

        // Event
        EventFactionsShardsChange event = new EventFactionsShardsChange(faction, amount);
        event.run();
        if (event.isCancelled()) return;

        // Apply
        faction.addShards(event.getShards());

        // Inform
        msg("%s <i>gave <h>%,dx <i>shards to %s<i>.", msender.describeTo(msender, true), amount, mplayer.describeTo(msender));
        if ( ! silent) mplayer.msg("%s <i>received <h>%,d <i>shards.", faction.describeTo(msender, true), amount);
    }

}
