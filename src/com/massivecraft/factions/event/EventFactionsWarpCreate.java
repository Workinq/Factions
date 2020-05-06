package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.FactionWarp;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventFactionsWarpCreate extends EventFactionsAbstractSender
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

    private FactionWarp newWarp;
    public FactionWarp getNewWarp() { return this.newWarp; }
    public void setNewWarp(FactionWarp newWarp) { this.newWarp = newWarp; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsWarpCreate(CommandSender sender, Faction faction, FactionWarp newWarp)
    {
        super(sender);
        this.faction = faction;
        this.newWarp = newWarp;
    }

}
