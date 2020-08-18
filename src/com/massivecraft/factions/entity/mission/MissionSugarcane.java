package com.massivecraft.factions.entity.mission;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class MissionSugarcane extends AbstractMission
{

   @Override
   public String getMissionName()
   {
      return "Sugarcane";
   }

   @Override
   public String getItemName()
   {
      return MMission.get().sugarcaneMission.getItemName();
   }

   @Override
   public String getDescription()
   {
      return MMission.get().sugarcaneMission.getDescription();
   }

   @Override
   public Double getRequirement()
   {
      return MMission.get().sugarcaneMission.getGoal();
   }

   @Override
   public Integer getReward()
   {
      return MMission.get().sugarcaneMission.getCreditsReward();
   }

   @Override
   public Material getItemMaterial()
   {
      return MMission.get().sugarcaneMission.getItemMaterial();
   }

   @Override
   public Integer getItemData()
   {
      return MMission.get().sugarcaneMission.getItemData();
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
      MMission.get().incrementProgress(this, MPlayer.get(event.getPlayer()), sugarcaneAmount);
   }

}
