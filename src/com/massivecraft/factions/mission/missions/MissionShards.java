package com.massivecraft.factions.mission.missions;

import com.massivecraft.factions.entity.conf.ConfMission;
import com.massivecraft.factions.event.EventFactionsShardsChange;
import com.massivecraft.factions.mission.Mission;
import com.massivecraft.factions.mission.MissionsManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class MissionShards extends Mission
{

   private final ConfMission confMission = MissionsManager.get().getConfMissionByName("Shards");

   @Override
   public String getName()
   {
      return "Shards";
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
   public void onShardsChange(EventFactionsShardsChange event)
   {
      MissionsManager.get().incrementProgress(this, event.getFaction(), event.getShards());
   }

}
