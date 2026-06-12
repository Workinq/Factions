package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
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

    private String newWarp;
    public String getNewWarp() { return this.newWarp; }
    public void setNewWarp(String newWarp) { this.newWarp = newWarp; }

    private String newPassword;
    public String getNewPassword() { return this.newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    private PS newLocation;
    public PS getNewLocation() { return this.newLocation; }
    public void setNewLocation(PS newLocation) { this.newLocation = newLocation; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsWarpCreate(CommandSender sender, Faction faction, String newWarp, String newPassword, PS newLocation)
    {
        super(sender);
        this.faction = faction;
        this.newWarp = newWarp;
        this.newPassword = newPassword;
        this.newLocation = newLocation;
    }

}
