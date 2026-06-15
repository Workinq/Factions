package com.massivecraft.factions.entity.mission;

public enum Rarity
{

   COMMON(60, "<white>", "Common"),
   UNCOMMON(25, "<green>", "Uncommon"),
   RARE(10, "<aqua>", "Rare"),
   LEGENDARY(5, "<gold>", "Legendary");

   private final int weight;
   private final String color;
   private final String displayName;

   Rarity(int weight, String color, String displayName)
   {
      this.weight = weight;
      this.color = color;
      this.displayName = displayName;
   }

   public int getWeight()
   {
      return this.weight;
   }

   public String getColor()
   {
      return this.color;
   }

   public String getDisplayName()
   {
      return this.displayName;
   }

}
