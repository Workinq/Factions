package com.massivecraft.factions.engine;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.TerritoryAccess;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.integration.spigot.IntegrationSpigot;
import com.massivecraft.factions.util.EnumerationUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EnginePermBuild extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static final EnginePermBuild i = new EnginePermBuild();
	public static EnginePermBuild get() { return i; }

	// -------------------------------------------- //
	// LOGIC > PROTECT
	// -------------------------------------------- //
	
	public static Boolean isProtected(ProtectCase protectCase, boolean verboose, MPlayer mplayer, PS ps, Object object)
	{
		if (mplayer == null) return null;
		if (protectCase == null) return null;
		String name = mplayer.getName();
		if (MConf.get().playersWhoBypassAllProtection.contains(name)) return false;
		if (mplayer.isOverriding()) return false;

		MPerm perm = protectCase.getPerm(object);
		if (perm == null) return null;
		if (protectCase != ProtectCase.BUILD) return ! perm.has(mplayer, ps, verboose);
		
		if ( ! perm.has(mplayer, ps, false) && MPerm.getPermPainbuild().has(mplayer, ps, false) )
		{
			if ( ! verboose ) return false;
			
			Faction hostFaction = BoardColl.get().getFactionAt(ps);
			mplayer.msg("<b>It is painful to build in the territory of %s<b>.", hostFaction.describeTo(mplayer));
			Player player = mplayer.getPlayer();
			if (player != null) player.damage(MConf.get().actionDeniedPainAmount);

			return false;
		}
		
		return ! perm.has(mplayer, ps, verboose);
	}
	
	public static Boolean protect(ProtectCase protectCase, boolean verboose, Object senderObject, PS ps, Object object, Cancellable cancellable)
	{
		Boolean ret = isProtected(protectCase, verboose, MPlayer.get(senderObject), ps, object);
		if (Boolean.TRUE.equals(ret) && cancellable != null) cancellable.setCancelled(true);
		return ret;
	}
	
	public static Boolean build(Entity entity, Block block, Event event)
	{
		if (!(event instanceof Cancellable)) return true;

		// Resolve a projectile to its shooter
		if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Entity)
		{
			entity = (Entity) ((Projectile) entity).getShooter();
		}

		// Resolve a ridden entity to its player passenger
		if (entity != null && MUtil.isntPlayer(entity) && !entity.getPassengers().isEmpty() && entity.getPassengers().get(0) instanceof Player)
		{
			entity = entity.getPassengers().getFirst();
		}

		boolean verboose = !isFake(event);
		return protect(ProtectCase.BUILD, verboose, entity, PS.valueOf(block), block, (Cancellable) event);
	}
	
	public static Boolean useItem(Entity entity, Block block, Material material, Cancellable cancellable)
	{
		return protect(ProtectCase.USE_ITEM, true, entity, PS.valueOf(block), material, cancellable);
	}
	
	public static Boolean useEntity(Entity player, Entity entity, boolean verboose, Cancellable cancellable)
	{
		return protect(ProtectCase.USE_ENTITY, verboose, player, PS.valueOf(entity), entity, cancellable);
	}
	
	public static Boolean useBlock(Player player, Block block, boolean verboose, Cancellable cancellable)
	{
		return protect(ProtectCase.USE_BLOCK, verboose, player, PS.valueOf(block), block.getType(), cancellable);
	}
	
	// -------------------------------------------- //
	// LOGIC > PROTECT > BUILD
	// -------------------------------------------- //
	
	public static boolean canPlayerBuildAt(Object senderObject, PS ps, boolean verboose)
	{
		MPlayer mplayer = MPlayer.get(senderObject);
		if (mplayer == null) return false;
		
		Boolean ret = isProtected(ProtectCase.BUILD, verboose, mplayer, ps, null);
		return !Boolean.TRUE.equals(ret);
	}
	
	// -------------------------------------------- //
	// BUILD > BLOCK
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void build(BlockPlaceEvent event) { build(event.getPlayer(), event.getBlock(), event); }

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void build(BlockBreakEvent event) { build(event.getPlayer(), event.getBlock(), event); }

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void containerBreak(BlockBreakEvent event)
	{
		if (!(event.getBlock().getState() instanceof Container container)) return;

		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;

		MPlayer mplayer = MPlayer.get(player);
		if (mplayer == null) return;

		if (MConf.get().playersWhoBypassAllProtection.contains(mplayer.getName())) return;
		if (mplayer.isOverriding()) return;

		if (container.getInventory().isEmpty()) return;

		if (MPerm.getPermContainer().has(mplayer, PS.valueOf(event.getBlock()), true)) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void build(BlockDamageEvent event) { if (event.getInstaBreak()) build(event.getPlayer(), event.getBlock(), event); }

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void build(SignChangeEvent event) { build(event.getPlayer(), event.getBlock(), event); }
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void build(HangingPlaceEvent event) { build(event.getPlayer(), event.getBlock(), event); }
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void build(HangingBreakByEntityEvent event) { build(event.getRemover(), event.getEntity().getLocation().getBlock(), event); }

	// -------------------------------------------- //
	// USE > ITEM
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void useBlockItem(PlayerInteractEvent event)
	{
		// If the player right clicks (or is physical with) a block ...
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.PHYSICAL) return;

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		if (block == null) return;

		// ... and we are either allowed to use this block ...
		Boolean ret = useBlock(player, block, event.getHand() != EquipmentSlot.OFF_HAND, event);
		if (Boolean.TRUE.equals(ret)) return;
		
		// ... or are allowed to right click with the item, this event is safe to perform.
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		useItem(player, block, event.getMaterial(), event);
	}

	// For some reason onPlayerInteract() sometimes misses bucket events depending on distance
	// (something like 2-3 blocks away isn't detected), but these separate bucket events below always fire without fail

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void useItem(PlayerBucketEmptyEvent event) { useItem(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace()), event.getBucket(), event); }
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void useItem(PlayerBucketFillEvent event) { useItem(event.getPlayer(), event.getBlockClicked(), event.getBucket(), event); }

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void useLectern(PlayerTakeLecternBookEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;

		MPlayer mplayer = MPlayer.get(player);
		if (mplayer == null) return;
		if (MConf.get().playersWhoBypassAllProtection.contains(mplayer.getName())) return;
		if (mplayer.isOverriding()) return;

		Block block = event.getLectern().getBlock();
		if (MPerm.getPermContainer().has(mplayer, PS.valueOf(block), true)) return;
		event.setCancelled(true);
	}

	// -------------------------------------------- //
	// USE > ENTITY
	// -------------------------------------------- //
	
	// This event will not fire for Minecraft 1.8 armor stands.
	// Armor stands are handled in EngineSpigot instead.
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void useEntity(PlayerInteractEntityEvent event)
	{
		// Ignore Off Hand
		if (isOffHand(event)) return;
		useEntity(event.getPlayer(), event.getRightClicked(), true, event);
	}
	
	// -------------------------------------------- //
	// BUILD > ENTITY
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void buildEntity(EntityDamageByEntityEvent event)
	{
		// If a player ...
		Entity damager = MUtil.getLiableDamager(event);
		if (MUtil.isntPlayer(damager)) return;
		Player player = (Player)damager;
		
		// ... damages an entity which is edited on damage ...
		Entity entity = event.getEntity();
		if (entity == null || !EnumerationUtil.isEntityTypeEditOnDamage(entity.getType())) return;
		
		// ... and the player can't build there, cancel the event
		build(player, entity.getLocation().getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void combustEntity(EntityCombustByEntityEvent event) {
		
		// If a burning projectile ...
		if (!(event.getCombuster() instanceof Projectile)) return;
		Projectile entityProjectile = (Projectile)event.getCombuster();
		
		// ... fired by a player ...
		ProjectileSource projectileSource = entityProjectile.getShooter();
		if (MUtil.isntPlayer(projectileSource)) return;
		
		// ... and hits an entity which is edited on damage (and thus likely to burn) ...
		Entity entityTarget = event.getEntity();
		if (entityTarget == null || !EnumerationUtil.isEntityTypeEditOnDamage(entityTarget.getType())) return;

		// ... and the player can't build there, cancel the event
		Block block = entityTarget.getLocation().getBlock();
		protect(ProtectCase.BUILD, false, projectileSource, PS.valueOf(block), block, event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void riderCheck(EntityChangeBlockEvent event)
	{
		Entity entity = event.getEntity();

		boolean playerInvolved = entity instanceof Player || (!entity.getPassengers().isEmpty() && entity.getPassengers().getFirst() instanceof Player);
		if (!playerInvolved) return;

		build(entity, event.getBlock(), event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void build(BlockIgniteEvent event)
	{
		build(event.getIgnitingEntity(), event.getBlock(), event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void frostwalk(EntityBlockFormEvent event)
	{
		build(event.getEntity(), event.getBlock(), event);
	}

	// -------------------------------------------- //
	// BUILD > VEHICLE
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void vehiclePlace(EntityPlaceEvent event)
	{
		if (!(event.getEntity() instanceof Vehicle)) return;
		Player player = event.getPlayer();
		if (player == null) return;
		build(player, event.getBlock(), event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void vehicleDestroy(VehicleDestroyEvent event)
	{
		if (event.getAttacker() == null) return;
		build(event.getAttacker(), event.getVehicle().getLocation().getBlock(), event);
	}

	// -------------------------------------------- //
	// BUILD > PISTON
	// -------------------------------------------- //
	
	/*
	* NOTE: These piston listeners are only called on 1.7 servers.
	*
	* Originally each affected block in the territory was tested, but since we found that pistons can only push
	* up to 12 blocks and the width of any territory is 16 blocks, it should be safe (and much more lightweight) to test
	* only the final target block as done below.
	*/
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void buildPiston(BlockPistonExtendEvent event)
	{
		// Is using Spigot or is checking deactivated by MConf?
		if (IntegrationSpigot.get().isIntegrationActive() || !MConf.get().handlePistonProtectionThroughDenyBuild) return;
		
		// Targets end-of-the-line empty (air) block which is being pushed into, including if piston itself would extend into air
		Block block = event.getBlock();
		Block targetBlock = block.getRelative(event.getDirection(), event.getLength() + 1);
		
		// Factions involved
		Faction pistonFaction = BoardColl.get().getFactionAt(PS.valueOf(block));
		Faction targetFaction = BoardColl.get().getFactionAt(PS.valueOf(targetBlock));
		
		// Members of a faction might not have build rights in their own territory, but pistons should still work regardless
		if (targetFaction == pistonFaction) return;
		
		// If potentially pushing into air/water/lava in another territory, we need to check it out
		if (!targetBlock.isEmpty() && !targetBlock.isLiquid()) return;
		if (MPerm.getPermBuild().has(pistonFaction, targetFaction)) return;
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void buildPiston(BlockPistonRetractEvent event)
	{
		// Is using Spigot or is checking deactivated by MConf?
		if (IntegrationSpigot.get().isIntegrationActive() || ! MConf.get().handlePistonProtectionThroughDenyBuild) return;
		
		// If not a sticky piston, retraction should be fine
		if ( ! event.isSticky()) return;
		
		Block retractBlock = event.getRetractLocation().getBlock();
		PS retractPs = PS.valueOf(retractBlock);
		
		// if potentially retracted block is just air/water/lava, no worries
		if (retractBlock.isEmpty() || retractBlock.isLiquid()) return;
		
		// Factions involved
		Faction pistonFaction = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock()));
		Faction targetFaction = BoardColl.get().getFactionAt(retractPs);
		
		// Members of a faction might not have build rights in their own territory, but pistons should still work regardless
		if (targetFaction == pistonFaction) return;
		if (MPerm.getPermBuild().has(pistonFaction, targetFaction)) return;
		
		event.setCancelled(true);
	}
	
	// -------------------------------------------- //
	// BUILD > FIRE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void buildFire(PlayerInteractEvent event)
	{
		// If it is a left click on block and the clicked block is not null...
		if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getClickedBlock() == null) return;
		
		// ... and the potential block is not null either ...
		Block potentialBlock = event.getClickedBlock().getRelative(BlockFace.UP, 1);
		if (potentialBlock == null) return;
		
		Material blockType = potentialBlock.getType();
		
		// ... and we're only going to check for fire ... (checking everything else would be bad performance wise)
		if (blockType != Material.FIRE) return;
		
		// ... check if they can't build, cancel the event ...
		if (!Boolean.FALSE.equals(build(event.getPlayer(), potentialBlock, event))) return;
		
		// ... and compensate for client side prediction
		event.getPlayer().sendBlockChange(potentialBlock.getLocation(), potentialBlock.getBlockData());
	}
	
	// -------------------------------------------- //
	// BUILD > MOVE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void buildMove(BlockFromToEvent event)
	{
		if ( ! MConf.get().protectionLiquidFlowEnabled) return;
		
		// Prepare fields
		Block fromBlock = event.getBlock();
		int chunkFromX = fromBlock.getX() >> 4;
		int chunkFromZ = fromBlock.getZ() >> 4;
		BlockFace face = event.getFace();
		int chunkToX = (fromBlock.getX() + face.getModX()) >> 4;
		int chunkToZ = (fromBlock.getZ() + face.getModZ()) >> 4;
		
		// If a liquid (or dragon egg) moves from one chunk to another ...
		if (chunkToX == chunkFromX && chunkToZ == chunkFromZ) return;
		
		// ... get the correct board for this block ...
		Board board = BoardColl.get().getFixed(fromBlock.getWorld().getName().toLowerCase(), false);
		if (board == null) return;
		
		// ... get the access map ...
		Map<PS, TerritoryAccess> map = board.getMapRaw();
		if (map.isEmpty()) return;
		
		// ... get the faction ids from and to ...
		PS fromPs = PS.valueOf(chunkFromX, chunkFromZ);
		PS toPs = PS.valueOf(chunkToX, chunkToZ);
		TerritoryAccess fromTa = map.get(fromPs);
		TerritoryAccess toTa = map.get(toPs);
		
		// Null checks are needed here since automatic board cleaning can be undesired sometimes
		String fromId = fromTa != null ? fromTa.getHostFactionId() : Factions.ID_NONE;
		String toId = toTa != null ? toTa.getHostFactionId() : Factions.ID_NONE;
		
		// ... and the chunks belong to different factions ...
		if (toId.equals(fromId)) return;
		
		// ... and the faction "from" can not build at "to" ...
		Faction fromFac = FactionColl.get().getFixed(fromId);
		if (fromFac == null) fromFac = FactionColl.get().getNone();
		Faction toFac = FactionColl.get().getFixed(toId);
		if (toFac == null) toFac = FactionColl.get().getNone();
		if (MPerm.getPermBuild().has(fromFac, toFac)) return;

		// ... cancel the event!
		event.setCancelled(true);
	}

	// -------------------------------------------- //
	// BUILD > BONEMEAL
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void bonemealGrow(StructureGrowEvent event)
	{
		if ( ! MConf.get().handleBonemealProtectionThroughDenyBuild) return;
		if ( ! event.isFromBonemeal()) return;
		bonemealFilter(PS.valueOf(event.getLocation()), event.getBlocks());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void bonemealFertilize(BlockFertilizeEvent event)
	{
		if ( ! MConf.get().handleBonemealProtectionThroughDenyBuild) return;
		bonemealFilter(PS.valueOf(event.getBlock()), event.getBlocks());
	}

	private static void bonemealFilter(PS source, List<BlockState> blocks)
	{
		Faction sourceFaction = BoardColl.get().getFactionAt(source);
		Iterator<BlockState> it = blocks.iterator();
		while (it.hasNext())
		{
			BlockState state = it.next();
			Faction targetFaction = BoardColl.get().getFactionAt(PS.valueOf(state.getBlock()));
			if (targetFaction == sourceFaction) continue;
			if (MPerm.getPermBuild().has(sourceFaction, targetFaction)) continue;
			it.remove();
		}
	}

	// -------------------------------------------- //
	// BUILD > DISPENSER
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void dispense(BlockDispenseEvent event)
	{
		if ( ! MConf.get().handleDispenserProtectionThroughDenyBuild) return;

		Block block = event.getBlock();
		if ( ! (block.getBlockData() instanceof Directional directional)) return;
		Block targetBlock = block.getRelative(directional.getFacing());

		if ((block.getX() >> 4) == (targetBlock.getX() >> 4) && (block.getZ() >> 4) == (targetBlock.getZ() >> 4)) return;

		Faction sourceFaction = BoardColl.get().getFactionAt(PS.valueOf(block));
		Faction targetFaction = BoardColl.get().getFactionAt(PS.valueOf(targetBlock));
		if (targetFaction == sourceFaction) return;
		if (MPerm.getPermBuild().has(sourceFaction, targetFaction)) return;

		event.setCancelled(true);
	}

}
