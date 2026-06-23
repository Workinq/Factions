package com.massivecraft.factions.integration.holographicdisplays;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.massivecraft.factions.comparator.ComparatorFactionList;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EngineHolographicDisplays extends Engine
{
    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    public static int LIMIT = 10;

    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static EngineHolographicDisplays i = new EngineHolographicDisplays();
    public static EngineHolographicDisplays get()
    {
        return i;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public List<Faction> topFactions = Collections.EMPTY_LIST;

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Long getPeriod()
    {
        return (long) MConf.get().holographicDisplayUpdatePeriod;
    }

    @Override
    public void setActiveInner(boolean active)
    {
        this.run();

        if (active)
        {
            for (int index = 0; index < LIMIT; index++)
            {
                int finalIndex = index;
                this.register("{factions.top." + index + "}", () -> {
                    try
                    {
                        Faction faction = topFactions.get(finalIndex);
                        if (faction == null) return "ERR";

                        if (faction.isNone())
                        {
                            return Txt.parse("<i>Factionless<i> %d online", faction.getMPlayersWhereOnline(true).size());
                        }

                        int onlineCount = (int) faction.getMPlayersWhereOnline(true).stream().filter(mplayer -> mplayer.isntAlt()).count();
                        int memberCount = faction.getMPlayersWhereAlt(false).size();

                        return Txt.parse(
                                "%s<i> %d/%d online",
                                faction.getName(),
                                onlineCount,
                                memberCount
                        );
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        return "Out of Bounds";
                    }
                });
            }
        }
        else HologramsAPI.unregisterPlaceholders(this.getPlugin());
    }

    // -------------------------------------------- //
    // TASK
    // -------------------------------------------- //

    @Override
    public void run()
    {
        try
        {
            topFactions = FactionColl.get().getAll(ComparatorFactionList.get(null), LIMIT);
        }
        catch (NullPointerException e)
        {
            topFactions = this.getAll();
        }
    }

    private List<Faction> getAll()
    {
        return MUtil.transform(FactionColl.get().getAll(), null, ComparatorFactionList.get(null), LIMIT, 0);
    }

    // -------------------------------------------- //
    // PLACEHOLDERS
    // -------------------------------------------- //

    public void register(String textPlaceholder, PlaceholderReplacer replacer)
    {
        Collection<String> textPlaceholders = HologramsAPI.getRegisteredPlaceholders(this.getPlugin());
        if (textPlaceholders.contains(textPlaceholder)) return;
        HologramsAPI.registerPlaceholder(this.getPlugin(), textPlaceholder, 10, replacer);
    }

}
