package com.massivecraft.factions.upgrade.upgrades;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.upgrade.Upgrade;
import org.bukkit.Material;

public class PowerboostUpgrade extends Upgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().powerboostUpgrade.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().powerboostUpgrade.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().powerboostUpgrade.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().powerboostUpgrade.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return MUpgrade.get().powerboostUpgrade.getUpgradeItem();
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().powerboostUpgrade.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
      faction.setPowerBoost(faction.getPowerBoost() + faction.getLevel(this.getUpgradeName()) * 10.0D);
   }

}
