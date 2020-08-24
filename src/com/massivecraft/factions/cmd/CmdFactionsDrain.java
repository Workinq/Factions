package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.money.Money;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CmdFactionsDrain extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDrain()
    {
        // Aliases
        this.setAliases("drain");

        // Desc
        this.setDescPermission("factions.drain");

        // Parameters
        this.addParameter(TypeSet.get(TypeMPlayer.get()), "players/all", true);

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Set<MPlayer> mplayers = new HashSet<>();
        boolean all = false;

        // Args
        if ("all".equalsIgnoreCase(this.argAt(0)))
        {
            List<MPlayer> offlineMembers = msenderFaction.getMPlayersWhereOnline(false);

            // Doesn't show up if list is empty. Test at home if it worked.
            if (offlineMembers == null)
            {
                throw new MassiveException().addMsg("<b>You don't have any offline members in your faction.");
            }

            // Check if there are no invites.
            if (offlineMembers.isEmpty())
            {
                throw new MassiveException().addMsg("<b>You don't have any offline members in your faction.");
            }

            // Remove players with high enough rank.
            offlineMembers.removeIf(mplayer -> mplayer.getRole().isAtMost(MConf.get().drainRank));

            all = true;

            mplayers.addAll(offlineMembers);
        }
        else
        {
            mplayers = this.readArgAt(0);
        }

        if ( ! MPerm.getPermDrain().has(msender, msenderFaction, true)) return;

        boolean success = false;
        for (MPlayer mplayer : mplayers)
        {
            if (mplayer.isOnline())
            {
                msg("%s <b>can't drain %s's <b>balance because they're online.", msender.describeTo(msender, true), mplayer.describeTo(msender));
                continue;
            }

            if (mplayer.getRole().isLessThan(MConf.get().drainRank))
            {
                msg("%s <b>can't drain %s's <b>balance because their rank isn't below <h>%s<b>.", msender.describeTo(msender, true), mplayer.describeTo(msender), MConf.get().drainRank.getName());
                continue;
            }

            if (msender.getRole().isAtMost(mplayer.getRole()))
            {
                msg("%s <b>can't drain %s's <b>balance because their rank is equal to or higher than yours.", msender.describeTo(msender, true), mplayer.describeTo(msender));
                continue;
            }

            double amount = Money.get(mplayer);
            if ( ! Money.move(mplayer, msenderFaction, msender, Money.get(mplayer), "Factions"))
            {
                msender.msg("Unable to transfer %s<b> to <h>%s<b> from <h>%s<b>.", Money.format(amount), msenderFaction.describeTo(msender), mplayer.describeTo(msender));
                continue;
            }

            success = true;
        }

        if ( ! success ) return;

        // Inform
        if (all)
        {
            msenderFaction.msg("%s <i>drained the balance for all offline members into <g>your faction's <i>bank.", msender.describeTo(msenderFaction, true));
        }
        else
        {
            msg("%s <i>drained the balances of <h>%,d <i>members.", msender.describeTo(msender, true), mplayers.size());
        }
    }

}
