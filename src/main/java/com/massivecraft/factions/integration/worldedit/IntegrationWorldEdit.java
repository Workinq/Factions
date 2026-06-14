package com.massivecraft.factions.integration.worldedit;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.Integration;

public class IntegrationWorldEdit extends Integration
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //

	private static final IntegrationWorldEdit i = new IntegrationWorldEdit();
	public static IntegrationWorldEdit get() { return i; }
	private IntegrationWorldEdit()
	{
		this.setPluginName("WorldEdit");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Engine getEngine()
	{
		return EngineWorldEdit.get();
	}

}
