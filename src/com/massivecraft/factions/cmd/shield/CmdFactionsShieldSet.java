package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.action.ActionClickShield;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CmdFactionsShieldSet extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShieldSet()
    {
        // Requirements
        this.addRequirements(ReqHasFaction.get());
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MConf.get().graceEnabled)
        {
            msg("<b>You can't set your faction shield as grace has been disabled.");
            return;
        }

        if ( ! MPerm.getPermShield().has(msender, msenderFaction, true)) return;

        me.openInventory(getShieldGui());
    }

    private Inventory getShieldGui()
    {
        Inventory inventory = Bukkit.createInventory(null, 45, Txt.parse("<gray>Faction Shield"));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        // Args
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String now = dateFormat.format(Calendar.getInstance().getTime());
        Calendar calendar = getFreshCalendar();

        // Time Items
        for (int i = 0; i <= 23; i++)
        {
            // Args
            String from = getTime(calendar);
            Calendar clone = (Calendar) calendar.clone();
            clone.add(Calendar.HOUR_OF_DAY, 10);
            String to = getTime(clone);
            String fromTo = Txt.parse("<k>%s <white>---> <k>%s <n>(<k>10 hours total<n>)", from, to);

            if (msenderFaction.isShieldedAtHour(calendar.get(Calendar.HOUR_OF_DAY)))
            {
                chestGui.getInventory().setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(13).addLore("").addLore(Txt.parse("<g>Your shielded hours are currently")).addLore(fromTo).addLore("").addLore(Txt.parse("<n>Current Time: <k>%s", now)));
            }
            else
            {
                chestGui.getInventory().setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(14).addLore("").addLore(Txt.parse("<g>Click to change your shield hours to")).addLore(fromTo).addLore("").addLore(Txt.parse("<n>Current Time: <k>%s", now)));
                chestGui.setAction(i, new ActionClickShield(calendar.get(Calendar.HOUR_OF_DAY), msenderFaction, msender, fromTo));
            }

            // Increment
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        // Filler Barriers
        for (int i = 24; i <= 26; i++)
        {
            chestGui.getInventory().setItem(i, new ItemBuilder(Material.BARRIER).name(" "));
        }

        chestGui.getInventory().setItem(40, new ItemBuilder(Material.PAPER).name(Txt.parse("<k>Shield Information")).setLore(Txt.parse(MUtil.list("", "<white>Whilst a shield is active, tnt won't", "<white>explode within the faction's base region", "", "<b>Abuse of this mechanic in any way will be", "<b>punished severely"))));

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

    private String getTime(Calendar calendar)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0) return "Midnight";
        if (calendar.get(Calendar.HOUR_OF_DAY) == 12) return "Midday";
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
