package com.massivecraft.factions.entity.mission;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public class MissionBlaze extends AbstractMission
{

   @Override
   public String getMissionName()
   {
      return "Blaze";
   }

   @Override
   public String getItemName()
   {
      return MMission.get().blazeMission.getItemName();
   }

   @Override
   public String getDescription()
   {
      return MMission.get().blazeMission.getDescription();
   }

   @Override
   public Double getRequirement()
   {
      return MMission.get().blazeMission.getGoal();
   }

   @Override
   public Integer getReward()
   {
      return MMission.get().blazeMission.getCreditsReward();
   }

   @Override
   public Material getItemMaterial()
   {
      return MMission.get().blazeMission.getItemMaterial();
   }

   @Override
   public Integer getItemData()
   {
      return MMission.get().blazeMission.getItemData();
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onMobKill(EntityDeathEvent event)
   {
      if (event.getEntity().getKiller() == null) return;
      if (event.getEntityType() != EntityType.BLAZE) return;

      MMission.get().incrementProgress(this, MPlayer.get(MPlayer.get(event.getEntity().getKiller())));
   }

}
