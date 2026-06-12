package com.massivecraft.factions;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.util.extractor.Extractor;

public class ExtractorFactionAccountName implements Extractor
{

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static ExtractorFactionAccountName i = new ExtractorFactionAccountName();
    public static ExtractorFactionAccountName get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE: EXTRACTOR
    // -------------------------------------------- //

    @Override
    public Object extract(Object o)
    {
        if (o instanceof Faction)
        {
            String factionId = ((Faction)o).getId();
            if (factionId == null) return null;
            return Factions.FACTION_MONEY_ACCOUNT_ID_PREFIX + factionId;
        }

        return null;
    }

}
