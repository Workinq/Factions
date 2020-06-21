package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MOption;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;

public class CmdFactionsRosterRemove extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterRemove()
    {
        // Aliases
        this.addAliases("kick");

        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        MPlayer mplayer = this.readArg();
        Faction faction = this.readArg(msenderFaction);

        // Grace
        if ( ! MOption.get().isGrace() && ! msender.isOverriding() )
        {
            msg("<b>You can't kick players from your roster as grace has been disabled.");
            return;
        }

        // Verify
        if ( ! MPerm.getPermRoster().has(msender, faction, true)) return;

        if ( ! faction.isInRoster(mplayer) )
        {
            msg("%s <i>is not in the faction roster.", mplayer.describeTo(msender));
            return;
        }

        if (mplayer.getRole().isMoreThan(msender.getRole()) && ! msender.isOverriding())
        {
            throw new MassiveException().addMsg("<b>You can't kick people of higher rank than yourself.");
        }

        if (mplayer.getRole() == msender.getRole() && ! msender.isOverriding())
        {
            throw new MassiveException().addMsg("<b>You can't kick people of the same rank as yourself.");
        }

        faction.removeFromRoster(mplayer); // Remove from roster
        CmdFactions.get().cmdFactionsKick.execute(sender, MUtil.list(mplayer.getName())); // Kick

        msg("%s <i>removed %s <i>from the faction roster.", msender.describeTo(msender, true), mplayer.describeTo(msender));
        faction.msg("%s <i>was removed to the faction roster.", mplayer.describeTo(faction, true));
    }

}
