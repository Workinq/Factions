package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventFactionsBanChange extends EventFactionsAbstractSender
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

    private final MPlayer mplayer;
    public MPlayer getMPlayer() { return this.mplayer; }

    private final Faction faction;
    public Faction getFaction() { return this.faction; }

    private boolean newBanned;
    public boolean isNewBanned() { return this.newBanned; }
    public void setNewBanned(boolean newBanned) { this.newBanned = newBanned; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsBanChange(CommandSender sender, MPlayer mplayer, Faction faction, boolean newBanned)
    {
        super(sender);
        this.mplayer = mplayer;
        this.faction = faction;
        this.newBanned = newBanned;
    }

}
