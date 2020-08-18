package com.massivecraft.factions.entity.mission;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class MissionTravel extends AbstractMission
{

   @Override
   public String getMissionName()
   {
      return "Travel";
   }

   @Override
   public String getItemName()
   {
      return MMission.get().travelMission.getItemName();
   }

   @Override
   public String getDescription()
   {
      return MMission.get().travelMission.getDescription();
   }

   @Override
   public Double getRequirement()
   {
      return MMission.get().travelMission.getGoal();
   }

   @Override
   public Integer getReward()
   {
      return MMission.get().travelMission.getCreditsReward();
   }

   @Override
   public Material getItemMaterial()
   {
      return MMission.get().travelMission.getItemMaterial();
   }

   @Override
   public Integer getItemData()
   {
      return MMission.get().travelMission.getItemData();
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onPlayerMove(PlayerMoveEvent event)
   {
      if (MUtil.isSameBlock(event)) return;

      MMission.get().incrementProgress(this, MPlayer.get(event.getPlayer()));
   }

}
