package com.massivecraft.factions.entity.mission;

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

   public abstract String getMissionName();

   public abstract String getDescription();

   public abstract Double getRequirement();

   public abstract Integer getReward();

   public abstract Material getItemMaterial();

   public abstract String getItemName();

   public abstract Integer getItemData();

}
