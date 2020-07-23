package com.massivecraft.factions.mission;

import com.massivecraft.factions.Factions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

public abstract class AbstractMission implements Listener
{

   public AbstractMission()
   {
      Bukkit.getServer().getPluginManager().registerEvents(this, Factions.get());
   }

   public abstract String getName();

   public abstract String getDescription();

   public abstract Double getRequirement();

   public abstract Integer getReward();

   public abstract Material getItemMaterial();

   public abstract String getItemName();

   public abstract Integer getItemData();

}
