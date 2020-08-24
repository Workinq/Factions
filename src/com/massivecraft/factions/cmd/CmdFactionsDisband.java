package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public class CmdFactionsDisband extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDisband()
	{
		// Aliases
		this.setAliases("disband");

		// Desc
		this.setDescPermission("factions.disband");

		// Parameters
		this.addParameter(TypeFaction.get(), "faction", "you");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Faction faction = this.readArg(msenderFaction);
		
		// MPerm
		if ( ! MPerm.getPermDisband().has(msender, faction, true)) return;

		// Verify
		if (faction.getFlag(MFlag.getFlagPermanent()))
		{
			msg("<i>This faction is designated as permanent, so you cannot disband it.");
			return;
		}

		// Event
		EventFactionsDisband event = new EventFactionsDisband(me, faction);
		event.run();
		if (event.isCancelled()) return;
		
		// Run event for each player in the faction
		for (MPlayer mplayer : faction.getMPlayers())
		{
			EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Inform
		for (MPlayer mplayer : faction.getMPlayersWhereOnline(true))
		{
			mplayer.msg("<h>%s<i> disbanded your faction.", msender.describeTo(mplayer));
		}
		
		if (msenderFaction != faction)
		{
			msg("<i>You disbanded <h>%s<i>." , faction.describeTo(msender));
		}

		Inventory inventory = faction.getInventory();
		for (HumanEntity entity : inventory.getViewers())
		{
			entity.closeInventory();
		}
		for (int i = 0; i < inventory.getSize(); i++)
		{
			if (inventory.getItem(i) == null) continue;

			me.getWorld().dropItemNaturally(me.getLocation(), inventory.getItem(i));
			inventory.setItem(i, null);
		}
		msender.msg("<i>As result of disbanding <h>%s<i>, all /f chest contents have been dropped at your feet.", new Object[] { faction.describeTo(this.msender) });
		
		// Log
		if (MConf.get().logFactionDisband)
		{
			Factions.get().log(Txt.parse("<i>The faction <h>%s <i>(<h>%s<i>) was disbanded by <h>%s<i>.", faction.getName(), faction.getId(), msender.getDisplayName(IdUtil.getConsole())));
		}

		// Detach
		faction.detach();
	}
	
}
