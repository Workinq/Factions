package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.gui.AuditMenu;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsAuditGui extends FactionsCommand
{
	public CmdFactionsAuditGui()
	{
		this.addParameter(TypeFaction.get(), "faction", "you");
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
	}

	@Override
	public void perform() throws MassiveException
	{
		Faction faction = this.readArg(msenderFaction);
		boolean own = faction == msenderFaction;

		if (own)
		{
			if ( ! MPerm.getPermAudit().has(msender, faction, true)) return;
		}
		else
		{
			if ( ! Perm.AUDIT_ADMIN.has(sender, true)) return;
		}

		new AuditMenu(me, faction, false).open();
	}
}
