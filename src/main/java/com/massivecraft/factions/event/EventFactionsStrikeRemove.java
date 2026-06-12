package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.FactionStrike;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventFactionsStrikeRemove extends EventFactionsAbstractSender
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

    private FactionStrike newStrike;
    public FactionStrike getNewStrike() { return this.newStrike; }
    public void setNewWarp(FactionStrike newStrike) { this.newStrike = newStrike; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsStrikeRemove(CommandSender sender, Faction faction, FactionStrike newStrike)
    {
        super(sender);
        this.faction = faction;
        this.newStrike = newStrike;
    }

}
