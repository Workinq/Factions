package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.cmd.type.TypeRel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsRosterSetrank extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterSetrank()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
        this.addParameter(TypeRel.get(), "role");
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
        Rel rel = this.readArg();
        Faction faction = this.readArg(msenderFaction);

        // Verify
        if ( ! MPerm.getPermRoster().has(msender, faction, true)) return;

        if ( ! faction.isInRoster(mplayer))
        {
            msg("%s <i>is not in the faction roster.", mplayer.describeTo(msender));
            return;
        }

        if (mplayer.getRole().isMoreThan(msender.getRole()) && ! msender.isOverriding())
        {
            throw new MassiveException().addMsg("<b>You can't set people's rank to a higher one than yours.");
        }

        if (mplayer.getRole() == msender.getRole() && ! msender.isOverriding())
        {
            throw new MassiveException().addMsg("<b>You can't set people's rank to the same as yours.");
        }

        faction.setRosterRank(mplayer, rel);
        msg("%s <i>changed %s's <i>roster rank to <h>%s<i>.", msender.describeTo(msender, true), mplayer.describeTo(msender), rel.getName());
    }

}
