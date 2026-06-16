package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.StandardGui;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShieldSelectorMenu extends StandardGui
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;

    // Inner-grid slots for rows 1..4 (cols 1..7) of a 6-row chest.
    private static final int[] HOUR_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43,
    };

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ShieldSelectorMenu(Player player, Faction faction)
    {
        super(player, 6, "<gray>Faction Shield");
        this.faction = faction;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        fillBorder();

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String now = dateFormat.format(Calendar.getInstance().getTime());
        Calendar calendar = getFreshCalendar();

        for (int hourIndex = 0; hourIndex <= 23; hourIndex++)
        {
            int slot = HOUR_SLOTS[hourIndex];
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            String from = getTime(calendar);
            Calendar clone = (Calendar) calendar.clone();
            clone.add(Calendar.HOUR_OF_DAY, MConf.get().shieldHours);
            String to = getTime(clone);
            String fromTo = Txt.parse("<k>%s <white>---> <k>%s <n>(<k>" + MConf.get().shieldHours + " hours total<n>)", from, to);

            if (faction.isShieldedAtHour(hour))
            {
                set(slot, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name(" ")
                    .addLore("")
                    .addLore(Txt.parse("<g>Your shielded hours are currently"))
                    .addLore(fromTo)
                    .addLore("")
                    .addLore(Txt.parse("<n>Current Time: <k>%s", now)));
            }
            else
            {
                button(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(" ")
                    .addLore("")
                    .addLore(Txt.parse("<g>Click to change your shield hours to"))
                    .addLore(fromTo)
                    .addLore("")
                    .addLore(Txt.parse("<n>Current Time: <k>%s", now)), ctx -> {
                    MPlayer mplayer = MPlayer.get(ctx.getPlayer());
                    if ( ! MPerm.getPermShield().has(mplayer, faction, true)) return;
                    new ShieldConfirmMenu(ctx.getPlayer(), faction, mplayer, hour).open();
                });
            }

            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        set(48, new ItemBuilder(Material.CLOCK).name(Txt.parse("<k>Current Time")).withLore(Txt.parse(MUtil.list(
            "", Txt.parse("<n>It is currently <k>%s", now)))));

        set(50, new ItemBuilder(Material.PAPER).name(Txt.parse("<k>Shield Information")).withLore(Txt.parse(MUtil.list(
            "",
            "<white>Whilst a shield is active, tnt won't",
            "<white>explode within the faction's base region",
            "",
            "<b>Abuse of this mechanic in any way will be",
            "<b>punished severely"))));

        button(53, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
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
