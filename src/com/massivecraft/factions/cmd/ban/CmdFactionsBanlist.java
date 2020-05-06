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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class CmdFactionsBanlist extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsBanlist()
	{
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
		final List<Entry<String, FactionBan>> bannedMembers = new MassiveList<>(faction.getBannedMembers().entrySet());
		
		Collections.sort(bannedMembers, new Comparator<Entry<String, FactionBan>>()
		{
			@Override
			public int compare(Entry<String, FactionBan> i1, Entry<String, FactionBan> i2)
			{
				return ComparatorSmart.get().compare(i2.getValue().getCreationMillis(), i1.getValue().getCreationMillis());
			}
		});
		
		final long now = System.currentTimeMillis();
		
		final Pager<Entry<String, FactionBan>> pager = new Pager<>(this, "Banned Members", page, bannedMembers, new Stringifier<Entry<String, FactionBan>>()
		{
			public String toString(Entry<String, FactionBan> entry, int index)
			{
				String bannedId = entry.getKey();
				String bannerId = entry.getValue().getBannerId();
				
				String bannedDisplayName = MixinDisplayName.get().getDisplayName(bannedId, sender);
				String bannerDisplayName = bannerId != null ? MixinDisplayName.get().getDisplayName(bannerId, sender) : Txt.parse("<silver>unknown");
				
				String ageDesc = "";
				if (entry.getValue().getCreationMillis() != null)
				{
					long millis = now - entry.getValue().getCreationMillis();
					LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
					ageDesc = TimeDiffUtil.formatedMinimal(ageUnitcounts, "<i>");
					ageDesc = " " + ageDesc + Txt.parse(" ago");
				}

				return Txt.parse("%s<i> was banned by %s<reset>%s<i>.", bannedDisplayName, bannerDisplayName, ageDesc);
			}
		});
		
		// Pager Message
		pager.message();
	}
	
}
