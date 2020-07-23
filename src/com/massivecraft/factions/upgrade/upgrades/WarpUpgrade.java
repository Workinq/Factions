package com.massivecraft.factions.upgrade.upgrades;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.upgrade.AbstractUpgrade;
import org.bukkit.Material;

public class WarpUpgrade extends AbstractUpgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().warpUpgrade.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().warpUpgrade.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().warpUpgrade.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().warpUpgrade.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return MUpgrade.get().warpUpgrade.getUpgradeItem();
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().warpUpgrade.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
   }

}
