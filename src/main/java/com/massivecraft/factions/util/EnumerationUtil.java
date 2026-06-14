package com.massivecraft.factions.util;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.collections.BackstringSet;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;

public class EnumerationUtil
{
	// -------------------------------------------- //
	// MATERIAL EDIT ON INTERACT
	// -------------------------------------------- //
	
	public static final BackstringSet<Material> MATERIALS_EDIT_ON_INTERACT = new BackstringSet<>(Material.class,
		"DIODE_BLOCK_OFF", // Minecraft 1.?
		"DIODE_BLOCK_ON", // Minecraft 1.?
		"NOTE_BLOCK", // Minecraft 1.?
		"CAULDRON", // Minecraft 1.?
		"SOIL", // Minecraft 1.?
		"DAYLIGHT_DETECTOR", // Minecraft 1.5
		"DAYLIGHT_DETECTOR_INVERTED", // Minecraft 1.5
		"REDSTONE_COMPARATOR_OFF", // Minecraft 1.?
		"REDSTONE_COMPARATOR_ON" // Minecraft 1.?
	);
	
	public static boolean isMaterialEditOnInteract(Material material)
	{
		return MATERIALS_EDIT_ON_INTERACT.contains(material) || MConf.get().materialsEditOnInteract.contains(material);
	}
	
	// -------------------------------------------- //
	// MATERIAL EDIT TOOLS
	// -------------------------------------------- //
	
	public static final BackstringSet<Material> MATERIALS_EDIT_TOOL = new BackstringSet<>(Material.class,
		"FIREBALL", // Minecraft 1.?
		"FIRE_CHARGE", // Minecraft 1.13
		"FLINT_AND_STEEL", // Minecraft 1.?
		"BUCKET", // Minecraft 1.?
		"WATER_BUCKET", // Minecraft 1.?
		"LAVA_BUCKET", // Minecraft 1.?
		"POWDER_SNOW_BUCKET", // Minecraft 1.17
		"COD_BUCKET", // Minecraft 1.13
		"SALMON_BUCKET", // Minecraft 1.13
		"PUFFERFISH_BUCKET", // Minecraft 1.13
		"TROPICAL_FISH_BUCKET", // Minecraft 1.13
		"AXOLOTL_BUCKET", // Minecraft 1.17
		"TADPOLE_BUCKET", // Minecraft 1.19
		"GLOW_INK_SAC", // Minecraft 1.17
		"ARMOR_STAND", // Minecraft 1.8
		"END_CRYSTAL", // Minecraft 1.10

		// The duplication bug found in Spigot 1.8 protocol patch
		// https://github.com/MassiveCraft/Factions/issues/693
		"CHEST", // Minecraft 1.?
		"SIGN_POST", // Minecraft 1.?
		"TRAPPED_CHEST", // Minecraft 1.?
		"SIGN" // Minecraft 1.?
	);
	
	public static boolean isMaterialEditTool(Material material)
	{
		return MATERIALS_EDIT_TOOL.contains(material) || MConf.get().materialsEditTools.contains(material);
	}
	
	// -------------------------------------------- //
	// MATERIAL DOOR
	// -------------------------------------------- //
	
	// Interacting with these materials placed in the terrain results in door toggling.
	public static final BackstringSet<Material> MATERIALS_DOOR = new BackstringSet<>(Material.class,
		"WOODEN_DOOR", // Minecraft 1.?
		"ACACIA_DOOR", // Minecraft 1.8
		"BIRCH_DOOR", // Minecraft 1.8
		"DARK_OAK_DOOR", // Minecraft 1.8
		"JUNGLE_DOOR", // Minecraft 1.8
		"SPRUCE_DOOR", // Minecraft 1.8
		"TRAP_DOOR", // Minecraft 1.?
		"FENCE_GATE", // Minecraft 1.?
		"ACACIA_FENCE_GATE", // Minecraft 1.8
		"BIRCH_FENCE_GATE", // Minecraft 1.8
		"DARK_OAK_FENCE_GATE", // Minecraft 1.8
		"JUNGLE_FENCE_GATE", // Minecraft 1.8
		"SPRUCE_FENCE_GATE" // Minecraft 1.8
	);
	
	public static boolean isMaterialDoor(Material material)
	{
		if (material == null) return false;
		if (Tag.DOORS.isTagged(material)) return true;
		if (Tag.TRAPDOORS.isTagged(material)) return true;
		if (Tag.FENCE_GATES.isTagged(material)) return true;
		return MATERIALS_DOOR.contains(material) || MConf.get().materialsDoor.contains(material);
	}
	
	// -------------------------------------------- //
	// MATERIAL CONTAINER
	// -------------------------------------------- //
	
	public static final BackstringSet<Material> MATERIALS_CONTAINER = new BackstringSet<>(Material.class,
		"DISPENSER", // Minecraft 1.?
		"CHEST", // Minecraft 1.?
		"FURNACE", // Minecraft 1.?
		"BURNING_FURNACE", // Minecraft 1.?
		"JUKEBOX", // Minecraft 1.?
		"BREWING_STAND", // Minecraft 1.?
		"ENCHANTMENT_TABLE", // Minecraft 1.?
		"ANVIL", // Minecraft 1.?
		"BEACON", // Minecraft 1.?
		"TRAPPED_CHEST", // Minecraft 1.?
		"HOPPER", // Minecraft 1.?
		"DROPPER", // Minecraft 1.?
		"CHIPPED_ANVIL", // Minecraft 1.?
		"DAMAGED_ANVIL", // Minecraft 1.?
		"ENCHANTING_TABLE", // Minecraft 1.13

		// Storage blocks added since the flattening.
		"BARREL", // Minecraft 1.14
		"BLAST_FURNACE", // Minecraft 1.14
		"SMOKER", // Minecraft 1.14
		"CAMPFIRE", // Minecraft 1.14
		"SOUL_CAMPFIRE", // Minecraft 1.16
		"CHISELED_BOOKSHELF", // Minecraft 1.20
		"DECORATED_POT", // Minecraft 1.20
		"CRAFTER", // Minecraft 1.21 (stores items)

		// The various shulker boxes, they had to make each one a different material -.-
		"SHULKER_BOX", // Minecraft 1.11
		"BLACK_SHULKER_BOX", // Minecraft 1.11
		"BLUE_SHULKER_BOX", // Minecraft 1.11
		"BROWN_SHULKER_BOX", // Minecraft 1.11
		"CYAN_SHULKER_BOX", // Minecraft 1.11
		"GRAY_SHULKER_BOX", // Minecraft 1.11
		"GREEN_SHULKER_BOX", // Minecraft 1.11
		"LIGHT_BLUE_SHULKER_BOX", // Minecraft 1.11
		"LIME_SHULKER_BOX", // Minecraft 1.11
		"MAGENTA_SHULKER_BOX", // Minecraft 1.11
		"ORANGE_SHULKER_BOX", // Minecraft 1.11
		"PINK_SHULKER_BOX", // Minecraft 1.11
		"PURPLE_SHULKER_BOX", // Minecraft 1.11
		"RED_SHULKER_BOX", // Minecraft 1.11
		"SILVER_SHULKER_BOX", // Minecraft 1.11
		"WHITE_SHULKER_BOX", // Minecraft 1.11
		"YELLOW_SHULKER_BOX" // Minecraft 1.11
	);

	// -------------------------------------------- //
	// MATERIAL EXPLOSIVE
	// -------------------------------------------- //

	public static final BackstringSet<Material> MATERIALS_EXPLOSIVE = new BackstringSet<>(Material.class,
			"TNT", // Minecraft 1.?
			"FIREBALL", // Minecraft 1.?
			"MONSTER_EGG" // Minecraft 1.?
	);

	public static boolean isMaterialExplosive(Material material)
	{
		return MATERIALS_EXPLOSIVE.contains(material) || MConf.get().materialsExplosive.contains(material);
	}
	
	public static boolean isMaterialContainer(Material material)
	{
		return MATERIALS_CONTAINER.contains(material) || MConf.get().materialsContainer.contains(material);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE EDIT ON INTERACT
	// -------------------------------------------- //
	
	// Interacting with these entities results in an edit.
	public static final BackstringSet<EntityType> ENTITY_TYPES_EDIT_ON_INTERACT = new BackstringSet<>(EntityType.class,
		"ITEM_FRAME", // Minecraft 1.?
		"ARMOR_STAND" // Minecraft 1.8
	);
	
	public static boolean isEntityTypeEditOnInteract(EntityType entityType)
	{
		return ENTITY_TYPES_EDIT_ON_INTERACT.contains(entityType) || MConf.get().entityTypesEditOnInteract.contains(entityType);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE EDIT ON DAMAGE
	// -------------------------------------------- //
	
	// Damaging these entities results in an edit.
	public static final BackstringSet<EntityType> ENTITY_TYPES_EDIT_ON_DAMAGE = new BackstringSet<>(EntityType.class,
		"ITEM_FRAME", // Minecraft 1.?
		"ARMOR_STAND", // Minecraft 1.8
		"ENDER_CRYSTAL" // Minecraft 1.10
	);
	
	public static boolean isEntityTypeEditOnDamage(EntityType entityType)
	{
		return ENTITY_TYPES_EDIT_ON_DAMAGE.contains(entityType) || MConf.get().entityTypesEditOnDamage.contains(entityType);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE CONTAINER
	// -------------------------------------------- //
	
	public static final BackstringSet<EntityType> ENTITY_TYPES_CONTAINER = new BackstringSet<>(EntityType.class,
		"MINECART_CHEST", // Minecraft 1.?
		"MINECART_HOPPER", // Minecraft 1.?
		"CHEST_MINECART", // Minecraft 1.13
		"HOPPER_MINECART", // Minecraft 1.13
		"CHEST_BOAT", // Minecraft 1.19
		"OAK_CHEST_BOAT", // Minecraft 1.21.2
		"SPRUCE_CHEST_BOAT", // Minecraft 1.21.2
		"BIRCH_CHEST_BOAT", // Minecraft 1.21.2
		"JUNGLE_CHEST_BOAT", // Minecraft 1.21.2
		"ACACIA_CHEST_BOAT", // Minecraft 1.21.2
		"DARK_OAK_CHEST_BOAT", // Minecraft 1.21.2
		"MANGROVE_CHEST_BOAT", // Minecraft 1.21.2
		"CHERRY_CHEST_BOAT", // Minecraft 1.21.2
		"PALE_OAK_CHEST_BOAT", // Minecraft 1.21.4
		"BAMBOO_CHEST_RAFT" // Minecraft 1.21.2
	);
	
	public static boolean isEntityTypeContainer(EntityType entityType)
	{
		return ENTITY_TYPES_CONTAINER.contains(entityType) || MConf.get().entityTypesContainer.contains(entityType);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE MONSTER
	// -------------------------------------------- //
	
	public static final BackstringSet<EntityType> ENTITY_TYPES_MONSTER = new BackstringSet<>(EntityType.class,
		"BLAZE", // Minecraft 1.?
		"CAVE_SPIDER", // Minecraft 1.?
		"CREEPER", // Minecraft 1.?
		"ELDER_GUARDIAN", // minecraft 1.11
		"ENDERMAN", // Minecraft 1.?
		"ENDERMITE", // Minecraft 1.8
		"ENDER_DRAGON", // Minecraft 1.?
		"EVOKER", // Minecraft 1.11
		"GUARDIAN", // Minecraft 1.8
		"GHAST", // Minecraft 1.?
		"GIANT", // Minecraft 1.?
		"HUSK", // Minecraft 1.11
		"MAGMA_CUBE", // Minecraft 1.?
		"PIG_ZOMBIE", // Minecraft 1.?
		"POLAR_BEAR", // Minecraft 1.10
		"SILVERFISH", // Minecraft 1.?
		"SHULKER", // Minecraft 1.10
		"SKELETON", // Minecraft 1.?
		"SLIME", // Minecraft 1.?
		"SPIDER", // Minecraft 1.?
		"STRAY", // Minecraft 1.11
		"VINDICATOR", // Minecraft 1.11
		"VEX", // Minecraft 1.11
		"WITCH", // Minecraft 1.?
		"WITHER", // Minecraft 1.?
		"WITHER_SKELETON", // Minecraft 1.11
		"ZOMBIE", // Minecraft 1.?
		"ZOMBIE_VILLAGER", // Minecraft 1.11
		"ILLUSIONER", // Minecraft 1.12
		// Hostiles added since 1.13.
		"DROWNED", // Minecraft 1.13
		"PHANTOM", // Minecraft 1.13
		"PILLAGER", // Minecraft 1.14
		"RAVAGER", // Minecraft 1.14
		"ZOMBIFIED_PIGLIN", // Minecraft 1.16
		"PIGLIN", // Minecraft 1.16
		"PIGLIN_BRUTE", // Minecraft 1.16.2
		"HOGLIN", // Minecraft 1.16
		"ZOGLIN", // Minecraft 1.16
		"WARDEN", // Minecraft 1.19
		"BREEZE", // Minecraft 1.21
		"BOGGED", // Minecraft 1.21
		"CREAKING" // Minecraft 1.21.4
	);
	
	public static boolean isEntityTypeMonster(EntityType entityType)
	{
		return ENTITY_TYPES_MONSTER.contains(entityType) || MConf.get().entityTypesMonsters.contains(entityType);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE ANIMAL
	// -------------------------------------------- //
	
	public static final BackstringSet<EntityType> ENTITY_TYPES_ANIMAL = new BackstringSet<>(EntityType.class,
		"BAT", // Minecraft 1.?
		"CHICKEN", // Minecraft 1.?
		"COW", // Minecraft 1.?
		"DONKEY", // Minecraft 1.11
		"HORSE", // Minecraft 1.?
		"LLAMA", // Minecraft 1.11
		"MULE", // Minecraft 1.11
		"MUSHROOM_COW", // Minecraft 1.?
		"OCELOT", // Minecraft 1.?
		"PIG", // Minecraft 1.?
		"RABBIT", // Minecraft 1.?
		"SHEEP", // Minecraft 1.?
		"SKELETON_HORSE", // Minecraft 1.11
		"SQUID", // Minecraft 1.?
		"WOLF", // Minecraft 1.?
		"ZOMBIE_HORSE", // Minecraft 1.11
		"PARROT", // Minecraft 1.12
		// Passives added since 1.13.
		"TURTLE", // Minecraft 1.13
		"COD", // Minecraft 1.13
		"SALMON", // Minecraft 1.13
		"PUFFERFISH", // Minecraft 1.13
		"TROPICAL_FISH", // Minecraft 1.13
		"DOLPHIN", // Minecraft 1.13
		"PANDA", // Minecraft 1.14
		"FOX", // Minecraft 1.14
		"BEE", // Minecraft 1.15
		"CAT", // Minecraft 1.14
		"TRADER_LLAMA", // Minecraft 1.14
		"WANDERING_TRADER", // Minecraft 1.14
		"STRIDER", // Minecraft 1.16
		"AXOLOTL", // Minecraft 1.17
		"GLOW_SQUID", // Minecraft 1.17
		"GOAT", // Minecraft 1.17
		"FROG", // Minecraft 1.19
		"TADPOLE", // Minecraft 1.19
		"ALLAY", // Minecraft 1.19
		"CAMEL", // Minecraft 1.20
		"SNIFFER", // Minecraft 1.20
		"ARMADILLO" // Minecraft 1.21
	);
	
	public static boolean isEntityTypeAnimal(EntityType entityType)
	{
		return ENTITY_TYPES_ANIMAL.contains(entityType) || MConf.get().entityTypesAnimals.contains(entityType);
	}
	
}
