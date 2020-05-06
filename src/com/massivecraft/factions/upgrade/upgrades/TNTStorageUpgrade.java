package com.massivecraft.factions.upgrade.upgrades;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.upgrade.Upgrade;
import org.bukkit.Material;

public class TNTStorageUpgrade extends Upgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().tntUpgrade.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().tntUpgrade.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().tntUpgrade.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().tntUpgrade.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return MUpgrade.get().tntUpgrade.getUpgradeItem();
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().tntUpgrade.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
   }

}
