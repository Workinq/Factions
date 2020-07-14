package com.massivecraft.factions.task;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.money.Money;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class TaskPlaceSand extends ModuloRepeatTask
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static TaskPlaceSand i = new TaskPlaceSand();
    public static TaskPlaceSand get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public long getDelayMillis()
    {
        return MConf.get().sandSpawnDelay;
    }

    @Override
    public void invoke(long now)
    {
        // Loop - Factions
        for (Faction faction : FactionColl.get().getAll())
        {
            // Verify - Empty
            if (faction.getSandAlts().isEmpty()) continue;

            // Loop - Sand Alts
            for (SandAlt sandAlt : faction.getSandAlts())
            {
                // Args
                Location location = sandAlt.getLocation();

                // Verify - Paused
                if (sandAlt.isPaused()) continue;

                // Loop - Blocks
                location_loop: for (int x = location.getBlockX() - MConf.get().sandSpawnRadius; x <= location.getBlockX() + MConf.get().sandSpawnRadius; x++)
                {
                    for (int y = location.getBlockY() - MConf.get().sandSpawnRadius; y <= location.getBlockY() + MConf.get().sandSpawnRadius; y++)
                    {
                        for (int z = location.getBlockZ() - MConf.get().sandSpawnRadius; z <= location.getBlockZ() + MConf.get().sandSpawnRadius; z++)
                        {
                            // Args
                            Block block = location.getWorld().getBlockAt(x, y, z);

                            // Verify - Block Type
                            if (block.getType() != MConf.get().sandSpawnMaterial) continue;
                            if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR) continue;

                            // Verify - Money
                            if ( ! Money.despawn(faction, null, MConf.get().sandCost))
                            {
                                sandAlt.setPaused(true);
                                sandAlt.changed();
                                faction.msg("<g>Your sand alt at <i>x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <i>world: <h>%s <g>cannot print as there's not enough funds.", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
                                break location_loop;
                            }
                            Block affectedBlock = block.getRelative(BlockFace.DOWN);
                            // Spawn
                            affectedBlock.setType(Material.SAND);
                            affectedBlock.getState().update(true);
                            //location.getWorld().spawnFallingBlock(block.getRelative(BlockFace.DOWN).getLocation(), Material.SAND, (byte) 0);
                        }
                    }
                }
            }
        }
    }

}
