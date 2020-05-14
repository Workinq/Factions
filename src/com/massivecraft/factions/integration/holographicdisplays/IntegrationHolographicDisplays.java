package com.massivecraft.factions.integration.holographicdisplays;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.Integration;

public class IntegrationHolographicDisplays extends Integration
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static IntegrationHolographicDisplays i = new IntegrationHolographicDisplays();
    public static IntegrationHolographicDisplays get() { return i; }
    private IntegrationHolographicDisplays()
    {
        this.setPluginName("HolographicDisplays");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Engine getEngine()
    {
        return EngineHolographicDisplays.get();
    }

}
