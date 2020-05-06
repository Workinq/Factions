package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.conf.ConfItem;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionPurchaseItem extends ChestActionAbstract
{

    private final Faction faction;
    private final MPlayer mPlayer;
    private final ConfItem item;

    public ActionPurchaseItem(Faction faction, MPlayer mPlayer, ConfItem item)
    {
        this.faction = faction;
        this.mPlayer = mPlayer;
        this.item = item;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        int cost = item.getCost();
        if (faction.getShards() - cost < 0)
        {
            mPlayer.msg("<b>Your faction does not have %,d shards to purchase that.", cost);
            return true;
        }

        // Apply
        faction.takeShards(cost);

        // Perform
        for (String command : item.getCommands())
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", mPlayer.getName()).replace("%faction%", faction.getName()));
        }
        return true;
    }

}
