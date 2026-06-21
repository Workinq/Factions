package com.massivecraft.factions.integration.essentials;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.Integration;

public class IntegrationEssentials extends Integration
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final IntegrationEssentials i = new IntegrationEssentials();
    public static IntegrationEssentials get() { return i; }
    private IntegrationEssentials()
    {
        this.setPluginName("Essentials");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Engine getEngine()
    {
        return EngineEssentials.get();
    }

}
