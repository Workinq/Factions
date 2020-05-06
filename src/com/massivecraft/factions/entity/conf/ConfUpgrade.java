package com.massivecraft.factions.entity.conf;

import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Material;

public class ConfUpgrade extends EntityInternal<ConfUpgrade>
{

   private final int maxLevel;
   private final String upgradeName;
   private final String[] currentDescription;
   private final String[] nextDescription;
   private final Material upgradeItem;
   private final Integer[] cost;

   public String getUpgradeName()
   {
      return this.upgradeName;
   }

   public String[] getCurrentDescription()
   {
      return this.currentDescription;
   }

   public String[] getNextDescription()
   {
      return this.nextDescription;
   }

   public Material getUpgradeItem()
   {
      return this.upgradeItem;
   }

   public Integer[] getCost()
   {
      return this.cost;
   }

   public ConfUpgrade(String upgradeName, Material upgradeItem, int maxLevel, String[] currentDescription, String[] nextDescription, Integer[] cost)
   {
      this.maxLevel = maxLevel;
      this.upgradeName = upgradeName;
      this.currentDescription = currentDescription;
      this.nextDescription = nextDescription;
      this.upgradeItem = upgradeItem;
      this.cost = cost;
   }

   public int getMaxLevel()
   {
      return this.maxLevel;
   }

}
