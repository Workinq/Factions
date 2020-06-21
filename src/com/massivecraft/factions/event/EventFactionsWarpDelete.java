package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventFactionsWarpDelete extends EventFactionsAbstractSender
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

    private String newWarp;
    public String getNewWarp() { return this.newWarp; }
    public void setNewWarp(String newWarp) { this.newWarp = newWarp; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsWarpDelete(CommandSender sender, Faction faction, String newWarp)
    {
        super(sender);
        this.faction = faction;
        this.newWarp = newWarp;
    }

}
