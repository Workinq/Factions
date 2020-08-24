package com.massivecraft.factions.cmd.alt;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.object.Invitation;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

import java.util.Collection;

public class CmdFactionsAltInvite extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsAltInvite()
    {
        // Aliases
        this.addAliases("invite");

        // Desc
        this.setDescPermission("factions.alt.invite");

        // Parameters
        this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Collection<MPlayer> mplayers = this.readArg();

        String senderId = IdUtil.getId(sender);
        long creationMillis = System.currentTimeMillis();

        // MPerm
        if (!MPerm.getPermAlt().has(msender, msenderFaction, true)) return;

        for (MPlayer mplayer : mplayers)
        {
            // Already member?
            if (mplayer.getFaction() == msenderFaction)
            {
                msg("%s<i> is already an alt of %s<i>.", mplayer.getName(), msenderFaction.getName(msender));
                continue;
            }

            // Already invited?
            boolean isInvited = msenderFaction.isInvited(mplayer);

            if ( ! isInvited)
            {
                // Event
                EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
                event.run();
                if (event.isCancelled()) continue;
                isInvited = event.isNewInvited();

                // Inform
                mplayer.msg("%s<i> invited you as an alt to %s<i>.", msender.describeTo(mplayer, true), msenderFaction.describeTo(mplayer));
                msenderFaction.msg("%s<i> invited %s<i> as an alt to your faction.", msender.describeTo(msenderFaction, true), mplayer.describeTo(msenderFaction));

                // Apply
                Invitation invitation = new Invitation(senderId, creationMillis, true);
                msenderFaction.invite(mplayer.getId(), invitation);
                msenderFaction.changed();

                // Log
                if (MConf.get().logFactionInvite)
                {
                    Factions.get().log(msender.getDisplayName(IdUtil.getConsole()) + " invited " + mplayer.getName() + " as an alt to the faction " + msenderFaction.getName());
                }
            }
            else
            {
                // Mson
                String command = CmdFactions.get().cmdFactionsAlt.cmdFactionsAltDeinvite.getCommandLine(mplayer.getName());
                String tooltip = Txt.parse("<i>Click to <c>%s<i>.", command);

                Mson remove = Mson.mson(
                        mson("You might want to remove him. ").color(ChatColor.YELLOW),
                        mson("Click to " + command).color(ChatColor.RED).tooltip(tooltip).suggest(command)
                );

                // Inform
                msg("%s <i>is already invited as an alt to %s<i>.", mplayer.getName(), msenderFaction.getName(msender));
                message(remove);
            }
        }
    }

}
