package com.massivecraft.factions.entity.conf;

import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Material;

import java.util.List;

public class ConfItem extends EntityInternal<ConfItem>
{

    private final int cost;
    private final List<String> commands;
    private final Material itemType;
    private final String itemName;
    private final int itemData;
    private final List<String> itemLore;

    public ConfItem(int cost, List<String> commands, Material itemType, String itemName, int itemData, List<String> itemLore)
    {
        this.cost = cost;
        this.commands = commands;
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemData = itemData;
        this.itemLore = itemLore;
    }

    public int getCost()
    {
        return cost;
    }

    public List<String> getCommands()
    {
        return commands;
    }

    public Material getItemType()
    {
        return itemType;
    }

    public String getItemName()
    {
        return itemName;
    }

    public int getItemData()
    {
        return itemData;
    }

    public List<String> getItemLore()
    {
        return itemLore;
    }

}
