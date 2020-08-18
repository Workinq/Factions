package com.massivecraft.factions.cmd.alt;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

public class CmdFactionsAltJoin extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsAltJoin()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction");
        this.addParameter(TypeMPlayer.get(), "player", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg();

        MPlayer mplayer = this.readArg(msender);
        Faction mplayerFaction = mplayer.getFaction();

        boolean samePlayer = mplayer == msender;

        // Validate
        if (!samePlayer  && ! Perm.JOIN_OTHERS.has(sender, false))
        {
            msg("<b>You do not have permission to move other players into a faction.");
            return;
        }

        if (faction == mplayerFaction)
        {
            String command = CmdFactions.get().cmdFactionsKick.getCommandLine(mplayer.getName());

            // Mson creation
            Mson alreadyMember = Mson.mson(
                    Mson.parse(mplayer.describeTo(msender, true)),
                    mson((samePlayer ? " are" : " is") + " already an alt for " + faction.getName(msender) + ".").color(ChatColor.YELLOW)
            );

            message(alreadyMember.suggest(command).tooltip(Txt.parse("<i>Click to <c>%s<i>.", command)));
            return;
        }

        if (MConf.get().factionAltLimit > 0 && faction.getMPlayersWhereAlt(true).size() >= MConf.get().factionAltLimit)
        {
            msg(" <b>!<white> The faction %s is at the limit of %d alts, so %s cannot currently join.", faction.getName(msender), MConf.get().factionMemberLimit, mplayer.describeTo(msender, false));
            return;
        }

        if (mplayerFaction.isNormal())
        {
            String command = CmdFactions.get().cmdFactionsLeave.getCommandLine(mplayer.getName());

            // Mson creation
            Mson leaveFirst = Mson.mson(
                    Mson.parse(mplayer.describeTo(msender, true)),
                    mson(" must leave " + (samePlayer ? "your" : "their") + " current faction first.").color(ChatColor.RED)
            );

            message(leaveFirst.suggest(command).tooltip(Txt.parse("<i>Click to <c>%s<i>.", command)));
            return;
        }

        if (!MConf.get().canLeaveWithNegativePower && mplayer.getPower() < 0)
        {
            msg("<b>%s cannot join a faction with a negative power level.", mplayer.describeTo(msender, true));
            return;
        }

        if (faction.isBanned(msender))
        {
            msg("<b>You've been banned from joining %s<b>.", faction.describeTo(msender));
            return;
        }

        if (faction.isInvited(mplayer) && ! faction.isInvitedAlt(mplayer) && ! faction.getFlag(MFlag.getFlagOpen()))
        {
            msg("<b>You can't join this faction as an alt, use /f join instead.");
            return;
        }

        if ( ! faction.isInvitedAlt(mplayer) && ! msender.isOverriding() && ! faction.getFlag(MFlag.getFlagOpen()) )
        {
            msg("<i>This faction requires invitation to join as an alt.");
            if (samePlayer)
            {
                faction.msg("%s<i> tried to join your faction as an alt.", mplayer.describeTo(faction, true));
            }
            return;
        }

        // Event
        EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, msender, faction, EventFactionsMembershipChange.MembershipChangeReason.JOIN);
        membershipChangeEvent.run();
        if (membershipChangeEvent.isCancelled()) return;

        // Inform
        if ( ! samePlayer )
        {
            mplayer.msg("<i>%s <i>moved you into the faction %s<i> as an alt.", msender.describeTo(mplayer, true), faction.getName(mplayer));
        }
        faction.msg("<i>%s <i>joined <lime>your faction<i> as an alt.", mplayer.describeTo(faction, true));
        msender.msg("<i>%s <i>successfully joined %s<i> as an alt.", mplayer.describeTo(msender, true), faction.getName(msender));

        // Apply
        mplayer.resetFactionData();
        mplayer.setFaction(faction);

        // Alt
        mplayer.setAlt(true);

        faction.uninvite(mplayer);

        // Derplog
        if (MConf.get().logFactionJoin)
        {
            if (samePlayer)
            {
                Factions.get().log(Txt.parse("%s joined the faction %s as an alt.", mplayer.getName(), faction.getName()));
            }
            else
            {
                Factions.get().log(Txt.parse("%s moved the player %s into the faction %s as an alt.", msender.getName(), mplayer.getName(), faction.getName()));
            }
        }
    }

}
