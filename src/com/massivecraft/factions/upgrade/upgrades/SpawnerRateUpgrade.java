package com.massivecraft.factions.upgrade.upgrades;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.factions.upgrade.AbstractUpgrade;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftCreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnerRateUpgrade extends AbstractUpgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().spawnerRate.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().spawnerRate.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().spawnerRate.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().spawnerRate.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return MUpgrade.get().spawnerRate.getUpgradeItem();
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().spawnerRate.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
   }

   @EventHandler
   public void onSpawn(SpawnerSpawnEvent event)
   {
      Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(event.getLocation()));

      if (factionAt == null) return;
      if (event.getSpawner().getType() != Material.MOB_SPAWNER) return;

      BlockState state = event.getSpawner().getBlock().getState();

      if (state == null) return;

      if (!this.isDefaultSpawnerSettings(state))
      {
         int spawnerRateLevel = factionAt.getLevel(this.getUpgradeName());
         if (spawnerRateLevel == 0)
         {
            this.setSpawner(state, 225, 625);
         }
         else if (spawnerRateLevel == 1)
         {
            this.setSpawner(state, 210, 580);
         }
         else if (spawnerRateLevel == 2)
         {
            this.setSpawner(state, 200, 530);
         }
         else if (spawnerRateLevel == 3)
         {
            this.setSpawner(state, 188, 498);
         }
         else if (spawnerRateLevel == 4)
         {
            this.setSpawner(state, 172, 452);
         }
         else if (spawnerRateLevel == 5)
         {
            this.setSpawner(state, 161, 437);
         }
      }
   }

   private void setSpawner(BlockState state, int min, int max)
   {
      TileEntityMobSpawner tile = ((CraftCreatureSpawner) state).getTileEntity();
      NBTTagCompound spawnerTag = new NBTTagCompound();
      tile.b(spawnerTag);
      spawnerTag.setShort("MinSpawnDelay", (short) min);
      spawnerTag.setShort("MaxSpawnDelay", (short) max);
      tile.a(spawnerTag);
      state.update();
   }

   private boolean isDefaultSpawnerSettings(BlockState state)
   {
      TileEntityMobSpawner tile = ((CraftCreatureSpawner) state).getTileEntity();
      NBTTagCompound spawnerTag = new NBTTagCompound();
      tile.b(spawnerTag);
      return spawnerTag.getShort("MinSpawnDelay") == 225 && spawnerTag.getShort("MaxSpawnDelay") == 625;
   }

}
