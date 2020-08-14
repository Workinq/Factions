package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.comparator.ComparatorMPlayerInactivity;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

import java.util.List;

public class CmdFactionsJoin extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsJoin()
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
		if ( ! samePlayer  && ! Perm.JOIN_OTHERS.has(sender, false))
		{
			throw new MassiveException().setMsg("<b>You do not have permission to move other players into a faction.");
		}

		if (faction == mplayerFaction)
		{
			String command = CmdFactions.get().cmdFactionsKick.getCommandLine(mplayer.getName());

			// Mson creation
			Mson alreadyMember = Mson.mson(
				Mson.parse(mplayer.describeTo(msender, true)),
				mson((samePlayer ? " are" : " is") + " already a member of " + faction.getName(msender) + ".").color(ChatColor.YELLOW)
			);

			message(alreadyMember.suggest(command).tooltip(Txt.parse("<i>Click to <c>%s<i>.", command)));
			return;
		}

		if (MConf.get().factionMemberLimit > 0 && faction.getMPlayersWhere(mp -> ! mp.isAlt()).size() >= MConf.get().factionMemberLimit && ! mplayer.isOverriding() )
		{
			throw new MassiveException().setMsg("<b>The faction %s <b>is at the limit of <h>%d <b>members, so %s <b>cannot currently join.", faction.getName(msender), MConf.get().factionMemberLimit, mplayer.describeTo(msender));
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

		if ( ! MConf.get().canLeaveWithNegativePower && mplayer.getPower() < 0 && ! mplayer.isOverriding() )
		{
			throw new MassiveException().setMsg("%s <b>cannot join a faction with a negative power level.", mplayer.describeTo(msender, true));
		}

		if (faction.isBanned(mplayer) && ! mplayer.isOverriding() )
		{
			throw new MassiveException().setMsg("%s <b>cannot join a faction when banned from it.", mplayer.describeTo(msender, true));
		}

		if (faction.isInvitedAlt(mplayer) && ! mplayer.isOverriding() )
		{
			throw new MassiveException().setMsg("%s <b>can't join %s <b>as a member, use /f alt join instead.", mplayer.describeTo(msender, true), faction.describeTo(msender));
		}

		if ( ! faction.isInRoster(mplayer) && ! mplayer.isOverriding() )
		{
			throw new MassiveException().setMsg("%s <b>must be added to %s's <b>roster to join.", mplayer.describeTo(msender, true), faction.describeTo(msender));
		}

		if (faction.isFull())
		{
			// Args
			List<MPlayer> mplayers = faction.getMPlayersWhereOnline(false);
			if (mplayers.isEmpty())
			{
				throw new MassiveException().setMsg("<b>There are no players that can be rotated out of the faction.");
			}

			// Sort by inactivity
			mplayers.sort(new ComparatorMPlayerInactivity());

			MPlayer toKick = null;
			for (MPlayer member : mplayers)
			{
				if (member.getRole().isMoreThan(mplayer.getRole())) continue;
				if (member.getRole() == mplayer.getRole()) continue;

				toKick = member;
				break;
			}

			if (toKick == null)
			{
				throw new MassiveException().setMsg("<b>There are no players that can be rotated out of the faction.");
			}

			// Event
			EventFactionsMembershipChange event = new EventFactionsMembershipChange(sender, toKick, FactionColl.get().getNone(), MembershipChangeReason.KICK);
			event.run();
			if (event.isCancelled())
			{
				throw new MassiveException().setMsg("<b>There are no players that can be rotated out of the faction.");
			}

			// Inform
			faction.msg("%s <i>has been rotated out of <g>your faction's <i>roster.", toKick.describeTo(faction, true), faction.describeTo(faction));

			// Apply
			faction.uninvite(toKick);
			toKick.resetFactionData();
		}

		/*if ( ! (faction.getFlag(MFlag.getFlagOpen()) || faction.isInvited(mplayer) || msender.isOverriding()))
		{
			msg("<i>This faction requires invitation.");
			if (samePlayer)
			{
				faction.msg("%s<i> tried to join your faction.", mplayer.describeTo(faction, true));
			}
			return;
		}*/

		// Event
		EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.JOIN);
		membershipChangeEvent.run();
		if (membershipChangeEvent.isCancelled()) return;

		// Inform
		if (!samePlayer)
		{
			mplayer.msg("<i>%s <i>moved you into the faction %s<i>.", msender.describeTo(mplayer, true), faction.getName(mplayer));
		}
		faction.msg("<i>%s <i>joined <lime>your faction<i>.", mplayer.describeTo(faction, true));
		msg("<i>%s <i>successfully joined %s<i>.", mplayer.describeTo(msender, true), faction.getName(msender));

		// Apply
		mplayer.resetFactionData();
		mplayer.setFaction(faction);
		mplayer.setRole(faction.getRosterRole(mplayer));

		// Uninvite
		faction.uninvite(mplayer);

		// Log
		if (MConf.get().logFactionJoin)
		{
			if (samePlayer)
			{
				Factions.get().log(Txt.parse("%s joined the faction %s.", mplayer.getName(), faction.getName()));
			}
			else
			{
				Factions.get().log(Txt.parse("%s moved the player %s into the faction %s.", msender.getName(), mplayer.getName(), faction.getName()));
			}
		}
	}

}
