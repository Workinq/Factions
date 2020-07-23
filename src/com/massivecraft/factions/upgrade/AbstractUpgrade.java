package com.massivecraft.factions.upgrade;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public abstract class AbstractUpgrade implements Listener
{

   public AbstractUpgrade()
   {
      Bukkit.getServer().getPluginManager().registerEvents(this, Factions.get());
   }

   public abstract int getMaxLevel();

   public abstract String getUpgradeName();

   public abstract String[] getCurrentDescription();

   public abstract String[] getNextDescription();

   public abstract Material getUpgradeItem();

   public abstract Integer[] getCost();

   public abstract void onUpgrade(Faction faction);

}
