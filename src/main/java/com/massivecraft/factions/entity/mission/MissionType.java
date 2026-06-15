package com.massivecraft.factions.entity.mission;

public enum MissionType
{

   // Combat - resolved from EntityDeathEvent in EngineMission.
   KILL_ENTITY,       // target = entity type name (comma separated for variants)
   KILL_ANY_HOSTILE,  // any Monster, target ignored
   KILL_PLAYER,       // any player, target ignored
   KILL_BOSS,         // target = boss entity type name

   // Mining / gathering - resolved from BlockBreakEvent in EngineMission.
   MINE_ANY,          // any block, target ignored
   BREAK_BLOCK,       // target = material name (comma separated for variants)
   CHOP_LOG,          // any *_LOG / *_STEM, target ignored
   HARVEST_CROP,      // target = crop material name (vertical column counted)
   FISH,              // PlayerFishEvent, target ignored

   // Misc / progression.
   GAIN_EXP,          // PlayerExpChangeEvent, increment by xp amount
   TRAVEL,            // PlayerMoveEvent, one per block moved
   ENCHANT,           // EnchantItemEvent
   SMELT;             // FurnaceExtractEvent, increment by item amount

}
