package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventFactionsSpawnerUpgrade extends EventFactionsAbstractSender
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

    public CreatureSpawner spawner;
    public CreatureSpawner getSpawner() { return spawner; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsSpawnerUpgrade(CommandSender sender, Faction faction, CreatureSpawner spawner)
    {
        super(sender);
        this.faction = faction;
        this.spawner = spawner;
    }

}
