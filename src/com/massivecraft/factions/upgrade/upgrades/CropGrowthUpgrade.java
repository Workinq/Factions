package com.massivecraft.factions.upgrade.upgrades;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.upgrade.Upgrade;
import org.bukkit.Material;

public class CropGrowthUpgrade extends Upgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().cropGrowth.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().cropGrowth.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().cropGrowth.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().cropGrowth.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return Material.CACTUS;
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().cropGrowth.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
   }

}
