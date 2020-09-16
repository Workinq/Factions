package com.massivecraft.factions.integration.litebans;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.Integration;

public class IntegrationLitebans extends Integration
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static IntegrationLitebans i = new IntegrationLitebans();
    public static IntegrationLitebans get() { return i; }
    private IntegrationLitebans()
    {
        this.setClassNames(
                "litebans.api.Events"
        );
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Engine getEngine()
    {
        return EngineLitebans.get();
    }

}
