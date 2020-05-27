package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import org.bukkit.event.HandlerList;

public class EventFactionsTntChange extends EventFactionsAbstract
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

    private int tnt;
    public int getTnt() { return this.tnt; }
    public void setTnt(int tnt) { this.tnt = tnt; }

    private final int oldTnt;
    public int getOldTnt() { return this.oldTnt; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsTntChange(Faction faction, int tnt)
    {
        super(false);
        this.faction = faction;
        this.tnt = tnt;
        this.oldTnt = faction.getTnt();
    }

}
