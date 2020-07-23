package com.massivecraft.factions.task;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ModuloRepeatTask;

public class TaskRemindBaseRegion extends ModuloRepeatTask
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static TaskRemindBaseRegion i = new TaskRemindBaseRegion();
    public static TaskRemindBaseRegion get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public long getDelayMillis()
    {
        return super.getDelayMillis();
    }

    @Override
    public void invoke(long now)
    {
        // Loop
        for (Faction faction : FactionColl.get().getAll())
        {
            // Verify
            if (faction.isSystemFaction()) continue;
            if (faction.hasBaseRegion()) continue;

            // Inform
            faction.msg("<b><bold>REMINDER: <reset><white>Your faction does not have a base region set. Please use /f setbaseregion with your selection to be protected during shielded hours.");
        }
    }

}
