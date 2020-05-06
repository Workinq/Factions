package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FTopChunkCollectEvent extends Event implements Cancellable
{

    private static final HandlerList handlers = new HandlerList();
    private final Faction faction;
    private final PS ps;
    private boolean cancelled = false;

    public FTopChunkCollectEvent(Faction faction, PS ps)
    {
        super(true);
        this.faction = faction;
        this.ps = ps;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    public Faction getFaction()
    {
        return faction;
    }

    public PS getPs()
    {
        return ps;
    }

}
