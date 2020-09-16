package com.massivecraft.factions.cmd.shield;

import com.massivecraft.factions.action.shield.ActionShieldClick;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MOption;
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
        // Aliases
        this.addAliases("set");

        // Desc
        this.setDescPermission("factions.shield.set");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg(msenderFaction);

        if ( ! MOption.get().isGrace() && faction.hasShield() && ! msender.isOverriding() )
        {
            throw new MassiveException().setMsg("<b>You can't change your faction shield as grace has been disabled.");
        }

        if ( ! MPerm.getPermShield().has(msender, faction, true) ) return;

        me.openInventory(getShieldGui(faction));
    }

    private Inventory getShieldGui(Faction faction)
    {
        Inventory inventory = Bukkit.createInventory(null, 45, Txt.parse("<gray>Faction Shield"));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);

        // Args
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String now = dateFormat.format(Calendar.getInstance().getTime());
        Calendar calendar = this.getFreshCalendar();

        // Time Items
        for (int i = 0; i <= 23; i++)
        {
            // Args
            String from = this.getTime(calendar);
            Calendar clone = (Calendar) calendar.clone();
            clone.add(Calendar.HOUR_OF_DAY, MConf.get().shieldHours);
            String to = this.getTime(clone);
            String fromTo = Txt.parse("<k>%s <white>---> <k>%s <n>(<k>" + MConf.get().shieldHours + " hours total<n>)", from, to);

            if (faction.isShieldedAtHour(calendar.get(Calendar.HOUR_OF_DAY)))
            {
                chestGui.getInventory().setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(13).addLore("").addLore(Txt.parse("<g>Your shielded hours are currently")).addLore(fromTo).addLore("").addLore(Txt.parse("<n>Current Time: <k>%s", now)));
            }
            else
            {
                chestGui.getInventory().setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(14).addLore("").addLore(Txt.parse("<g>Click to change your shield hours to")).addLore(fromTo).addLore("").addLore(Txt.parse("<n>Current Time: <k>%s", now)));
                chestGui.setAction(i, new ActionShieldClick(calendar.get(Calendar.HOUR_OF_DAY), faction, msender, from, to));
            }

            // Increment
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        // Filler Barriers
        for (int i = 24; i <= 26; i++)
        {
            chestGui.getInventory().setItem(i, new ItemBuilder(Material.BARRIER).name(" "));
        }

        // Information
        chestGui.getInventory().setItem(40, new ItemBuilder(Material.PAPER).name(Txt.parse("<k>Shield Information")).setLore(Txt.parse(MUtil.list("", "<white>Whilst a shield is active, tnt won't", "<white>explode within the faction's base region", "", "<b>Abuse of this mechanic in any way will be", "<b>punished severely"))));

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

    public String getTime(Calendar calendar)
    {
        if (calendar.get(Calendar.HOUR_OF_DAY) == 0) return "Midnight";
        if (calendar.get(Calendar.HOUR_OF_DAY) == 12) return "Midday";

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(calendar.getTime());
    }

    public Calendar getFreshCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);
        return calendar;
    }

}
