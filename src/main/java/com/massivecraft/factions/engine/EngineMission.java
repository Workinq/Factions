package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.mission.Mission;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EngineMission extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineMission i = new EngineMission();
    public static EngineMission get() { return i; }

    // -------------------------------------------- //
    // HELPERS
    // -------------------------------------------- //

    // The active mission for the acting player's faction, or null if it is not of the wanted type.
    private Mission active(Player player, com.massivecraft.factions.entity.mission.MissionType type)
    {
        Faction faction = MPlayer.get(player).getFaction();
        Mission mission = faction.getActiveMission();
        if (mission == null || mission.getType() != type) return null;
        return mission;
    }

    // -------------------------------------------- //
    // COMBAT
    // -------------------------------------------- //

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(EntityDeathEvent event)
    {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        Faction faction = MPlayer.get(killer).getFaction();
        Mission mission = faction.getActiveMission();
        if (mission == null) return;

        boolean matches;
        switch (mission.getType())
        {
            case KILL_ENTITY:
            case KILL_BOSS:
                matches = mission.matchesTarget(event.getEntityType().name());
                break;
            case KILL_ANY_HOSTILE:
                matches = event.getEntity() instanceof Monster;
                break;
            case KILL_PLAYER:
                matches = event.getEntity() instanceof Player;
                break;
            default:
                return;
        }

        if (matches) MMission.get().incrementProgress(mission, faction, 1);
    }

    // -------------------------------------------- //
    // MINING / GATHERING
    // -------------------------------------------- //

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock() == null) return;

        Faction faction = MPlayer.get(event.getPlayer()).getFaction();
        Mission mission = faction.getActiveMission();
        if (mission == null) return;

        Material material = event.getBlock().getType();
        switch (mission.getType())
        {
            case MINE_ANY:
                MMission.get().incrementProgress(mission, faction, 1);
                break;
            case BREAK_BLOCK:
                if (mission.matchesTarget(material.name())) MMission.get().incrementProgress(mission, faction, 1);
                break;
            case CHOP_LOG:
                if (material.name().endsWith("_LOG") || material.name().endsWith("_STEM")) MMission.get().incrementProgress(mission, faction, 1);
                break;
            case HARVEST_CROP:
                if (mission.matchesTarget(material.name())) MMission.get().incrementProgress(mission, faction, countColumn(event.getBlock(), material));
                break;
            default:
                break;
        }
    }

    // Counts a vertical stack of the same material upward from the broken block.
    // Preserves the original Sugarcane behaviour; returns 1 for non-stacking crops.
    private int countColumn(Block block, Material material)
    {
        int amount = 0;
        World world = block.getWorld();
        for (int y = block.getY(); y < world.getMaxHeight(); y++)
        {
            if (world.getBlockAt(block.getX(), y, block.getZ()).getType() != material) break;
            amount++;
        }
        return Math.max(amount, 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFish(PlayerFishEvent event)
    {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        Mission mission = this.active(event.getPlayer(), com.massivecraft.factions.entity.mission.MissionType.FISH);
        if (mission == null) return;

        MMission.get().incrementProgress(mission, MPlayer.get(event.getPlayer()), 1);
    }

    // -------------------------------------------- //
    // MISC / PROGRESSION
    // -------------------------------------------- //

    @EventHandler(priority = EventPriority.MONITOR)
    public void onExp(PlayerExpChangeEvent event)
    {
        if (event.getAmount() <= 0) return;

        Mission mission = this.active(event.getPlayer(), com.massivecraft.factions.entity.mission.MissionType.GAIN_EXP);
        if (mission == null) return;

        MMission.get().incrementProgress(mission, MPlayer.get(event.getPlayer()), event.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event)
    {
        if (MUtil.isSameBlock(event)) return;

        Mission mission = this.active(event.getPlayer(), com.massivecraft.factions.entity.mission.MissionType.TRAVEL);
        if (mission == null) return;

        MMission.get().incrementProgress(mission, MPlayer.get(event.getPlayer()), 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEnchant(EnchantItemEvent event)
    {
        Mission mission = this.active(event.getEnchanter(), com.massivecraft.factions.entity.mission.MissionType.ENCHANT);
        if (mission == null) return;

        MMission.get().incrementProgress(mission, MPlayer.get(event.getEnchanter()), 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSmelt(FurnaceExtractEvent event)
    {
        Mission mission = this.active(event.getPlayer(), com.massivecraft.factions.entity.mission.MissionType.SMELT);
        if (mission == null) return;

        MMission.get().incrementProgress(mission, MPlayer.get(event.getPlayer()), event.getItemAmount());
    }

}
