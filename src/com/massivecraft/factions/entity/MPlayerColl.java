package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.store.SenderColl;

public class MPlayerColl extends SenderColl<MPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MPlayerColl i = new MPlayerColl();
	public static MPlayerColl get() { return i; }
	public MPlayerColl()
	{
		this.setCleanTaskEnabled(true);
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	public static final Predicate<MPlayer> PREDICATE_ALT = MPlayer::isAlt;
	public static final Predicate<MPlayer> PREDICATE_NALT = MPlayer::isntAlt;

	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public long getCleanInactivityToleranceMillis()
	{
		return MConf.get().cleanInactivityToleranceMillis;
	}

}
