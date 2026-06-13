package com.massivecraft.factions.entity.object;

import org.bukkit.inventory.ItemStack;

public class ChestAction
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String playerId;
    public String getPlayerId() { return playerId; }

    private final long timestamp;
    public long getTimestamp() { return timestamp; }

    private final ItemStack item;
    public ItemStack getItem() { return item; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ChestAction(String playerId, long timestamp, ItemStack item)
    {
        this.playerId = playerId;
        this.timestamp = timestamp;
        this.item = item;
    }

}
