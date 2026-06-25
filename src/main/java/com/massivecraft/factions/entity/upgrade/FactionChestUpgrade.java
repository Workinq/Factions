package com.massivecraft.factions.entity.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public class FactionChestUpgrade extends AbstractUpgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().factionChestUpgrade.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().factionChestUpgrade.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().factionChestUpgrade.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().factionChestUpgrade.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return MUpgrade.get().factionChestUpgrade.getUpgradeItem();
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().factionChestUpgrade.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
      // Args
      int size = faction.getChestSize();

      // Resize every page
      for (int page = 1; page <= faction.getChestCount(); page++)
      {
         Inventory old = faction.getChest(page);

         // Close
         for (HumanEntity entity : old.getViewers()) entity.closeInventory();

         // Upgrade
         Inventory resized = Bukkit.createInventory(null, size, faction.getChestTitle(page));
         resized.setContents(old.getContents());
         faction.setChest(page, resized);
         faction.saveChest(page);
         old.clear();
      }
   }

}
