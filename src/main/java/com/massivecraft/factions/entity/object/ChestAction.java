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

    private final int page;
    public int getPage() { return Math.max(page, 1); }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ChestAction(String playerId, long timestamp, ItemStack item, int page)
    {
        this.playerId = playerId;
        this.timestamp = timestamp;
        this.item = item;
        this.page = page;
    }

}
