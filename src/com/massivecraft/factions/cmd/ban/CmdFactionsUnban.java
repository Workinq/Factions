package com.massivecraft.factions.cmd.ban;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsBanChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CmdFactionsUnban extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdFactionsUnban()
    {
        // Parameters
        this.addParameter(TypeSet.get(TypeMPlayer.get()), "players/all", true);
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
            if (ids == null || ids.isEmpty())
            {
                throw new MassiveException().addMsg("<b>No one is banned from your faction.");
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
        if ( ! MPerm.getPermBan().has(msender, msenderFaction, true)) return;

        for (MPlayer mplayer : mplayers)
        {
            // Already banned?
            boolean isBanned = msenderFaction.isBanned(mplayer);

            if (isBanned)
            {
                // Event
                EventFactionsBanChange event = new EventFactionsBanChange(sender, mplayer, msenderFaction, isBanned);
                event.run();
                if (event.isCancelled()) continue;
                isBanned = event.isNewBanned();

                // Inform Player
                mplayer.msg("%s<i> unbanned you from <h>%s<i>.", msender.describeTo(mplayer, true), msenderFaction.describeTo(mplayer));

                // Inform Faction
                if ( ! all)
                {
                    msenderFaction.msg("%s<i> unbanned %s's<i> from the faction.", msender.describeTo(msenderFaction), mplayer.describeTo(msenderFaction));
                }

                // Apply
                msenderFaction.unban(mplayer);

                // Log
                if (MConf.get().logFactionUnban)
                {
                    Factions.get().log(msender.getDisplayName(IdUtil.getConsole()) + " unbanned " + mplayer.getName() + " from the faction " + msenderFaction.getName());
                }

                // If all, we do this at last. So we only do it once.
                if (! all) msenderFaction.changed();
            }
            else
            {
                // Mson
                String command = CmdFactions.get().cmdFactionsBan.getCommandLine(mplayer.getName());
                String tooltip = Txt.parse("Click to <c>%s<i>.", command);

                Mson invite = Mson.mson(
                        mson("You might want to ban him. ").color(ChatColor.YELLOW),
                        mson(ChatColor.GREEN.toString() + tooltip).tooltip(ChatColor.YELLOW.toString() + tooltip).suggest(command)
                );

                // Inform
                msg("%s <i>is not banned from %s<i>.", mplayer.describeTo(msender, true), msenderFaction.describeTo(mplayer));
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

            Mson factionsUnbanAll = mson(
                    Mson.parse("%s<i> unbanned ", msender.describeTo(msenderFaction)),
                    Mson.parse("<i>all <h>%s <i>players", mplayers.size()).tooltip(names),
                    mson(" from your faction.").color(ChatColor.YELLOW)
            );

            msenderFaction.sendMessage(factionsUnbanAll);
            msenderFaction.changed();
        }
    }

}
