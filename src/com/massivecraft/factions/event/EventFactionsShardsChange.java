package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventFactionsShardsChange extends EventFactionsAbstract
{
    // -------------------------------------------- //
    // REQUIRED EVENT CODE
    // -------------------------------------------- //

    private static final HandlerList handlers = new HandlerList();
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    public Faction getFaction() { return this.faction; }

    private int shards;
    public int getShards() { return this.shards; }
    public void setShards(int shards) { this.shards = shards; }

    private final int oldShards;
    public int getOldShards() { return this.oldShards; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsShardsChange(Faction faction, int shards)
    {
        super(false);
        this.faction = faction;
        this.shards = shards;
        this.oldShards = faction.getShards();
    }

}
