package com.massivecraft.factions.cmd.ban;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.object.FactionBan;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import java.util.LinkedHashMap;
import java.util.List;

public class CmdFactionsBanlist extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsBanlist()
	{
		// Aliases
		this.addAliases("bans");

		// Parameters
		this.addParameter(Parameter.getPage());
		this.addParameter(TypeFaction.get(), "faction", "you");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{		
		// Args	
		int page = this.readArg();
		Faction faction = this.readArg(msenderFaction);
		
		if ( faction != msenderFaction && ! Perm.BANLIST_OTHER.has(sender, true)) return;
		
		// MPerm
		if ( ! MPerm.getPermBan().has(msender, msenderFaction, true)) return;
		
		// Pager Create
		final List<FactionBan> bannedMembers = new MassiveList<>(faction.getBannedMembers());
		
		bannedMembers.sort((i1, i2) -> ComparatorSmart.get().compare(i2.getCreationMillis(), i1.getCreationMillis()));
		
		final long now = System.currentTimeMillis();
		
		final Pager<FactionBan> pager = new Pager<>(this, "Banned Members", page, bannedMembers, (Stringifier<FactionBan>) (factionBan, index) -> {
			String bannedId = factionBan.getBannedId();
			String bannerId = factionBan.getBannerId();

			String bannedDisplayName = MixinDisplayName.get().getDisplayName(bannedId, sender);
			String bannerDisplayName = bannerId != null ? MixinDisplayName.get().getDisplayName(bannerId, sender) : Txt.parse("<silver>unknown");

			String ageDesc;
			long millis = now - factionBan.getCreationMillis();
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
			ageDesc = TimeDiffUtil.formatedMinimal(ageUnitcounts, "<i>");
			ageDesc = " " + ageDesc + Txt.parse(" ago");

			return Txt.parse("%s<i> was banned by %s<reset>%s<i>.", bannedDisplayName, bannerDisplayName, ageDesc);
		});
		
		// Pager Message
		pager.message();
	}
	
}
