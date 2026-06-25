package com.massivecraft.factions.integration.placeholderapi;

import com.massivecraft.massivecore.Integration;

public class IntegrationPlaceholderAPI extends Integration
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final IntegrationPlaceholderAPI i = new IntegrationPlaceholderAPI();
    public static IntegrationPlaceholderAPI get() { return i; }
    private IntegrationPlaceholderAPI()
    {
        this.setPluginName("PlaceholderAPI");
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private FactionsExpansion expansion = null;

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void setIntegrationActiveInner(boolean active)
    {
        if (active)
        {
            this.expansion = new FactionsExpansion();
            this.expansion.register();
        }
        else if (this.expansion != null)
        {
            this.expansion.unregister();
            this.expansion = null;
        }
    }

}
