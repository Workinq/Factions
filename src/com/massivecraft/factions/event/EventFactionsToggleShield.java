package com.massivecraft.factions.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventFactionsToggleShield extends EventFactionsAbstractSender
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

	private boolean active;
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	public EventFactionsToggleShield(CommandSender sender, boolean active)
	{
		super(sender);
		this.active = active;
	}

}
