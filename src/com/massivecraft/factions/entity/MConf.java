package com.massivecraft.factions.entity;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.engine.EngineChat;
import com.massivecraft.factions.event.EventFactionsChunkChangeType;
import com.massivecraft.massivecore.collections.BackstringSet;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeInner;
import com.massivecraft.massivecore.command.type.TypeMillisDiff;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EditorName("config")
public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public MConf load(MConf that)
	{
		super.load(that);
		
		// Reactivate EngineChat if currently active.
		// This way some event listeners are registered with the correct priority based on the config.
		EngineChat engine = EngineChat.get();
		if (engine.isActive())
		{
			engine.setActive(false);
			engine.setActive(true);
		}
		
		return this;
	}

	// -------------------------------------------- //
	// VERSION
	// -------------------------------------------- //
	
	public int version = 3;
	
	// -------------------------------------------- //
	// COMMAND ALIASES
	// -------------------------------------------- //
	
	// Don't you want "f" as the base command alias? Simply change it here.
	public List<String> aliasesF = MUtil.list("f");
	
	// -------------------------------------------- //
	// WORLDS FEATURE ENABLED
	// -------------------------------------------- //
	
	// Use this blacklist/whitelist system to toggle features on a per world basis.
	// Do you only want claiming enabled on the one map called "Hurr"?
	// In such case set standard to false and add "Hurr" as an exeption to worldsClaimingEnabled.
	public WorldExceptionSet worldsClaimingEnabled = new WorldExceptionSet();
	public WorldExceptionSet worldsPowerLossEnabled = new WorldExceptionSet();
	public WorldExceptionSet worldsPowerGainEnabled = new WorldExceptionSet();
	public WorldExceptionSet worldsPvpRulesEnabled = new WorldExceptionSet();
	
	// -------------------------------------------- //
	// DERPY OVERRIDES
	// -------------------------------------------- //
	
	// Add player names here who should bypass all protections.
	// Should /not/ be used for admins. There is "/f adminmode" for that.
	// This is for other plugins/mods that use a fake player to take actions, which shouldn't be subject to our protections.
	public Set<String> playersWhoBypassAllProtection = new MassiveSet<>();
	
	// -------------------------------------------- //
	// TASKS
	// -------------------------------------------- //
	
	// Define the time in minutes between certain Factions system tasks is ran.
	public double taskPlayerPowerUpdateMinutes = 1;
	public double taskPlayerDataRemoveMinutes = 5;
	public double taskEconLandRewardMinutes = 20;
	public double taskRemindBaseRegionMinutes = 5;
	public double taskRingFactionAlarmMinutes = 0.25;
	
	// -------------------------------------------- //
	// REMOVE DATA
	// -------------------------------------------- //
	
	// Should players be kicked from their faction and their data erased when banned?
	public boolean removePlayerWhenBanned = true;
	public boolean onlyRemovePlayersWhenPermanentlyBanned = true;
	
	// After how many milliseconds should players be automatically kicked from their faction?
	
	// The Default
	@EditorType(TypeMillisDiff.class)
	public long cleanInactivityToleranceMillis = 10 * TimeUnit.MILLIS_PER_DAY; // 10 days
	
	// Player Age Bonus
	@EditorTypeInner({TypeMillisDiff.class, TypeMillisDiff.class})
	public Map<Long, Long> cleanInactivityToleranceMillisPlayerAgeToBonus = MUtil.map(
		2 * TimeUnit.MILLIS_PER_WEEK, 10 * TimeUnit.MILLIS_PER_DAY  // +10 days after 2 weeks
	);
	
	// Faction Age Bonus
	@EditorTypeInner({TypeMillisDiff.class, TypeMillisDiff.class})
	public Map<Long, Long> cleanInactivityToleranceMillisFactionAgeToBonus = MUtil.map(
		4 * TimeUnit.MILLIS_PER_WEEK, 10 * TimeUnit.MILLIS_PER_DAY, // +10 days after 4 weeks
		2 * TimeUnit.MILLIS_PER_WEEK,  5 * TimeUnit.MILLIS_PER_DAY  // +5 days after 2 weeks
	);
	
	// -------------------------------------------- //
	// DEFAULTS
	// -------------------------------------------- //
	
	// Which faction should new players be followers of?
	// "none" means Wilderness. Remember to specify the id, like "3defeec7-b3b1-48d9-82bb-2a8903df24e3" and not the name.
	public String defaultPlayerFactionId = Factions.ID_NONE;
	
	// What rank should new players joining a faction get?
	// If not RECRUIT then MEMBER might make sense.
	public Rel defaultPlayerRole = Rel.RECRUIT;
	
	// What power should the player start with?
	public double defaultPlayerPower = 100.0;
	
	// -------------------------------------------- //
	// MOTD
	// -------------------------------------------- //
	
	// During which event priority should the faction message of the day be displayed?
	// Choose between: LOWEST, LOW, NORMAL, HIGH, HIGHEST and MONITOR.
	// This setting only matters if "motdDelayTicks" is set to -1
	public EventPriority motdPriority = EventPriority.NORMAL;
	
	// How many ticks should we delay the faction message of the day with?
	// -1 means we don't delay at all. We display it at once.
	// 0 means it's deferred to the upcoming server tick.
	// 5 means we delay it yet another 5 ticks.
	public int motdDelayTicks = -1;

	// -------------------------------------------- //
	// POWER
	// -------------------------------------------- //
	
	// What is the maximum player power?
	public double powerMax = 100.0;
	
	// What is the minimum player power?
	// NOTE: Negative minimum values is possible.
	public double powerMin = 0.0;
	
	// How much power should be regained per hour online on the server?
	public double powerPerHour = 20.0;
	
	// How much power should be lost on death?
	public double powerPerDeath = -20.0;
	
	// Can players with negative power leave their faction?
	// NOTE: This only makes sense to set to false if your "powerMin" setting is negative.
	public boolean canLeaveWithNegativePower = true;

	// -------------------------------------------- //
	// FLY
	// -------------------------------------------- //

	public boolean usePearlsFlying = false;
	public int maxFlyHeight = 280;
	public int flyXZCheck = 60;
	public int flyYCheck = 60;

	// -------------------------------------------- //
	// WARPS
	// -------------------------------------------- //

	public int amountOfWarps = 5;
	public boolean warpsMustBeInClaimedTerritory = true;
	public int warpWarmup = 10;

	// -------------------------------------------- //
	// MISSIONS
	// -------------------------------------------- //

	public String missionGuiName = "<gray>Faction Missions";
	public int missionGuiSize = 27;
	public Material missionItemType = Material.WATCH;
	public String missionItemName = "<k><bold>Start Mission";
	public byte missionItemData = 0;
	public int missionItemSlot = 13;
	public List<String> missionItemLore = MUtil.list(
			"<n>Progress: <k>%progress%",
			"",
			"<n>Challenge: <k>%description%",
			"",
			"<n>Reward: <k>%reward% tokens",
			"",
			"<n>Time Remaining: <k>%time%"
	);
	public int missionDeadlineHours = 4;

	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //

	// Is there a maximum amount of members per faction?
	// 0 means there is not. If you set it to 100 then there can at most be 100 members per faction.
	public int factionMemberLimit = 20;

	// Is there a maximum amount of alts per faction?
	// 0 means there is not. If you set it to 100 then there can at most be 100 alts per faction.
	public int factionAltLimit = 0;
	
	// Is there a maximum faction power cap?
	// 0 means there is not. Set it to a positive value in case you want to use this feature.
	public double factionPowerMax = 0.0;

	// Limit the length of faction names here.
	public int factionNameLengthMin = 3;
	public int factionNameLengthMax = 16;
	
	// Should faction names automatically be converted to upper case?
	// You probably don't want this feature.
	// It's a remnant from old faction versions.
	public boolean factionNameForceUpperCase = false;
	
	// -------------------------------------------- //
	// SET LIMITS
	// -------------------------------------------- //
	
	// When using radius setting of faction territory, what is the maximum radius allowed?
	public int setRadiusMax = 30;
	
	// When using fill setting of faction territory, what is the maximum chunk count allowed?
	public int setFillMax = 1000;

	// When using line setting of faction territory, what is the maximum length allowed?
	public int setLineMax = 35;
	
	// -------------------------------------------- //
	// CLAIMS
	// -------------------------------------------- //
	
	// Must claims be connected to each other?
	// If you set this to false you will allow factions to claim more than one base per world map.
	// That would makes outposts possible but also potentially ugly weird claims messing up your Dynmap and ingame experiance.
	public boolean claimsMustBeConnected = false;
	
	// Would you like to allow unconnected claims when conquering land from another faction?
	// Setting this to true would allow taking over someone elses base even if claims normally have to be connected.
	// Note that even without this you can pillage/unclaim another factions territory in war.
	// You just won't be able to take the land as your own.
	public boolean claimsCanBeUnconnectedIfOwnedByOtherFaction = false;
	
	// Is claiming from other factions even allowed?
	// Set this to false to disable territorial warfare altogether.
	public boolean claimingFromOthersAllowed = true;
	
	// Is a minimum distance (measured in chunks) to other factions required?
	// 0 means the feature is disabled.
	// Set the feature to 10 and there must be 10 chunks of wilderness between factions.
	// Factions may optionally allow their allies to bypass this limit by configuring their faction permissions ingame themselves.
	public int claimMinimumChunksDistanceToOthers = 0;
	
	// Do you need a minimum amount of faction members to claim land?
	// 1 means just the faction leader alone is enough.
	public int claimsRequireMinFactionMembers = 1;
	
	// Is there a maximum limit to chunks claimed?
	// 0 means there isn't.
	public int claimedLandsMax = 0;
	
	// The max amount of worlds in which a player can have claims in.
	public int claimedWorldsMax = -1;

	// The maximum distance to claim from using click to claim map.
	public int maximumClaimDistance = 20;
	
	// -------------------------------------------- //
	// PROTECTION
	// -------------------------------------------- //
	
	public boolean protectionLiquidFlowEnabled = true;
	
	// -------------------------------------------- //
	// HOMES
	// -------------------------------------------- //
	
	// Is the home feature enabled?
	// If you set this to false players can't set homes or teleport home.
	public boolean homesEnabled = true;
	
	// Must homes be located inside the faction's territory?
	// It's usually a wise idea keeping this true.
	// Otherwise players can set their homes inside enemy territory.
	public boolean homesMustBeInClaimedTerritory = true;
	
	// Is the home teleport command available?
	// One reason you might set this to false is if you only want players going home on respawn after death.
	public boolean homesTeleportCommandEnabled = true;
	
	// These options can be used to limit rights to tp home under different circumstances.
	public boolean homesTeleportAllowedFromEnemyTerritory = true;
	public boolean homesTeleportAllowedFromDifferentWorld = true;
	public double homesTeleportAllowedEnemyDistance = 32.0;
	public boolean homesTeleportIgnoreEnemiesIfInOwnTerritory = true;
	
	// Should players teleport to faction home on death?
	// Set this to true to override the default respawn location.
	public boolean homesTeleportToOnDeathActive = false;
	
	// This value can be used to tweak compatibility with other plugins altering the respawn location.
	// Choose between: LOWEST, LOW, NORMAL, HIGH, HIGHEST and MONITOR.
	public EventPriority homesTeleportToOnDeathPriority = EventPriority.NORMAL;
	
	// -------------------------------------------- //
	// TERRITORY INFO
	// -------------------------------------------- //
	
	public boolean territoryInfoTitlesDefault = true;
	public String territoryInfoTitlesMain = "{relcolor}{name}";
	public String territoryInfoTitlesSub = "<i>{desc}";
	public int territoryInfoTitlesTicksIn = 5;
	public int territoryInfoTitlesTicksStay = 60;
	public int territoryInfoTitleTicksOut = 5;
	public String territoryInfoChat = "<i> ~ {relcolor}{name} <i>{desc}";
	
	// -------------------------------------------- //
	// ASSORTED
	// -------------------------------------------- //
	
	// Set this to true if want to block the promotion of new leaders for permanent factions.
	// I don't really understand the user case for this option.
	public boolean permanentFactionsDisableLeaderPromotion = false;
	
	// How much health damage should a player take upon placing or breaking a block in a "pain build" territory?
	// 2.0 means one heart.
	public double actionDeniedPainAmount = 2.0D;
	
	// If you set this option to true then factionless players cant partake in PVP.
	// It works in both directions. Meaning you must join a faction to hurt players and get hurt by players.
	public boolean disablePVPForFactionlessPlayers = false;
	
	// If you set this option to true then factionless players cant damage each other.
	// So two factionless can't PvP, but they can PvP with others if that is allowed.
	public boolean enablePVPBetweenFactionlessPlayers = true;
	
	// Set this option to true to create an exception to the rule above.
	// Players inside their own faction territory can then hurt facitonless players.
	// This way you may "evict" factionless trolls messing around in your home base.
	public boolean enablePVPAgainstFactionlessInAttackersLand = false;
	
	// Inside your own faction territory you take less damage.
	// 0.1 means that you take 10% less damage at home.
	public double territoryShieldFactor = 0.1D;
	
	// Protects the faction land from piston extending/retracting
	// through the denying of MPerm build
	public boolean handlePistonProtectionThroughDenyBuild = true;
	
	// -------------------------------------------- //
	// DENY COMMANDS
	// -------------------------------------------- //
	
	// A list of commands to block for members of permanent factions.
	// I don't really understand the user case for this option.
	public List<String> denyCommandsPermanentFactionMember = new ArrayList<>();

	// Lists of commands to deny depending on your relation to the current faction territory.
	// You may for example not type /home (might be the plugin Essentials) in the territory of your enemies.
	public Map<Rel, List<String>> denyCommandsTerritoryRelation = MUtil.map(
		Rel.ENEMY, MUtil.list(
			// Essentials commands
			"home",
			"homes",
			"sethome",
			"createhome",
			"tpahere",
			"tpaccept",
			"tpyes",
			"tpa",
			"call",
			"tpask",
			"warp",
			"warps",
			"spawn",
			// Essentials e-alliases
			"ehome",
			"ehomes",
			"esethome",
			"ecreatehome",
			"etpahere",
			"etpaccept",
			"etpyes",
			"etpa",
			"ecall",
			"etpask",
			"ewarp",
			"ewarps",
			"espawn",
			// Essentials fallback alliases
			"essentials:home",
			"essentials:homes",
			"essentials:sethome",
			"essentials:createhome",
			"essentials:tpahere",
			"essentials:tpaccept",
			"essentials:tpyes",
			"essentials:tpa",
			"essentials:call",
			"essentials:tpask",
			"essentials:warp",
			"essentials:warps",
			"essentials:spawn",
			// Other plugins
			"wtp",
			"uspawn",
			"utp",
			"mspawn",
			"mtp",
			"fspawn",
			"ftp",
			"jspawn",
			"jtp"
		),
		Rel.NEUTRAL, new ArrayList<String>(),
		Rel.TRUCE, new ArrayList<String>(),
		Rel.ALLY, new ArrayList<String>(),
		Rel.MEMBER, new ArrayList<String>()
	);
	
	// The distance for denying the following commands. Set to -1 to disable.
	public double denyCommandsDistance = -1;
	
	// Lists of commands to deny depending on your relation to a nearby enemy in the above distance.
	public Map<Rel, List<String>> denyCommandsDistanceRelation = MUtil.map(
		Rel.ENEMY, MUtil.list("home"),
		Rel.NEUTRAL, new ArrayList<String>(),
		Rel.TRUCE, new ArrayList<String>(),
		Rel.ALLY, new ArrayList<String>(),
		Rel.MEMBER, new ArrayList<String>()
	);
	
	// Allow bypassing the above setting when in these territories.
	public List<Rel> denyCommandsDistanceBypassIn = MUtil.list(
		Rel.MEMBER, Rel.ALLY
	);
	
	// -------------------------------------------- //
	// CHAT
	// -------------------------------------------- //
	
	// Should Factions set the chat format?
	// This should be kept at false if you use an external chat format plugin.
	// If you are planning on running a more lightweight server you can set this to true.
	public boolean chatSetFormat = true;
	
	// At which event priority should the chat format be set in such case?
	// Choose between: LOWEST, LOW, NORMAL, HIGH and HIGHEST.
	public EventPriority chatSetFormatAt = EventPriority.LOWEST;
	
	// What format should be set?
	public String chatSetFormatTo = "<{factions_relcolor}§l{factions_roleprefix}§r{factions_relcolor}{factions_name|rp}§f%1$s> %2$s";
	
	// Should the chat tags such as {factions_name} be parsed?
	// NOTE: You can set this to true even with chatSetFormat = false.
	// But in such case you must set the chat format using an external chat format plugin.
	public boolean chatParseTags = true;
	
	// At which event priority should the faction chat tags be parsed in such case?
	// Choose between: LOWEST, LOW, NORMAL, HIGH, HIGHEST.
	public EventPriority chatParseTagsAt = EventPriority.LOW;

	// What should the format be when a player speaks with faction chat?
	public String chatFormat = "%s:" + ChatColor.WHITE + " %s";

	// What should the chat spy format be?
	public String spyChatFormat = "<i>[Faction Spy] %s <i>sent a <h>%s <i>message: <a>%s";

	// -------------------------------------------- //
	// TNT
	// -------------------------------------------- //

	public int maximumFillRadius = 20;

	// What is the minimum and maximum tnt to be added to /f tnt.
	public Map<EntityType, List<Integer>> tntChances = MUtil.map(
			EntityType.CREEPER, MUtil.list(1, 3)
	);

	public boolean autoDepositTnt = false;

	// How much should TNT sell for?
	public double tntSellPrice = 5;

	// -------------------------------------------- //
	// ROSTER
	// -------------------------------------------- //

	public int rosterMemberLimit = 25;
	public int rosterKickLimit = 5;

	// -------------------------------------------- //
	// DRAIN
	// -------------------------------------------- //

	public Rel drainRank = Rel.MEMBER;

	// -------------------------------------------- //
	// SAND ALTS
	// -------------------------------------------- //

	public String sandAltName = "&c&lSAND ALT";
	public String altSkin = "Steve";
	public String sandAltGuiName = "<gray>Faction Sand Alts";
	public int sandAltGuiSize = 54;
	public double sandCost = 10.0D;
	public Material sandSpawnMaterial = Material.LAPIS_BLOCK;
	public int sandSpawnRadius = 6;
	public long sandSpawnDelay = 500L;
	
	// -------------------------------------------- //
	// COLORS
	// -------------------------------------------- //
	
	// Here you can alter the colors tied to certain faction relations and settings.
	// You probably don't want to edit these to much.
	// Doing so might confuse players that are used to Factions.
	public ChatColor colorMember = ChatColor.GREEN;
	public ChatColor colorAlly = ChatColor.DARK_PURPLE;
	public ChatColor colorTruce = ChatColor.LIGHT_PURPLE;
	public ChatColor colorNeutral = ChatColor.WHITE;
	public ChatColor colorEnemy = ChatColor.RED;
	public ChatColor colorWilderness = ChatColor.DARK_GREEN;
	public ChatColor colorFocused = ChatColor.BLUE;
	
	// This one is for example applied to SafeZone since that faction has the pvp flag set to false.
	public ChatColor colorNoPVP = ChatColor.GOLD;
	
	// This one is for example applied to WarZone since that faction has the friendly fire flag set to true.
	public ChatColor colorFriendlyFire = ChatColor.DARK_RED;

	// -------------------------------------------- //
	// LIMITS
	// -------------------------------------------- //

	public int truceLimit = 1;
	public int allyLimit = 0;

	// -------------------------------------------- //
	// PREFIXES
	// -------------------------------------------- //
	
	// Here you may edit the name prefixes associated with different faction ranks.
	public String prefixLeader = "***";
	public String prefixColeader = "**";
	public String prefixOfficer = "*";
	public String prefixMember = "+";
	public String prefixRecruit = "-";
	
	// -------------------------------------------- //
	// EXPLOITS
	// -------------------------------------------- //
	
	public boolean handleExploitObsidianGenerators = true;
	public boolean handleExploitEnderPearlClipping = true;
	public boolean handleExploitTNTWaterlog = false;
	public boolean handleNetherPortalTrap = true;
	
	// -------------------------------------------- //
	// SEE CHUNK
	// -------------------------------------------- //
	
	// These options can be used to tweak the "/f seechunk" particle effect.
	// They are fine as is but feel free to experiment with them if you want to.
	
	// Use 1 or multiple of 3, 4 or 5.
	public int seeChunkSteps = 1;
	
	// White/Black List for creating sparse patterns.
	public int seeChunkKeepEvery = 5;
	public int seeChunkSkipEvery = 0;
	
	@EditorType(TypeMillisDiff.class)
	public long seeChunkPeriodMillis = 500;
	public int seeChunkParticleAmount = 30;
	public float seeChunkParticleOffsetY = 2;
	public float seeChunkParticleDeltaY = 2;
	
	// -------------------------------------------- //
	// UNSTUCK
	// -------------------------------------------- //
	
	public int unstuckSeconds = 30;
	public int unstuckChunkRadius = 10; 
	
	// -------------------------------------------- //
	// LOGGING
	// -------------------------------------------- //
	
	// Here you can disable logging of certain events to the server console.
	
	public boolean logFactionCreate = true;
	public boolean logFactionDisband = true;
	public boolean logFactionInvite = true;
	public boolean logFactionJoin = true;
	public boolean logFactionKick = true;
	public boolean logFactionLeave = true;
	public boolean logFactionMute = true;
	public boolean logFactionBan = true;
	public boolean logFactionUnban = true;
	public boolean logLandClaims = true;
	public boolean logMoneyTransactions = true;
	public boolean logCreditsTransactions = true;
	public boolean logTntTransactions = true;

	// -------------------------------------------- //
	// ENUMERATIONS
	// -------------------------------------------- //
	// In this configuration section you can add support for Forge mods that add new Materials and EntityTypes.
	// This way they can be protected in Faction territory.
	// Use the "UPPER_CASE_NAME" for the Material or EntityType in question.
	// If you are running a regular Spigot server you don't have to edit this section.
	// In fact all of these sets can be empty on regular Spigot servers without any risk.
	
	// Interacting with these materials when they are already placed in the terrain results in an edit.
	public BackstringSet<Material> materialsEditOnInteract = new BackstringSet<>(Material.class);
	
	// Interacting with the the terrain holding this item in hand results in an edit.
	// There's no need to add all block materials here. Only special items other than blocks.
	public BackstringSet<Material> materialsEditTools = new BackstringSet<>(Material.class);
	
	// Interacting with these materials placed in the terrain results in door toggling.
	public BackstringSet<Material> materialsDoor = new BackstringSet<>(Material.class);
	
	// Interacting with these materials placed in the terrain results in opening a container.
	public BackstringSet<Material> materialsContainer = new BackstringSet<>(Material.class);

	// Interacting with these materials placed in the terrain results in placing an explosive.
	public BackstringSet<Material> materialsExplosive = new BackstringSet<>(Material.class);
	
	// Interacting with these entities results in an edit.
	public BackstringSet<EntityType> entityTypesEditOnInteract = new BackstringSet<>(EntityType.class);
	
	// Damaging these entities results in an edit.
	public BackstringSet<EntityType> entityTypesEditOnDamage = new BackstringSet<>(EntityType.class);
	
	// Interacting with these entities results in opening a container.
	public BackstringSet<EntityType> entityTypesContainer = new BackstringSet<>(EntityType.class);
	
	// The complete list of entities considered to be monsters.
	public BackstringSet<EntityType> entityTypesMonsters = new BackstringSet<>(EntityType.class);
	
	// List of entities considered to be animals.
	public BackstringSet<EntityType> entityTypesAnimals = new BackstringSet<>(EntityType.class);

	// List of entities to add to faction money.
	public BackstringSet<EntityType> entityTypesMoney = new BackstringSet<>(EntityType.class,
			"PIG_ZOMBIE"
	);

	// List of entities to add to faction tnt.
	public BackstringSet<EntityType> entityTypesTnt = new BackstringSet<>(EntityType.class,
			"CREEPER"
	);
	
	// -------------------------------------------- //
	// INTEGRATION: WorldGuard
	// -------------------------------------------- //
	
	// Global WorldGuard Integration Switch
	public boolean worldguardCheckEnabled = false;
	
	// Enable the WorldGuard check per-world 
	// Specify which worlds the WorldGuard Check can be used in
	public WorldExceptionSet worldguardCheckWorldsEnabled = new WorldExceptionSet();

	// -------------------------------------------- //
	// INTEGRATION: CoreProtect
	// -------------------------------------------- //

	public int inspectResultLimit = 100;
	
	// -------------------------------------------- //
	// INTEGRATION: ECONOMY
	// -------------------------------------------- //
	
	// Should economy features be enabled?
	// This requires that you have the external plugin called "Vault" installed.
	public boolean econEnabled = true;
	
	// A money reward per chunk. This reward is divided among the players in the faction.
	// You set the time inbetween each reward almost at the top of this config file. (taskEconLandRewardMinutes)
	public double econLandReward = 0.00;
	
	// When paying a cost you may specify an account that should receive the money here.
	// Per default "" the money is just destroyed.
	public String econUniverseAccount = "";
	
	// What is the price per chunk when using /f set?
	public Map<EventFactionsChunkChangeType, Double> econChunkCost = MUtil.map(
		EventFactionsChunkChangeType.BUY, 0.0, // when claiming from wilderness
		EventFactionsChunkChangeType.SELL, 0.0, // when selling back to wilderness
		EventFactionsChunkChangeType.CONQUER, 0.0, // when claiming from another player faction
		EventFactionsChunkChangeType.PILLAGE, 0.0 // when unclaiming (to wilderness) from another player faction
	);
	
	// What is the price to create a faction?
	public double econCostCreate = 0.0;
	
	// And so on and so forth ... you get the idea.
	public double econCostSethome = 0.0;
	public double econCostJoin = 0.0;
	public double econCostLeave = 0.0;
	public double econCostKick = 0.0;
	public double econCostInvite = 0.0;
	public double econCostDeinvite = 0.0;
	public double econCostHome = 0.0;
	public double econCostName = 0.0;
	public double econCostDescription = 0.0;
	public double econCostTitle = 0.0;
	public double econCostFlag = 0.0;
	public double econCostSetwarp = 10000.0;
	public double econCostBan = 0.0;
	public double econCostUnban = 0.0;

	public Map<Rel, Double> econRelCost = MUtil.map(
		Rel.ENEMY, 0.0,
		Rel.ALLY, 0.0,
		Rel.TRUCE, 0.0,
		Rel.NEUTRAL, 0.0
	);
	
	// Should the faction bank system be enabled?
	// This enables the command /f money.
	public boolean bankEnabled = true;
	
	// That costs should the faciton bank take care of?
	// If you set this to false the player executing the command will pay instead.
	public boolean bankFactionPaysCosts = true;

	// -------------------------------------------- //
	// INTEGRATION: HOLOGRAPHIC DISPLAYS
	// -------------------------------------------- //

	// How long is the Hologram update period?
	// Requires a server restart to take effect.
	public int holographicDisplayUpdatePeriod = 30 * 20;

	// -------------------------------------------- //
	// MONEY
	// -------------------------------------------- //

	public Map<EntityType, List<Integer>> moneyChances = MUtil.map(
			EntityType.PIG_ZOMBIE, MUtil.list(20, 30)
	);

	public boolean autoSellMobs = false;

	// -------------------------------------------- //
	// ITEMS
	// -------------------------------------------- //

	public Material fillerItemMaterial = Material.STAINED_GLASS_PANE;
	public String fillerItemName = " ";
	public byte fillerItemData = 8;

	// -------------------------------------------- //
	// LOGINS
	// -------------------------------------------- //

	public String loginFormat = "%s <g>has logged in.";
	public String logoffFormat = "%s <g>has logged off.";

	// -------------------------------------------- //
	// SHIELDS
	// -------------------------------------------- //

	public int shieldHours = 10;

	// -------------------------------------------- //
	// ALARM
	// -------------------------------------------- //

	public String alarmSound = "NOTE_BASS";
	public float alarmVolume = 1.0f;
	public float alarmPitch = 1.0f;

}
