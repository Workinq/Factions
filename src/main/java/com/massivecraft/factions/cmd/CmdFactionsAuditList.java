package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.AuditEntry;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.factions.util.AuditFormat;
import com.massivecraft.factions.util.AuditUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import java.util.List;

public class CmdFactionsAuditList extends FactionsCommand
{
	public CmdFactionsAuditList()
	{
		this.addParameter(Parameter.getPage());
		this.addParameter(TypeFaction.get(), "faction", "you");
		this.addParameter(null, new TypeEnum<>(AuditCategory.class), "category", false);
		this.addParameter(null, TypeMPlayer.get(), "player", false);
		this.addParameter(0, TypeInteger.get(), "days", false);
	}

	@Override
	public void perform() throws MassiveException
	{
		int page = this.readArg();
		Faction faction = this.readArg(msenderFaction);
		AuditCategory category = this.readArg();
		MPlayer player = this.readArg();
		int days = this.readArg();

		if (faction == msenderFaction)
		{
			if ( ! msender.hasFaction()) throw new MassiveException().setMsg("<b>You are not in a faction.");
			if ( ! MPerm.getPermAudit().has(msender, msenderFaction, true)) return;
		}
		else if ( ! Perm.AUDIT_ADMIN.has(sender, true))
		{
			return;
		}

		List<AuditEntry> entries = AuditUtil.query(faction, category, player, days);

		Pager<AuditEntry> pager = new Pager<>(this, "Audit Log", page, entries, new Stringifier<AuditEntry>()
		{
			@Override
			public String toString(AuditEntry entry, int index)
			{
				return Txt.parse("<i>%s <i>by %s <silver>(%s ago)",
					AuditFormat.describe(entry, sender), AuditFormat.actorName(entry, sender), AuditFormat.age(entry));
			}
		});
		pager.message();
	}
}
