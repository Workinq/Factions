package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.store.EntityInternal;

public class Invitation extends EntityInternal<Invitation>
{
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public Invitation load(Invitation that)
	{
		this.inviterId = that.inviterId;
		this.creationMillis = that.creationMillis;
		this.alt = that.alt;
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String inviterId;
	public String getInviterId() { return inviterId; }
	public void setInviterId(String inviterId) { this.inviterId = inviterId; }
	
	private Long creationMillis;
	public Long getCreationMillis() { return creationMillis; }
	public void setCreationMillis(Long creationMillis) { this.creationMillis = creationMillis; }

	private Boolean alt;
	public Boolean isAlt() { return alt; }
	public void setAlt(Boolean alt) { this.alt = alt; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Invitation()
	{
		this(null, null, null);
	}
	
	public Invitation(String inviterId, Long creationMillis, Boolean alt)
	{
		this.inviterId = inviterId;
		this.creationMillis = creationMillis;
		this.alt = alt;
	}
	
}
