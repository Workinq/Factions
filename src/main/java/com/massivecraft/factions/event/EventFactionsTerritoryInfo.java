package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.event.HandlerList;

public class EventFactionsTerritoryInfo extends EventFactionsAbstract
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

	private final MPlayer mPlayer;
	public MPlayer getMPlayer() { return mPlayer; }

	private final Faction faction;
	public Faction getFaction() { return faction; }

	private String titleMain;
	public String getTitleMain() { return titleMain; }
	public void setTitleMain(String newTitleMain) { titleMain = newTitleMain; }

	private String titleSub;
	public String getTitleSub() { return titleSub; }
	public void setTitleSub(String newTitleSub) { titleSub = newTitleSub; }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public EventFactionsTerritoryInfo(MPlayer mPlayer, Faction faction, String titleMain, String titleSub)
	{
		this.mPlayer = mPlayer;
		this.faction = faction;
		this.titleMain = titleMain;
		this.titleSub = titleSub;
	}

}
