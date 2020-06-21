package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ActionClickShield extends ChestActionAbstract
{

    private final int from;
    private final Faction faction;
    private final MPlayer mplayer;
    private final String time;

    public ActionClickShield(int from, Faction faction, MPlayer mplayer, String time)
    {
        this.from = from;
        this.faction = faction;
        this.mplayer = mplayer;
        this.time = time;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        player.openInventory(this.getConfirmGui());
        return true;
    }

    private Inventory getConfirmGui()
    {
        Inventory inventory = Bukkit.createInventory(null, 27, Txt.parse("<gray>Confirm Shield"));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        chestGui.getInventory().setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).name(Txt.parse("<g>Confirm")).durability(13));
        chestGui.setAction(11, new ActionConfirmShield(from, faction, mplayer, time));
        chestGui.getInventory().setItem(13, new ItemBuilder(Material.WATCH).name(" ").addLore(Txt.parse("<g>Click to change your shielded hours to")).addLore(time));
        chestGui.getInventory().setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).name(Txt.parse("<b>Cancel")).durability(14));
        chestGui.setAction(15, event -> true);

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        return chestGui.getInventory();
    }

}
