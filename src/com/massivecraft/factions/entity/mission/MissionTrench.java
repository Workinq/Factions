package com.massivecraft.factions.entity.mission;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class MissionTrench extends AbstractMission
{

   @Override
   public String getMissionName()
   {
      return "Trench";
   }

   @Override
   public String getItemName()
   {
      return MMission.get().trenchMission.getItemName();
   }

   @Override
   public String getDescription()
   {
      return MMission.get().trenchMission.getDescription();
   }

   @Override
   public Double getRequirement()
   {
      return MMission.get().trenchMission.getGoal();
   }

   @Override
   public Integer getReward()
   {
      return MMission.get().trenchMission.getCreditsReward();
   }

   @Override
   public Material getItemMaterial()
   {
      return MMission.get().trenchMission.getItemMaterial();
   }

   @Override
   public Integer getItemData()
   {
      return MMission.get().trenchMission.getItemData();
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onBlockBreak(BlockBreakEvent event)
   {
      if (event.getBlock() == null) return;

      MMission.get().incrementProgress(this, MPlayer.get(event.getPlayer()));
   }

}
