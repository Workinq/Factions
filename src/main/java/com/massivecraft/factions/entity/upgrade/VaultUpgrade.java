package com.massivecraft.factions.entity.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public class VaultUpgrade extends AbstractUpgrade
{

   @Override
   public int getMaxLevel()
   {
      return MUpgrade.get().vaultUpgrade.getMaxLevel();
   }

   @Override
   public String getUpgradeName()
   {
      return MUpgrade.get().vaultUpgrade.getUpgradeName();
   }

   @Override
   public String[] getCurrentDescription()
   {
      return MUpgrade.get().vaultUpgrade.getCurrentDescription();
   }

   @Override
   public String[] getNextDescription()
   {
      return MUpgrade.get().vaultUpgrade.getNextDescription();
   }

   @Override
   public Material getUpgradeItem()
   {
      return MUpgrade.get().vaultUpgrade.getUpgradeItem();
   }

   @Override
   public Integer[] getCost()
   {
      return MUpgrade.get().vaultUpgrade.getCost();
   }

   @Override
   public void onUpgrade(Faction faction)
   {
      // Verify
      if (faction.getVault() == null) return;

      // Args
      int size = Integer.parseInt(MUpgrade.get().getUpgradeByName(MUpgrade.get().vaultUpgrade.getUpgradeName()).getCurrentDescription()[faction.getLevel(MUpgrade.get().vaultUpgrade.getUpgradeName()) - 1].split(" ")[0]);

      // Close
      for (HumanEntity entity : faction.getVault().getViewers()) entity.closeInventory();

      // Upgrade
      Inventory old = faction.getVault();
      faction.setVault(Bukkit.createInventory(null, size, Txt.parse("<gray>%s - Faction Vault", faction.getName())));
      faction.getVault().setContents(old.getContents());
      faction.saveVault();
      old.clear();
   }

}
