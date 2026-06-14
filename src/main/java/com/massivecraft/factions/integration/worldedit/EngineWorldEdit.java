package com.massivecraft.factions.integration.worldedit;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.EventHandler.Priority;
import com.sk89q.worldedit.util.eventbus.Subscribe;

public class EngineWorldEdit extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static final EngineWorldEdit i = new EngineWorldEdit();
	public static EngineWorldEdit get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void setActiveInner(boolean active)
	{
		if (active)
		{
			WorldEdit.getInstance().getEventBus().register(this);
		}
		else
		{
			WorldEdit.getInstance().getEventBus().unregister(this);
		}
	}

	// -------------------------------------------- //
	// WORLD EDIT LISTENER
	// -------------------------------------------- //

	@Subscribe(priority = Priority.VERY_LATE)
	public void worldEditListener(EditSessionEvent event)
	{
		MPlayer mPlayer = ExtentWorldEdit.getMPlayer(event);

		// If the Player is Overriding, or is null, then don't wrap.
		if (mPlayer == null || mPlayer.isOverriding()) return;

		ExtentWorldEdit.wrap(event);
	}

}
