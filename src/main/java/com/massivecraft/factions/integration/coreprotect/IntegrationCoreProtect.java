package com.massivecraft.factions.integration.coreprotect;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.Integration;

public class IntegrationCoreProtect extends Integration
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static IntegrationCoreProtect i = new IntegrationCoreProtect();
    public static IntegrationCoreProtect get() { return i; }
    private IntegrationCoreProtect()
    {
        this.setPluginName("CoreProtect");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Engine getEngine()
    {
        return EngineCoreProtect.get();
    }

}
