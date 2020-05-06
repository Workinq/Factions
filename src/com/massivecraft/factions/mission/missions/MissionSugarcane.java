package com.massivecraft.factions.mission.missions;

import com.massivecraft.factions.entity.conf.ConfMission;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.mission.Mission;
import com.massivecraft.factions.mission.MissionsManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class MissionSugarcane extends Mission
{

   private final ConfMission confMission = MissionsManager.get().getConfMissionByName("Sugarcane");

   @Override
   public String getName()
   {
      return "Sugarcane";
   }

   @Override
   public String getItemName()
   {
      return confMission.getItemName();
   }

   @Override
   public String getDescription()
   {
      return confMission.getDescription();
   }

   @Override
   public Double getRequirement()
   {
      return confMission.getGoal();
   }

   @Override
   public Integer getReward()
   {
      return confMission.getCreditsReward();
   }

   @Override
   public Material getItemMaterial()
   {
      return confMission.getItemMaterial();
   }

   @Override
   public Integer getItemData()
   {
      return confMission.getItemData();
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onBlockBreak(BlockBreakEvent event)
   {
      if (event.getBlock() == null || event.getBlock().getType() != Material.SUGAR_CANE_BLOCK) return;

      int sugarcaneAmount = 0;
      for (int i = event.getBlock().getY(); i < 256; i++)
      {
         Block block = new Location(event.getBlock().getWorld(), event.getBlock().getX(), i, event.getBlock().getZ()).getBlock();
         if (block.getType() != Material.SUGAR_CANE_BLOCK) break;

         sugarcaneAmount++;
      }
      MissionsManager.get().incrementProgress(this, MPlayer.get(event.getPlayer()), sugarcaneAmount);
   }

}
