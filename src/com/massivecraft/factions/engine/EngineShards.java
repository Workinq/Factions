package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.ThreadLocalRandom;

public class EngineShards extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineShards i = new EngineShards();
    public static EngineShards get() { return i; }

    // -------------------------------------------- //
    // SHARD ITEM
    // -------------------------------------------- //

    public ItemStack getShardItem(int amount)
    {
        ItemStack shard = new ItemStack(MConf.get().shardMaterial, amount, (short) MConf.get().shardData);
        ItemMeta meta = shard.getItemMeta();
        meta.setDisplayName(Txt.parse(MConf.get().shardName));
        meta.setLore(Txt.parse(MConf.get().shardLore));
        shard.setItemMeta(meta);
        return shard;
    }

    // -------------------------------------------- //
    // SHARDS
    // -------------------------------------------- //

    @EventHandler
    public void onShardDrop(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) return;

        // Args
        EntityType type = event.getEntityType();
        if ( ! MConf.get().shardChances.containsKey(type)) return;
        if ( ! MConf.get().entityTypesShards.contains(type)) return;

        Location location = event.getLocation();
        if (location == null) return;

        Faction at = BoardColl.get().getFactionAt(PS.valueOf(location));
        if (at.isSystemFaction()) return;

        PS chunk = PS.valueOf(location.getChunk());
        if (at.getBaseRegion() == null) return;
        if ( ! at.getBaseRegion().contains(chunk)) return;

        // Cancel
        event.setCancelled(true);

        // Args
        int minimum = MConf.get().shardChances.get(type).get(0);
        int maximum = MConf.get().shardChances.get(type).get(1);

        // Apply
        at.addShards(ThreadLocalRandom.current().nextInt(minimum, maximum));
    }

    @EventHandler
    public void onMoneyDrop(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) return;

        // Args
        EntityType type = event.getEntityType();
        if ( ! MConf.get().moneyChances.containsKey(type)) return;
        if ( ! MConf.get().entityTypesMoney.contains(type)) return;

        Location location = event.getLocation();
        if (location == null) return;

        Faction at = BoardColl.get().getFactionAt(PS.valueOf(location));
        if (at.isSystemFaction()) return;

        PS chunk = PS.valueOf(location.getChunk());
        if (at.getBaseRegion() == null) return;
        if ( ! at.getBaseRegion().contains(chunk)) return;

        // Cancel
        event.setCancelled(true);

        // Args
        int minimum = MConf.get().moneyChances.get(type).get(0);
        int maximum = MConf.get().moneyChances.get(type).get(1);

        // Apply
        Money.spawn(at, null, ThreadLocalRandom.current().nextInt(minimum, maximum));
    }

    @EventHandler
    public void onKillEntity(EntityDeathEvent event)
    {
        EntityType type = event.getEntityType();
        if ( ! MConf.get().entityTypesShards.contains(type)) return;

        // Args
        int minimum = MConf.get().shardChances.get(type).get(0);
        int maximum = MConf.get().shardChances.get(type).get(1);
        int shards = ThreadLocalRandom.current().nextInt(minimum, maximum);

        // Add shards
        event.getDrops().add(getShardItem(shards));
    }

    @EventHandler
    public void onClickShard(PlayerInteractEvent event)
    {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != MConf.get().shardMaterial) return;
        if ( ! item.hasItemMeta() || ! item.getItemMeta().hasDisplayName() || ! item.getItemMeta().hasLore()) return;
        if ( ! item.getItemMeta().getDisplayName().equals(Txt.parse(MConf.get().shardName))) return;
        if ( ! item.getItemMeta().getLore().equals(Txt.parse(MConf.get().shardLore))) return;

        Player player = event.getPlayer();
        MPlayer mplayer = MPlayer.get(player);
        if ( ! mplayer.hasFaction())
        {
            mplayer.msg("<b>You can only redeem shards if you're in a faction.");
            return;
        }

        Faction faction = mplayer.getFaction();
        int totalShards = getShardsIn(player.getInventory());
        faction.addShards(totalShards);
        mplayer.msg("%s <i>deposited <h>%,d <i>shards into %s's <i>shard balance.", mplayer.describeTo(mplayer, true), totalShards, faction.describeTo(mplayer));
    }

    private int getShardsIn(Inventory inventory)
    {
        int total = 0;
        for (int i = 0; i < inventory.getSize(); i++)
        {
            // Args
            ItemStack item = inventory.getItem(i);

            // Verify
            if (item == null || item.getType() != MConf.get().shardMaterial) continue;
            if ( ! item.hasItemMeta() || ! item.getItemMeta().hasDisplayName() || ! item.getItemMeta().hasLore()) continue;
            if ( ! item.getItemMeta().getDisplayName().equals(Txt.parse(MConf.get().shardName))) continue;
            if ( ! item.getItemMeta().getLore().equals(Txt.parse(MConf.get().shardLore))) continue;

            // Apply
            total += item.getAmount();
            inventory.setItem(i, null);
        }
        return total;
    }

}
