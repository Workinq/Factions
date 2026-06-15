package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.mission.Mission;
import com.massivecraft.factions.entity.mission.MissionType;
import com.massivecraft.factions.entity.mission.Rarity;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@EditorName("config")
public class MMission extends Entity<MMission>
{

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MMission i;
    public static MMission get() { return i; }

    // -------------------------------------------- //
    // REWARDS BY RARITY
    // -------------------------------------------- //

    private static final int REWARD_COMMON = 5000;
    private static final int REWARD_UNCOMMON = 10000;
    private static final int REWARD_RARE = 25000;
    private static final int REWARD_LEGENDARY = 75000;

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    // Missions are defined in code (transient) and matched centrally by EngineMission.
    // Adding a new mission is a single entry here - no new class required.
    public transient List<Mission> missions = MUtil.list(

        // ---- Combat ----
        new Mission("Zombie", MissionType.KILL_ENTITY, "ZOMBIE", Rarity.COMMON, 2000, REWARD_COMMON, "Slay Zombies", Material.ROTTEN_FLESH, "Slay 2,000 Zombies"),
        new Mission("Skeleton", MissionType.KILL_ENTITY, "SKELETON", Rarity.COMMON, 2000, REWARD_COMMON, "Slay Skeletons", Material.BONE, "Slay 2,000 Skeletons"),
        new Mission("Spider", MissionType.KILL_ENTITY, "SPIDER", Rarity.COMMON, 1500, REWARD_COMMON, "Slay Spiders", Material.STRING, "Slay 1,500 Spiders"),
        new Mission("Creeper", MissionType.KILL_ENTITY, "CREEPER", Rarity.UNCOMMON, 1000, REWARD_UNCOMMON, "Slay Creepers", Material.GUNPOWDER, "Slay 1,000 Creepers"),
        new Mission("Blaze", MissionType.KILL_ENTITY, "BLAZE", Rarity.UNCOMMON, 2500, REWARD_UNCOMMON, "Slay Blazes", Material.BLAZE_ROD, "Slay 2,500 Blazes"),
        new Mission("Witch", MissionType.KILL_ENTITY, "WITCH", Rarity.UNCOMMON, 750, REWARD_UNCOMMON, "Slay Witches", Material.POTION, "Slay 750 Witches"),
        new Mission("Enderman", MissionType.KILL_ENTITY, "ENDERMAN", Rarity.RARE, 750, REWARD_RARE, "Slay Endermen", Material.ENDER_PEARL, "Slay 750 Endermen"),
        new Mission("Wither Skeleton", MissionType.KILL_ENTITY, "WITHER_SKELETON", Rarity.RARE, 500, REWARD_RARE, "Slay Wither Skeletons", Material.WITHER_SKELETON_SKULL, "Slay 500 Wither Skeletons"),
        new Mission("Slayer", MissionType.KILL_ANY_HOSTILE, null, Rarity.COMMON, 5000, REWARD_COMMON, "Slay Hostiles", Material.IRON_SWORD, "Slay 5,000 hostile mobs"),
        new Mission("Bounty Hunter", MissionType.KILL_PLAYER, null, Rarity.RARE, 100, REWARD_RARE, "Slay Players", Material.DIAMOND_SWORD, "Slay 100 enemy players"),
        new Mission("Wither Slayer", MissionType.KILL_BOSS, "WITHER", Rarity.LEGENDARY, 10, REWARD_LEGENDARY, "Slay the Wither", Material.NETHER_STAR, "Slay 10 Withers"),
        new Mission("Dragon Slayer", MissionType.KILL_BOSS, "ENDER_DRAGON", Rarity.LEGENDARY, 3, REWARD_LEGENDARY, "Slay the Dragon", Material.DRAGON_EGG, "Slay the Ender Dragon 3 times"),

        // ---- Mining / Gathering ----
        new Mission("Trench", MissionType.MINE_ANY, null, Rarity.COMMON, 20000, REWARD_COMMON, "Mine", Material.DIAMOND_PICKAXE, "Mine 20,000 Blocks"),
        new Mission("Coal", MissionType.BREAK_BLOCK, "COAL_ORE,DEEPSLATE_COAL_ORE", Rarity.COMMON, 3000, REWARD_COMMON, "Mine Coal Ore", Material.COAL, "Mine 3,000 Coal Ore"),
        new Mission("Iron", MissionType.BREAK_BLOCK, "IRON_ORE,DEEPSLATE_IRON_ORE", Rarity.UNCOMMON, 2000, REWARD_UNCOMMON, "Mine Iron Ore", Material.RAW_IRON, "Mine 2,000 Iron Ore"),
        new Mission("Gold", MissionType.BREAK_BLOCK, "GOLD_ORE,DEEPSLATE_GOLD_ORE,NETHER_GOLD_ORE", Rarity.UNCOMMON, 1500, REWARD_UNCOMMON, "Mine Gold Ore", Material.RAW_GOLD, "Mine 1,500 Gold Ore"),
        new Mission("Diamond", MissionType.BREAK_BLOCK, "DIAMOND_ORE,DEEPSLATE_DIAMOND_ORE", Rarity.RARE, 1000, REWARD_RARE, "Mine Diamond Ore", Material.DIAMOND, "Mine 1,000 Diamond Ore"),
        new Mission("Emerald", MissionType.BREAK_BLOCK, "EMERALD_ORE,DEEPSLATE_EMERALD_ORE", Rarity.RARE, 500, REWARD_RARE, "Mine Emerald Ore", Material.EMERALD, "Mine 500 Emerald Ore"),
        new Mission("Ancient Debris", MissionType.BREAK_BLOCK, "ANCIENT_DEBRIS", Rarity.LEGENDARY, 250, REWARD_LEGENDARY, "Mine Ancient Debris", Material.ANCIENT_DEBRIS, "Mine 250 Ancient Debris"),
        new Mission("Lumberjack", MissionType.CHOP_LOG, null, Rarity.COMMON, 5000, REWARD_COMMON, "Chop Logs", Material.OAK_LOG, "Chop 5,000 Logs"),
        new Mission("Sugarcane", MissionType.HARVEST_CROP, "SUGAR_CANE", Rarity.COMMON, 25000, REWARD_COMMON, "Harvest Sugarcane", Material.SUGAR_CANE, "Harvest 25,000 Sugarcane"),
        new Mission("Wheat Farmer", MissionType.HARVEST_CROP, "WHEAT", Rarity.COMMON, 10000, REWARD_COMMON, "Harvest Wheat", Material.WHEAT, "Harvest 10,000 Wheat"),
        new Mission("Nether Wart", MissionType.HARVEST_CROP, "NETHER_WART", Rarity.UNCOMMON, 5000, REWARD_UNCOMMON, "Harvest Nether Wart", Material.NETHER_WART, "Harvest 5,000 Nether Wart"),
        new Mission("Angler", MissionType.FISH, null, Rarity.UNCOMMON, 1000, REWARD_UNCOMMON, "Catch Fish", Material.FISHING_ROD, "Catch 1,000 Fish"),

        // ---- Misc / Progression ----
        new Mission("EXP", MissionType.GAIN_EXP, null, Rarity.COMMON, 29315, REWARD_COMMON, "Earn EXP", Material.EXPERIENCE_BOTTLE, "Earn 100 EXP Levels"),
        new Mission("Travel", MissionType.TRAVEL, null, Rarity.COMMON, 60000, REWARD_COMMON, "Travel", Material.DIAMOND_BOOTS, "Take 60,000 Steps"),
        new Mission("Smelter", MissionType.SMELT, null, Rarity.COMMON, 5000, REWARD_COMMON, "Smelt Items", Material.FURNACE, "Smelt 5,000 Items"),
        new Mission("Enchanter", MissionType.ENCHANT, null, Rarity.UNCOMMON, 500, REWARD_UNCOMMON, "Enchant Items", Material.ENCHANTING_TABLE, "Enchant 500 Items")

    );

    // -------------------------------------------- //
    // FIELD: missions
    // -------------------------------------------- //

    public List<Mission> getMissions()
    {
        return new ArrayList<>(missions);
    }

    public Mission getMissionByName(String string)
    {
        if (string == null) return null;
        for (Mission mission : missions)
        {
            if (mission.getMissionName().equalsIgnoreCase(string))
            {
                return mission;
            }
        }
        return null;
    }

    // -------------------------------------------- //
    // SELECTION
    // -------------------------------------------- //

    // Pre-calculated, rarity-weighted pick. This is the single source of truth for
    // which mission a faction receives - the GUI reel only displays the result.
    public Mission selectWeighted()
    {
        if (missions.isEmpty()) return null;

        int total = 0;
        for (Mission mission : missions) total += mission.getWeight();

        int roll = ThreadLocalRandom.current().nextInt(total);
        int cumulative = 0;
        for (Mission mission : missions)
        {
            cumulative += mission.getWeight();
            if (roll < cumulative) return mission;
        }
        return missions.get(missions.size() - 1);
    }

    // -------------------------------------------- //
    // PROGRESS
    // -------------------------------------------- //

    public void incrementProgress(Mission mission, MPlayer mplayer) { this.incrementProgress(mission, mplayer, 1); }
    public void incrementProgress(Mission mission, MPlayer mplayer, Integer amount) { this.incrementProgress(mission, mplayer.getFaction(), amount); }
    public void incrementProgress(Mission mission, Faction faction) { this.incrementProgress(mission, faction, 1); }
    public void incrementProgress(Mission mission, Faction faction, Integer amount)
    {
        // Verify
        if (faction.getActiveMission() == null) return;
        if (!faction.getActiveMission().getMissionName().equalsIgnoreCase(mission.getMissionName())) return;
        if (faction.getMissionStart() != 0 && System.currentTimeMillis() > faction.getMissionStart() + (TimeUnit.MILLIS_PER_HOUR * MConf.get().missionDeadlineHours)) return;

        // Args
        Integer missionComplete = faction.getMissionGoal();
        if ((double) (missionComplete + amount) >= mission.getRequirement())
        {
            Integer credits = mission.getReward();
            faction.addCredits(credits);
            faction.setActiveMission(null);
            faction.setMissionGoal(0);
            faction.msg("%s <g>has received <h>%,d <g>credits for completing the <h>%s <g>mission.", faction.describeTo(faction), credits, mission.getMissionName());
        }
        else
        {
            faction.setMissionGoal(missionComplete + amount);
        }
    }

}
