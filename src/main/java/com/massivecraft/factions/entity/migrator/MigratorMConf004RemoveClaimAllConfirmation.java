package com.massivecraft.factions.entity.migrator;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.store.migrator.MigratorRoot;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class MigratorMConf004RemoveClaimAllConfirmation extends MigratorRoot
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static MigratorMConf004RemoveClaimAllConfirmation i = new MigratorMConf004RemoveClaimAllConfirmation();
	public static MigratorMConf004RemoveClaimAllConfirmation get() { return i; }
	private MigratorMConf004RemoveClaimAllConfirmation()
	{
		super(MConf.class);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void migrateInner(JsonObject entity)
	{
		entity.remove("requireConfirmationForClaimUnclaimAll");
	}

}
