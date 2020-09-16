package com.massivecraft.factions.cmd.ban;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.FactionBan;
import com.massivecraft.factions.event.EventFactionsBanChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

import java.util.Collection;

public class CmdFactionsBan extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsBan()
	{
		// Aliases
		this.setAliases("ban");

		// Desc
		this.setDescPermission("factions.ban");

		// Parameters
		this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);

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
		Collection<MPlayer> mplayers = this.readArg();

		// MPerm
		if ( ! MPerm.getPermBan().has(msender, msenderFaction, true)) return;

		for (MPlayer mplayer : mplayers)
		{
			// Already banned?
			boolean isBanned = msenderFaction.isBanned(mplayer);

			if ( ! isBanned )
			{
				// Already member?
				if (mplayer.getFaction() == msenderFaction)
				{
					CmdFactions.get().cmdFactionsKick.execute(sender, MUtil.list(mplayer.getName()));
				}
				// Event
				EventFactionsBanChange event = new EventFactionsBanChange(sender, mplayer, msenderFaction, isBanned);
				event.run();
				if (event.isCancelled()) continue;
				isBanned = event.isNewBanned();

				// Inform
				mplayer.msg("%s<i> banned you from %s<i>.", msender.describeTo(mplayer, true), msenderFaction.describeTo(mplayer));
				msenderFaction.msg("%s<i> banned %s<i> from the faction.", msender.describeTo(msenderFaction, true), mplayer.describeTo(msenderFaction));

				// Apply
				FactionBan factionBan = new FactionBan(msender.getId(), mplayer.getId(), System.currentTimeMillis());
				msenderFaction.ban(factionBan);

				// Log
				if (MConf.get().logFactionBan)
				{
					Factions.get().log(msender.getDisplayName(IdUtil.getConsole()) + " banned " + mplayer.getName() + " from the faction " + msenderFaction.getName());
				}
			}
			else
			{
				// Mson
				String command = CmdFactions.get().cmdFactionsUnban.getCommandLine(mplayer.getName());
				String tooltip = Txt.parse("<i>Click to <c>%s<i>.", command);

				Mson remove = Mson.mson(
						mson("You might want to unban him. ").color(ChatColor.YELLOW),
						mson("Click to " + command).color(ChatColor.RED).tooltip(tooltip).suggest(command)
				);

				// Inform
				msg("%s <i>is already banned from %s<i>.", mplayer.getName(), msenderFaction.getName(msender));
				message(remove);
			}
		}
	}

}
