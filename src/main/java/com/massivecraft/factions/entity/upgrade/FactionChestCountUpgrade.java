package com.massivecraft.factions.entity.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import org.bukkit.Material;

public class FactionChestCountUpgrade extends AbstractUpgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().chestCountUpgrade.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().chestCountUpgrade.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().chestCountUpgrade.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().chestCountUpgrade.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return MUpgrade.get().chestCountUpgrade.getUpgradeItem();
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().chestCountUpgrade.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
      // Pages unlock from the upgrade level; getChest creates them on demand.
   }

}
