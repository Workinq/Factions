package com.massivecraft.factions.mission.missions;

import com.massivecraft.factions.entity.conf.ConfMission;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.mission.Mission;
import com.massivecraft.factions.mission.MissionsManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

public class MissionCactus extends Mission
{

   private final ConfMission confMission = MissionsManager.get().getConfMissionByName("Cactus");

   @Override
   public String getName()
   {
      return "Cactus";
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
   public void onBlockBreak(BlockPlaceEvent event)
   {
      if (event.getBlock() != null && event.getBlock().getType() == Material.CACTUS)
      {
         MissionsManager.get().incrementProgress(this, MPlayer.get(event.getPlayer()));
      }
   }

}
