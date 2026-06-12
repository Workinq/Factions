package com.massivecraft.factions.integration.mobextras;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.Integration;

public class IntegrationMobExtras extends Integration
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static IntegrationMobExtras i = new IntegrationMobExtras();
    public static IntegrationMobExtras get() { return i; }
    private IntegrationMobExtras()
    {
        this.setPluginName("MobExtras");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Engine getEngine()
    {
        return EngineMobExtras.get();
    }

}
