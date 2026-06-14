package com.massivecraft.factions.integration.worldedit;

import com.massivecraft.factions.engine.ProtectCase;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldedit.EditSession.Stage;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockStateHolder;

import java.awt.Dimension;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.massivecraft.factions.engine.EnginePermBuild.isProtected;

public class ExtentWorldEdit extends AbstractDelegateExtent
{
	// -------------------------------------------- //
	// ALL
	// -------------------------------------------- //

	protected static final Queue<ExtentWorldEdit> all = new ConcurrentLinkedQueue<>();
	public static Queue<ExtentWorldEdit> getAll() { return all; }

	// -------------------------------------------- //
	// WRAP
	// -------------------------------------------- //
	// This event is called three times for the same extent.
	// Once for each Stage enumeration.
	// I believe the correct stage to listen to is BEFORE_HISTORY.

	public static void wrap(EditSessionEvent event)
	{
		if (Stage.BEFORE_HISTORY != event.getStage()) return;

		ExtentWorldEdit extent = new ExtentWorldEdit(event);
		getAll().add(extent);
		event.setExtent(extent);
	}

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	protected final EditSessionEvent event;
	public EditSessionEvent getEvent() { return this.event; }

	protected final ConcurrentHashMap<Dimension, Boolean> chunkCache = new ConcurrentHashMap<>();
	public ConcurrentHashMap<Dimension, Boolean> getChunkCache() { return chunkCache; }

	// -------------------------------------------- //
	// FAKE FIELDS
	// -------------------------------------------- //

	public String getWorldId()
	{
		World world = this.getEvent().getWorld();
		if (world == null) return null;
		return world.getName();
	}

	public static MPlayer getMPlayer(EditSessionEvent event)
	{
		Actor actor = event.getActor();
		if (actor == null) return null;

		// TODO: Support console?
		if ( ! actor.isPlayer()) return null;

		UUID uuid = actor.getUniqueId();
		return MPlayer.get(uuid);
	}

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public ExtentWorldEdit(EditSessionEvent event)
	{
		super(event.getExtent());
		this.event = event;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public final <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 position, T block) throws WorldEditException
	{
		// Get the chunk as a single key, and see if we have in the cache.
		Dimension chunk = new Dimension(position.x() >> 4, position.z() >> 4);
		Boolean cachedResult = chunkCache.get(chunk);

		// If it's in the cache, and is false, then block.
		if (cachedResult == Boolean.FALSE) return false;

		// If it's not in the cache, go check
		if (cachedResult == null) {
			// Get the location as a PS
			PS ps = PS.valueOf(
				getWorldId(), position.x(), position.y(), position.z(),
				null, null, null, null, null, null, null, null, null, null
			);

			MPlayer mPlayer = getMPlayer(event);

			// and check their permissions to build.
			if (isProtected(ProtectCase.BUILD, true, mPlayer, ps, null) == Boolean.TRUE) {
				// Alert the player
				mPlayer.msg(
					"<i>Factions <k>protected <i>Chunk <v>%d<i>, <v>%d<i> from a WorldEdit operation. ",
					chunk.height,
					chunk.width
				);

				// And store the result in the cache
				getChunkCache().put(chunk, false);
				return false;
			}

			// Otherwise, store the success in the cache.
			getChunkCache().put(chunk, true);
		}

		// And pass on to the next extent.
		return getExtent().setBlock(position, block);
	}

}
