package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
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
    private final String fromText, toText;

    public ActionClickShield(int from, Faction faction, MPlayer mplayer, String fromText, String toText)
    {
        this.from = from;
        this.faction = faction;
        this.mplayer = mplayer;
        this.fromText = fromText;
        this.toText = toText;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        player.openInventory(this.getConfirmGui());
        return true;
    }

    private Inventory getConfirmGui()
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, 27, Txt.parse("<gray>Confirm Shield"));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);

        // Items
        chestGui.getInventory().setItem(11, new ItemBuilder(Material.STAINED_GLASS_PANE).name(Txt.parse("<g>Confirm")).durability(13));
        chestGui.setAction(11, new ActionConfirmShield(from, faction, mplayer, fromText, toText));
        chestGui.getInventory().setItem(13, new ItemBuilder(Material.WATCH).name(" ").addLore(Txt.parse("<g>Click to change your shielded hours to")).addLore(Txt.parse("<k>%s <white>---> <k>%s <n>(<k>" + MConf.get().shieldHours + " hours total<n>)", fromText, toText)));
        chestGui.getInventory().setItem(15, new ItemBuilder(Material.STAINED_GLASS_PANE).name(Txt.parse("<b>Cancel")).durability(14));
        chestGui.setAction(15, event -> true);

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory());

        return chestGui.getInventory();
    }

}
