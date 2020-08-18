package com.massivecraft.factions.entity.mission;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class MissionEXP extends AbstractMission
{

   @Override
   public String getMissionName()
   {
      return "EXP";
   }

   @Override
   public String getItemName()
   {
      return MMission.get().expMission.getItemName();
   }

   @Override
   public String getDescription()
   {
      return MMission.get().expMission.getDescription();
   }

   @Override
   public Double getRequirement()
   {
      return MMission.get().expMission.getGoal();
   }

   @Override
   public Integer getReward()
   {
      return MMission.get().expMission.getCreditsReward();
   }

   @Override
   public Material getItemMaterial()
   {
      return MMission.get().expMission.getItemMaterial();
   }

   @Override
   public Integer getItemData()
   {
      return MMission.get().expMission.getItemData();
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void gainXp(PlayerExpChangeEvent event)
   {
      if (event.getAmount() <= 0) return;

      MMission.get().incrementProgress(this, MPlayer.get(event.getPlayer()), event.getAmount());
   }

}
