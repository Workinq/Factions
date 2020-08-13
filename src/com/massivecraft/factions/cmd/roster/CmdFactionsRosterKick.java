package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.*;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import java.util.Collections;

public class CmdFactionsRosterKick extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterKick()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
        this.addParameter(TypeFaction.get(), "faction", "you");
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
            throw new MassiveException().setMsg("<b>You can't kick players from your roster as grace has been disabled.");
        }

        // MPerm
        if ( ! MPerm.getPermRoster().has(msender, faction, true)) return;

        // Verify
        if ( ! faction.isInRoster(mplayer) )
        {
            throw new MassiveException().setMsg("%s <i>is not in the faction roster.", mplayer.describeTo(msender));
        }

        if (mplayer.getRole().isMoreThan(msender.getRole()) && ! msender.isOverriding())
        {
            throw new MassiveException().addMsg("<b>You can't kick people of higher rank than yourself.");
        }

        if (mplayer.getRole() == msender.getRole() && ! msender.isOverriding())
        {
            throw new MassiveException().addMsg("<b>You can't kick people of the same rank as yourself.");
        }

        if (faction.getNumberOfRosterKicks() == MConf.get().rosterKickLimit)
        {
            // Args
            MassiveList<Long> rosterKickTimes = new MassiveList<>(faction.getRosterKickTimes());
            boolean canKick = false;

            // Sort
            Collections.sort(rosterKickTimes);

            // Loop
            for (long time : rosterKickTimes)
            {
                long total = time + TimeUnit.MILLIS_PER_DAY;
                if (System.currentTimeMillis() > total)
                {
                    // Apply
                    canKick = true;

                    // Remove
                    faction.removeRosterKick(time);
                }
            }

            // Verify
            if (!canKick)
            {
                throw new MassiveException().setMsg("<b>You've reached the maximum number of roster kicks in the last 24 hours.");
            }
        }

        // Apply
        faction.addRosterKick();
        faction.removeFromRoster(mplayer);
        CmdFactions.get().cmdFactionsKick.execute(sender, MUtil.list(mplayer.getName()));

        // Inform
        msg("%s <i>removed %s <i>from the faction roster.", msender.describeTo(msender, true), mplayer.describeTo(msender));
        faction.msg("%s <i>was removed to the faction roster.", mplayer.describeTo(faction, true));
    }

}
