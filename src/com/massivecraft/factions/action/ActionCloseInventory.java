package com.massivecraft.factions.action;

import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionCloseInventory extends ChestActionAbstract
{

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        player.closeInventory();
        return true;
    }

}
