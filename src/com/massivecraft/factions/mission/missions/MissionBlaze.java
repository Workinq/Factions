package com.massivecraft.factions.mission.missions;

import com.massivecraft.factions.entity.conf.ConfMission;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.mission.Mission;
import com.massivecraft.factions.mission.MissionsManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public class MissionBlaze extends Mission
{

   private final ConfMission confMission = MissionsManager.get().getConfMissionByName("Blaze");

   @Override
   public String getName()
   {
      return "Blaze";
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
   public void onMobKill(EntityDeathEvent event)
   {
      if (event.getEntity().getKiller() == null) return;
      if (event.getEntityType() != EntityType.BLAZE) return;

      MissionsManager.get().incrementProgress(this, MPlayer.get(MPlayer.get(event.getEntity().getKiller())));
   }

}
