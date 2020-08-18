package com.massivecraft.factions.entity.mission;

import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Material;

public class ConfMission extends EntityInternal<ConfMission>
{

   private final String name;
   private final String description;
   private final Double goal;
   private final Integer creditsReward;
   private final String itemName;
   private final Material itemMaterial;
   private final Integer itemData;

   public ConfMission(String name, String itemName, String description, Double goal, Integer creditsReward, Material itemMaterial, Integer itemData)
   {
      this.name = name;
      this.itemName = itemName;
      this.description = description;
      this.goal = goal;
      this.creditsReward = creditsReward;
      this.itemMaterial = itemMaterial;
      this.itemData = itemData;
   }

   public String getItemName()
   {
      return this.itemName;
   }

   public String getDescription()
   {
      return this.description;
   }

   public Double getGoal()
   {
      return this.goal;
   }

   public Integer getCreditsReward()
   {
      return this.creditsReward;
   }

   public Material getItemMaterial()
   {
      return this.itemMaterial;
   }

   public Integer getItemData()
   {
      return this.itemData;
   }

   public String getName()
   {
      return this.name;
   }

}
