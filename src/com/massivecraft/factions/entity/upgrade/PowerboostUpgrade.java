package com.massivecraft.factions.entity.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import org.bukkit.Material;

public class PowerboostUpgrade extends AbstractUpgrade
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
      int power = Integer.parseInt(this.getCurrentDescription()[faction.getLevel(this.getUpgradeName()) - 1].split(" ")[0]);

      faction.setPowerBoost(faction.getPowerBoost() + power);
   }

}
