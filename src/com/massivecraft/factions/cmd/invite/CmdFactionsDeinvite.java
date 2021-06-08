package com.massivecraft.factions.cmd.invite;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CmdFactionsDeinvite extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDeinvite()
    {
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
        Set<MPlayer> mplayers = new HashSet<>();
        boolean all = false;

        // Args
        if ("all".equalsIgnoreCase(this.argAt(0)))
        {
            Set<String> ids = msenderFaction.getInvitations().keySet();

            // Doesn't show up if list is empty. Test at home if it worked.
            if (ids == null)
            {
                throw new MassiveException().addMsg("<b>No one is invited to your faction.");
            }

            // Remove all invites that aren't alts.
            ids.removeIf(id -> msenderFaction.getInvitations().get(id).isAlt());

            // Check if there are no invites.
            if (ids.isEmpty())
            {
                throw new MassiveException().addMsg("<b>No one is invited to your faction.");
            }

            all = true;

            for (String id : ids)
            {
                mplayers.add(MPlayer.get(id));
            }
        }
        else
        {
            mplayers = this.readArgAt(0);
        }

        // MPerm
        if ( ! MPerm.getPermInvite().has(msender, msenderFaction, true)) return;

        for (MPlayer mplayer : mplayers)
        {
            // Already member?
            if (mplayer.getFaction() == msenderFaction)
            {
                // Mson
                String command = CmdFactions.get().cmdFactionsKick.getCommandLine(mplayer.getName());
                String tooltip = Txt.parse("Click to <c>%s<i>.", command);

                Mson kick = Mson.mson(
                        mson("You might want to kick him. ").color(ChatColor.YELLOW),
                        mson(ChatColor.RED.toString() + tooltip).tooltip(ChatColor.YELLOW.toString() + tooltip).suggest(command)
                );

                // Inform
                msg("%s<i> is already a member of %s<i>.", mplayer.getName(), msenderFaction.getName(msender));
                message(kick);
                continue;
            }

            // Already invited?
            boolean isInvited = msenderFaction.isInvited(mplayer);

            if (isInvited)
            {
                // Event
                EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
                event.run();
                if (event.isCancelled()) continue;
                isInvited = event.isNewInvited();

                // Inform Player
                mplayer.msg("%s<i> revoked your invitation to <h>%s<i>.", msender.describeTo(mplayer, true), msenderFaction.describeTo(mplayer));

                // Inform Faction
                if ( ! all)
                {
                    msenderFaction.msg("%s<i> revoked %s's<i> invitation.", msender.describeTo(msenderFaction), mplayer.describeTo(msenderFaction));
                }

                // Apply
                msenderFaction.uninvite(mplayer);

                // If all, we do this at last. So we only do it once.
                if (! all) msenderFaction.changed();
            }
            else
            {
                // Mson
                String command = CmdFactions.get().cmdFactionsInvite.getCommandLine(mplayer.getName());
                String tooltip = Txt.parse("Click to <c>%s<i>.", command);

                Mson invite = Mson.mson(
                        mson("You might want to invite him. ").color(ChatColor.YELLOW),
                        mson(ChatColor.GREEN.toString() + tooltip).tooltip(ChatColor.YELLOW.toString() + tooltip).suggest(command)
                );

                // Inform
                msg("%s <i>is not invited to %s<i>.", mplayer.describeTo(msender, true), msenderFaction.describeTo(mplayer));
                message(invite);
            }
        }

        // Inform Faction if all
        if (all)
        {
            List<String> names = new ArrayList<>();
            for (MPlayer mplayer : mplayers)
            {
                names.add(mplayer.describeTo(msender, true));
            }

            Mson factionsRevokeAll = mson(
                    Mson.parse("%s<i> revoked ", msender.describeTo(msenderFaction)),
                    Mson.parse("<i>all <h>%s <i>pending invitations", mplayers.size()).tooltip(names),
                    mson(" from your faction.").color(ChatColor.YELLOW)
            );

            msenderFaction.sendMessage(factionsRevokeAll);
            msenderFaction.changed();
        }
    }

}
