package com.massivecraft.factions.cmd;

import com.massivecraft.factions.ClaimType;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.ps.PS;

import java.util.Set;

public abstract class CmdFactionsSetX extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String formatOne = null;
	public String getFormatOne() { return this.formatOne; }
	public void setFormatOne(String formatOne) { this.formatOne = formatOne; }
	
	private String formatMany = null;
	public String getFormatMany() { return this.formatMany; }
	public void setFormatMany(String formatMany) { this.formatMany = formatMany; }
	
	private boolean claim = true;
	public boolean isClaim() { return this.claim; }
	public void setClaim(boolean claim) { this.claim = claim; }

	private ClaimType claimType = ClaimType.NORMAL;
	public ClaimType getClaimType() { return this.claimType; }
	public void setClaimType(ClaimType claimType) { this.claimType = claimType; }
	
	private int factionArgIndex = 0;
	public int getFactionArgIndex() { return this.factionArgIndex; }
	public void setFactionArgIndex(int factionArgIndex) { this.factionArgIndex = factionArgIndex; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSetX(boolean claim)
	{
		this.setClaim(claim);
		this.setSetupEnabled(false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Args
		final Faction newFaction = this.getNewFaction();
		Set<PS> chunks = this.getChunks();

		Long expiryMillis = null;
		if (this.getClaimType() == ClaimType.RAID)
		{
			if (chunks == null) return;
			if ( ! MConf.get().raidClaimsEnabled) throw new MassiveException().setMsg("<b>Raid claims are disabled.");
			chunks = this.filterWilderness(chunks);
			if (chunks.isEmpty()) throw new MassiveException().setMsg("<b>Raid claims can only be placed on wilderness.");
			expiryMillis = System.currentTimeMillis() + MConf.get().raidClaimExpiryMillis;
		}
		
		// Apply / Inform
		msender.tryClaim(newFaction, chunks, this.getFormatOne(), this.getFormatMany(), this.getClaimType(), expiryMillis);
	}

	private Set<PS> filterWilderness(Set<PS> chunks)
	{
		Set<PS> ret = new MassiveSet<>();
		for (PS chunk : chunks)
		{
			if ( ! BoardColl.get().getFactionAt(chunk).isNone()) continue;
			ret.add(chunk);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Set<PS> getChunks() throws MassiveException;
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public Faction getNewFaction() throws MassiveException
	{
		if (this.isClaim())
		{
			return this.readArgAt(this.getFactionArgIndex(), msenderFaction);
		}
		else
		{
			return FactionColl.get().getNone();
		}
	}
	
}
