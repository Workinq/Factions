package com.massivecraft.factions.entity.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.massivecore.util.Txt;
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
      if (faction.getInventory() == null)
      {
         return;
      }
      int size = Integer.parseInt(MUpgrade.get().getUpgradeByName(MUpgrade.get().factionChestUpgrade.getUpgradeName()).getCurrentDescription()[faction.getLevel(MUpgrade.get().factionChestUpgrade.getUpgradeName()) - 1].split(" ")[0]);
      for (HumanEntity entity : faction.getInventory().getViewers())
      {
         entity.closeInventory();
      }
      Inventory old = faction.getInventory();
      faction.setInventory(Bukkit.createInventory(null, size, Txt.parse("<gray>%s - Faction Chest", faction.getName())));
      faction.getInventory().setContents(old.getContents());
      faction.saveInventory();
      old.clear();
   }

}
