package com.massivecraft.factions.cmd.alt;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CmdFactionsAltDeinvite extends FactionsCommand
{
   // -------------------------------------------- //
   // CONSTRUCT
   // -------------------------------------------- //

    public CmdFactionsAltDeinvite()
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
       // Args
       Set<MPlayer> mplayers = new HashSet<>();
       boolean all = false;

       if ("all".equalsIgnoreCase(this.argAt(0)))
       {
          Set<String> ids = msenderFaction.getInvitations().keySet();

          // Doesn't show up if list is empty. Test at home if it worked.
          if (ids == null || ids.isEmpty())
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

       if (!MPerm.getPermAlt().has(msender, msenderFaction, true)) return;

       Set<MPlayer> playersToRemove = new HashSet<>();
       for (MPlayer mPlayer : mplayers)
       {
          if (!msenderFaction.isInvitedAlt(mPlayer))
          {
             msg("%s <b>is invited as a member. Use /f deinvite instead.", mPlayer.describeTo(msender));
             playersToRemove.add(mPlayer);
          }
       }

       mplayers.removeAll(playersToRemove);

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
             msg("%s<i> is already an alt of %s<i>.", mplayer.getName(), msenderFaction.getName(msender));
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
             mplayer.msg("%s<i> revoked your invitation as an alt to <h>%s<i>.", msender.describeTo(mplayer, true), msenderFaction.describeTo(mplayer));

             // Inform Faction
             if ( ! all)
             {
                msenderFaction.msg("%s<i> revoked %s's<i> invitation as an alt.", msender.describeTo(msenderFaction), mplayer.describeTo(msenderFaction));
             }

             // Apply
             msenderFaction.uninvite(mplayer);

             // If all, we do this at last. So we only do it once.
             if (! all) msenderFaction.changed();
          }
          else
          {
             // Mson
             String command = CmdFactions.get().cmdFactionsAlt.cmdFactionsAltInvite.getCommandLine(mplayer.getName());
             String tooltip = Txt.parse("Click to <c>%s<i>.", command);

             Mson invite = Mson.mson(
                     mson("You might want to invite him as an alt. ").color(ChatColor.YELLOW),
                     mson(ChatColor.GREEN.toString() + tooltip).tooltip(ChatColor.YELLOW.toString() + tooltip).suggest(command)
             );

             // Inform
             msg("%s <i>is not invited to %s<i> as an alt.", mplayer.describeTo(msender, true), msenderFaction.describeTo(mplayer));
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
                  Mson.parse("<i>all <h>%s <i>pending alt invitations", mplayers.size()).tooltip(names),
                  mson(" from your faction.").color(ChatColor.YELLOW)
          );

          msenderFaction.sendMessage(factionsRevokeAll);
          msenderFaction.changed();
       }
    }


}
