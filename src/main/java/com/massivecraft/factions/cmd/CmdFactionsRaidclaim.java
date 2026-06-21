package com.massivecraft.factions.cmd;

import com.massivecraft.factions.ClaimType;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import java.util.Collections;
import java.util.LinkedHashMap;

public class CmdFactionsRaidclaim extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public CmdFactionsRaidclaimRemove cmdFactionsRaidclaimRemove = new CmdFactionsRaidclaimRemove();

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRaidclaim()
	{
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(RequirementHasPerm.get(Perm.RAIDCLAIM));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		if ( ! MConf.get().raidClaimsEnabled) throw new MassiveException().setMsg("<b>Raid claims are disabled.");

		Faction faction = msenderFaction;
		if ( ! faction.isNormal()) throw new MassiveException().setMsg("<b>You must be in a faction to place a raid claim.");

		PS chunk = PS.valueOf(me.getLocation()).getChunk(true);
		if ( ! BoardColl.get().getFactionAt(chunk).isNone()) throw new MassiveException().setMsg("<b>Raid claims can only be placed on wilderness.");

		long expiryMillis = System.currentTimeMillis() + MConf.get().raidClaimExpiryMillis;
		if ( ! msender.tryClaim(faction, Collections.singleton(chunk), null, null, ClaimType.RAID, expiryMillis)) return;

		LinkedHashMap<TimeUnit, Long> unitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(MConf.get().raidClaimExpiryMillis, TimeUnit.getAllButMillis()), 2);
		msg("<i>This raid claim will expire in <h>%s<i>.", TimeDiffUtil.formatedMinimal(unitcounts, "<h>"));
	}

}
