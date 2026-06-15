package com.massivecraft.factions.entity.mission;

import org.bukkit.Material;

public class Mission
{

   // -------------------------------------------- //
   // FIELDS
   // -------------------------------------------- //

   private final String name;
   private final MissionType type;
   // The target entity/material this mission tracks. May be null/"ANY" to match anything.
   // Multiple targets can be comma separated to cover variants (e.g. an ore and its deepslate form).
   private final String target;
   private final Rarity rarity;
   private final double goal;
   private final int reward;
   private final String itemName;
   private final Material itemMaterial;
   private final String description;

   // -------------------------------------------- //
   // CONSTRUCT
   // -------------------------------------------- //

   public Mission(String name, MissionType type, String target, Rarity rarity, double goal, int reward, String itemName, Material itemMaterial, String description)
   {
      this.name = name;
      this.type = type;
      this.target = target;
      this.rarity = rarity;
      this.goal = goal;
      this.reward = reward;
      this.itemName = itemName;
      this.itemMaterial = itemMaterial;
      this.description = description;
   }

   // -------------------------------------------- //
   // GETTERS (names kept compatible with the old AbstractMission)
   // -------------------------------------------- //

   public String getMissionName() { return this.name; }

   public String getDescription() { return this.description; }

   public double getRequirement() { return this.goal; }

   public int getReward() { return this.reward; }

   public Material getItemMaterial() { return this.itemMaterial; }

   public String getItemName() { return this.itemName; }

   public MissionType getType() { return this.type; }

   public String getTarget() { return this.target; }

   public Rarity getRarity() { return this.rarity; }

   public int getWeight() { return this.rarity.getWeight(); }

   // -------------------------------------------- //
   // MATCHING
   // -------------------------------------------- //

   public boolean matchesTarget(String name)
   {
      if (this.target == null || this.target.equalsIgnoreCase("ANY")) return true;
      for (String token : this.target.split(","))
      {
         if (token.trim().equalsIgnoreCase(name)) return true;
      }
      return false;
   }

}
