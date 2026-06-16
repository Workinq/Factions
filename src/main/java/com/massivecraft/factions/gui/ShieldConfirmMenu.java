package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.StandardGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShieldConfirmMenu extends StandardGui
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final MPlayer mplayer;
    private final int hour;
    private final String fromText;
    private final String toText;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ShieldConfirmMenu(Player player, Faction faction, MPlayer mplayer, int hour)
    {
        super(player, 3, "<gray>Confirm Shield");
        this.faction = faction;
        this.mplayer = mplayer;
        this.hour = hour;

        Calendar calendar = getFreshCalendar();
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        this.fromText = getTime(calendar);
        Calendar clone = (Calendar) calendar.clone();
        clone.add(Calendar.HOUR_OF_DAY, MConf.get().shieldHours);
        this.toText = getTime(clone);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        fillBorder();

        button(11, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name(Txt.parse("<g>Confirm")), ctx -> {
            faction.setShieldedHour(hour);
            faction.msg("%s <i>set the faction shield from <h>%s <i>to <h>%s<i>.", mplayer.describeTo(faction, true), fromText, toText);
            ctx.getPlayer().closeInventory();
        });

        set(13, new ItemBuilder(Material.CLOCK).name(" ")
            .addLore(Txt.parse("<g>Click to change your shielded hours to"))
            .addLore(Txt.parse("<k>%s <white>---> <k>%s <n>(<k>" + MConf.get().shieldHours + " hours total<n>)", fromText, toText)));

        button(15, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(Txt.parse("<b>Cancel")), ctx -> ctx.getPlayer().closeInventory());
    }

    // -------------------------------------------- //
    // TIME HELPERS
    // -------------------------------------------- //

    private String getTime(Calendar calendar)
    {
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0) return "Midnight";
        if (calendar.get(Calendar.HOUR_OF_DAY) == 12) return "Midday";

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(calendar.getTime());
    }

    private Calendar getFreshCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);
        return calendar;
    }
}
