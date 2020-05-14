package com.massivecraft.factions.entity;

import com.massivecraft.factions.*;
import com.massivecraft.factions.entity.object.*;
import com.massivecraft.factions.entity.object.Invitation;
import com.massivecraft.factions.mission.Mission;
import com.massivecraft.factions.mission.MissionsManager;
import com.massivecraft.factions.predicate.PredicateCommandSenderFaction;
import com.massivecraft.factions.predicate.PredicateMPlayerRole;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.util.SerializationUtil;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateAnd;
import com.massivecraft.massivecore.predicate.PredicateVisibleTo;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.store.EntityInternalMap;
import com.massivecraft.massivecore.store.SenderColl;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.*;
import java.util.Map.Entry;

public class Faction extends Entity<Faction> implements FactionsParticipator
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	public static final transient String NODESCRIPTION = Txt.parse("<em><silver>no description set");
	public static final transient String NOMOTD = Txt.parse("<em><silver>no message of the day set");

	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	public static Faction get(Object oid)
	{
		return FactionColl.get().get(oid);
	}

	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //

	@Override
	public Faction load(Faction that)
	{
		this.setName(that.name);
		this.setDescription(that.description);
		this.setMotd(that.motd);
		this.setCreatedAtMillis(that.createdAtMillis);
		this.setHome(that.home);
		this.setPowerBoost(that.powerBoost);
		this.invitations.load(that.invitations);
		this.setRelationWishes(that.relationWishes);
		this.setFlagIds(that.flags);
		this.setPermIds(that.perms);
		this.setTnt(that.tnt);
		this.setInventorySerialized(that.inventorySerialized);
		this.setChestActions(that.chestActions);
		this.setWarps(that.warps);
		this.setPaypal(that.paypal);
		this.setDiscord(that.discord);
		this.bannedMembers.load(that.bannedMembers);
		this.setUpgrades(that.upgrades);
		this.setMissionGoal(that.missionGoal);
		this.setActiveMission(that.activeMission);
		this.setMissionStart(that.missionStart);
		this.setCredits(that.credits);
		this.setStrikes(that.strikes);
		this.setBaseRegion(that.baseRegion);
		this.setShieldedHour(that.shieldedHour);
		this.setShieldString(that.shieldString);
		this.setFocusedPlayer(that.focusedPlayer);
		this.setShards(that.shards);
		this.setBanner(that.banner);
		this.setRoster(that.roster);
		this.setSandAlts(that.sandAlts);
		return this;
	}

	@Override
	public void preDetach(String id)
	{
		if (!this.isLive()) return;

		// NOTE: Existence check is required for compatibility with some plugins.
		// If they have money ...
		if (Money.exists(this))
		{
			// ... remove it.
			Money.set(this, null, 0, "Factions");
		}
	}

	// -------------------------------------------- //
	// VERSION
	// -------------------------------------------- //

	public int version = 1;

	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	// In this section of the source code we place the field declarations only.
	// Each field has it's own section further down since just the getter and setter logic takes up quite some place.

	// The actual faction id looks something like "54947df8-0e9e-4471-a2f9-9af509fb5889" and that is not too easy to remember for humans.
	// Thus we make use of a name. Since the id is used in all foreign key situations changing the name is fine.
	// Null should never happen. The name must not be null.
	private String name = null;

	// Factions can optionally set a description for themselves.
	// This description can for example be seen in territorial alerts.
	// Null means the faction has no description.
	private String description = null;

	// Factions can optionally set a message of the day.
	// This message will be shown when logging on to the server.
	// Null means the faction has no motd
	private String motd = null;

	// We store the creation date for the faction.
	// It can be displayed on info pages etc.
	private long createdAtMillis = System.currentTimeMillis();

	// Factions can optionally set a home location.
	// If they do their members can teleport there using /f home
	// Null means the faction has no home.
	private PS home = null;

	// Factions usually do not have a powerboost. It defaults to 0.
	// The powerBoost is a custom increase/decrease to default and maximum power.
	// Null means the faction has powerBoost (0).
	private Double powerBoost = null;

	// Factions will have a tnt bank which, by default, starts at 0 tnt.
	// When a player uses /f tnt deposit <amount>, the tnt in the faction bank will increase.
	private Integer tnt = null;

	// Factions will have their own faction chest in which they can store contents.
	// By default the chest will contain 27 slots however, the faction can upgrade this.
	// When first creating a faction, the chest will be empty.
	private transient Inventory inventory = null;

	// This is the serialized string for the inventories contents.
	// This cannot corrupt in the slightest or the entire inventory will reset.
	// Or worse, the server will spit errors non-stop.
	private String inventorySerialized = null;

	// This contains all the interactions that have been made with the faction chest.
	// Whenever a player takes or adds an item to the chest, an action will be added.
	// These items can later be viewed using /f chest log <page>.
	private MassiveSet<ChestAction> chestActions = new MassiveSet<>();

	// This contains all the faction warps which have been set.
	// By default this is empty however, warps can be added using /f setwarp <warp name>.
	// These warps can later be viewed using /f warp list.
	private MassiveSet<FactionWarp> warps = new MassiveSet<>();

	// This will store the faction's paypal to payout to.
	// By default it's empty and they should set it using /f setpaypal <email>.
	// Null means there is no paypal set.
	private String paypal = null;

	// This will store the faction's discord which members can join.
	// By default it's empty and should be set using /f setdiscord <link>.
	// Null means no discord has been set.
	private String discord = null;

	// This will store the current mission being done by the faction.
	// Null means there is no mission active.
	private String activeMission = null;

	// This will store the time when the mission was started.
	// 0L will mean there is no ongoing mission.
	private long missionStart = 0L;

	// This will store the current progress for the faction's current mission.
	// Null either means there is no mission ongoing or there is no progress.
	private Integer missionGoal = null;

	// This will store faction upgrades along with their level.
	private MassiveMap<String, Integer> upgrades = new MassiveMap<>();

	// This stores the faction's credits.
	// Credits are awarded when a faction completes a mission.
	// By default the faction starts with 0 credits.
	private int credits = 0;

	// This stores the faction's shards.
	// Shards are awarded when a mob dies in faction territory.
	// By default the faction starts with 0 shards.
	private int shards = 0;

	// This will store a list of strikes the faction has acquired.
	// A strike can be given using /f strike <faction> <points> <reason>.
	private MassiveSet<FactionStrike> strikes = new MassiveSet<>();

	// The faction's base region will be stored here.
	// Using /f setbaseregion will run a loop to save chunks in a 60x60 radius from the sender.
	// Empty most likely means the base region is empty.
	private MassiveSet<PS> baseRegion = new MassiveSet<>();

	// This will store the hour for which the faction is protected.
	// Null means no hour has been set.
	private Integer shieldedHour = null;

	// This will store the string which shows in /f show.
	// Example: 5:00 PM ---> 3:00 AM
	// Null means no shield is set.
	private String shieldString = null;

	// Stores the uuid of the focused player.
	// Null means no player has been focused.
	private String focusedPlayer = null;

	// This stores the patterns for the faction banner.
	// By default there are 0 patterns (i think).
	private MassiveList<String> banner = new MassiveList<>();

	// This will store the faction roster, you can add and remove members from the roster using /f roster.
	// By default the roster will be empty however, the owner might be in it.
	private MassiveMap<String, Rel> roster = new MassiveMap<>();

	// This will store the faction's currently active sand alts.
	// By default there will be none, obviously.
	private MassiveSet<SandAlt> sandAlts = new MassiveSet<>();

	// This will store a list of all the banned members.
	// By default it's empty and members can be banned using /f ban <player>.
	private final EntityInternalMap<FactionBan> bannedMembers = new EntityInternalMap<>(this, FactionBan.class);

	// Can anyone join the Faction?
	// If the faction is open they can.
	// If the faction is closed an invite is required.
	// Null means default.
	// private Boolean open = null;

	// This is the ids of the invited players.
	// They are actually "senderIds" since you can invite "@console" to your faction.
	// Null means no one is invited
	private final EntityInternalMap<Invitation> invitations = new EntityInternalMap<>(this, Invitation.class);

	// The keys in this map are factionIds.
	// Null means no special relation whishes.
	private MassiveMapDef<String, Rel> relationWishes = new MassiveMapDef<>();

	// The flag overrides are modifications to the default values.
	// Null means default.
	private MassiveMapDef<String, Boolean> flags = new MassiveMapDef<>();

	// The perm overrides are modifications to the default values.
	// Null means default.
	private MassiveMapDef<String, Set<Rel>> perms = new MassiveMapDef<>();

	// -------------------------------------------- //
	// FIELD: id
	// -------------------------------------------- //

	// FINER

	public boolean isNone()
	{
		return this.getId().equals(Factions.ID_NONE);
	}

	public boolean isNormal()
	{
		return ! this.isNone();
	}

	public boolean isSystemFaction()
	{
		return this.getId().equals(Factions.ID_NONE) || this.getId().equals(Factions.ID_WARZONE) || this.getId().equals(Factions.ID_SAFEZONE);
	}

	public boolean isFull()
	{
		return this.getMPlayers().size() == MConf.get().factionMemberLimit;
	}

	// -------------------------------------------- //
	// FIELD: name
	// -------------------------------------------- //

	// RAW

	@Override
	public String getName()
	{
		String ret = this.name;

		if (MConf.get().factionNameForceUpperCase)
		{
			ret = ret.toUpperCase();
		}

		return ret;
	}

	public void setName(String name)
	{
		// Clean input
		String target = name;

		// Detect Nochange
		if (MUtil.equals(this.name, target)) return;

		// Apply
		this.name = target;

		// Mark as changed
		this.changed();
	}

	// FINER

	public String getComparisonName()
	{
		return MiscUtil.getComparisonString(this.getName());
	}

	public String getName(String prefix)
	{
		return prefix + this.getName();
	}

	public String getName(RelationParticipator observer)
	{
		if (observer == null) return getName();
		return this.getName(this.getColorTo(observer).toString());
	}

	// -------------------------------------------- //
	// FIELD: description
	// -------------------------------------------- //

	// RAW

	public boolean hasDescription()
	{
		return this.description != null;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		// Clean input
		String target = clean(description);

		// Detect Nochange
		if (MUtil.equals(this.description, target)) return;

		// Apply
		this.description = target;

		// Mark as changed
		this.changed();
	}

	// FINER

	public String getDescriptionDesc()
	{
		String motd = this.getDescription();
		if (motd == null) motd = NODESCRIPTION;
		return motd;
	}

	// -------------------------------------------- //
	// FIELD: motd
	// -------------------------------------------- //

	// RAW

	public boolean hasMotd()
	{
		return this.motd != null;
	}

	public String getMotd()
	{
		return this.motd;
	}

	public void setMotd(String motd)
	{
		// Clean input
		String target = clean(motd);

		// Detect Nochange
		if (MUtil.equals(this.motd, target)) return;

		// Apply
		this.motd = target;

		// Mark as changed
		this.changed();
	}

	// FINER

	public String getMotdDesc()
	{
		return getMotdDesc(this.getMotd());
	}

	private static String getMotdDesc(String motd)
	{
		if (motd == null) motd = NOMOTD;
		return motd;
	}

	public List<Object> getMotdMessages()
	{
		// Create
		List<Object> ret = new MassiveList<>();

		// Fill
		Object title = this.getName() + " - Message of the Day";
		title = Txt.titleize(title);
		ret.add(title);

		String motd = Txt.parse("<i>") + this.getMotdDesc();
		ret.add(motd);

		ret.add("");

		// Return
		return ret;
	}

	// -------------------------------------------- //
	// FIELD: createdAtMillis
	// -------------------------------------------- //

	public long getCreatedAtMillis()
	{
		return this.createdAtMillis;
	}

	public void setCreatedAtMillis(long createdAtMillis)
	{
		// Clean input
		long target = createdAtMillis;

		// Detect Nochange
		if (MUtil.equals(this.createdAtMillis, createdAtMillis)) return;

		// Apply
		this.createdAtMillis = target;

		// Mark as changed
		this.changed();
	}

	public long getAge()
	{
		return this.getAge(System.currentTimeMillis());
	}

	public long getAge(long now)
	{
		return now - this.getCreatedAtMillis();
	}

	// -------------------------------------------- //
	// FIELD: home
	// -------------------------------------------- //

	public PS getHome()
	{
		this.verifyHomeIsValid();
		return this.home;
	}

	public void verifyHomeIsValid()
	{
		if (this.isValidHome(this.home)) return;
		this.home = null;
		this.changed();
		msg("<b>Your faction home has been un-set since it is no longer in your territory.");
	}

	public boolean isValidHome(PS ps)
	{
		if (ps == null) return true;
		if (!MConf.get().homesMustBeInClaimedTerritory) return true;
		return BoardColl.get().getFactionAt(ps) == this;
	}

	public boolean hasHome()
	{
		return this.getHome() != null;
	}

	public void setHome(PS home)
	{
		// Clean input
		PS target = home;

		// Detect Nochange
		if (MUtil.equals(this.home, target)) return;

		// Apply
		this.home = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: powerBoost
	// -------------------------------------------- //

	// RAW
	@Override
	public double getPowerBoost()
	{
		Double ret = this.powerBoost;
		if (ret == null) ret = 0D;
		return ret;
	}

	@Override
	public void setPowerBoost(Double powerBoost)
	{
		// Clean input
		Double target = powerBoost;

		if (target == null || target == 0) target = null;

		// Detect Nochange
		if (MUtil.equals(this.powerBoost, target)) return;

		// Apply
		this.powerBoost = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: tnt
	// -------------------------------------------- //

	public int getTnt()
	{
		Integer ret = this.tnt;
		if (ret == null) ret = 0;
		return ret;
	}

	public void setTnt(Integer tnt)
	{
		// Clean input
		Integer target = tnt;

		if (target == null || target == 0) target = null;

		// Detect Nochange
		if (MUtil.equals(this.tnt, target)) return;

		// Apply
		this.tnt = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: inventory
	// -------------------------------------------- //

	public void saveInventory()
	{
		if (inventory == null) return;
		this.setInventorySerialized(SerializationUtil.toBase64(inventory));
	}

	public Inventory getInventory()
	{
		Inventory ret = inventory;
		if (ret == null)
		{
			String inventorySerialized = this.getInventorySerialized();
			if (inventorySerialized == null) inventorySerialized = "";
			ret = SerializationUtil.fromBase64(inventorySerialized, Txt.parse("<gray>%s - Faction Chest", this.getName()));
			// Set inventory.
			this.inventory = ret;
		}
		return ret;
	}

	public void setInventory(Inventory inventory)
	{
		Inventory target = inventory;

		// Detect Nochange
		if (MUtil.equals(this.inventory, target)) return;

		// Apply
		this.inventory = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: inventorySerialized
	// -------------------------------------------- //

	public String getInventorySerialized()
	{
		String ret = this.inventorySerialized;
		if (ret == null) ret = "";
		return ret;
	}

	public void setInventorySerialized(String inventorySerialized)
	{
		String target = inventorySerialized;

		if (target == null || target.equals("")) target = null;

		// Detect Nochange
		if (MUtil.equals(this.inventorySerialized, target)) return;

		// Apply
		this.inventorySerialized = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: chestActions
	// -------------------------------------------- //

	public MassiveSet<ChestAction> getChestActions()
	{
		return chestActions;
	}

	public void addChestAction(ChestAction chestAction)
	{
		chestActions.add(chestAction);
		this.changed();
	}

	public void setChestActions(MassiveSet<ChestAction> chestActions)
	{
		this.chestActions = chestActions;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: warps
	// -------------------------------------------- //

	public MassiveSet<FactionWarp> getWarps()
	{
		return warps;
	}

	public void addWarp(FactionWarp warp)
	{
		warps.add(warp);

		// Mark as changed
		this.changed();
	}

	public void deleteWarp(FactionWarp warp)
	{
		warps.remove(warp);

		// Mark as changed
		this.changed();
	}

	public void setWarps(MassiveSet<FactionWarp> warps)
	{
		this.warps = warps;
	}

	public boolean verifyWarpIsValid(FactionWarp warp)
	{
		// Verify
		if (this.isValidWarp(warp.getLocation())) return true;

		// Apply
		this.deleteWarp(warp);

		// Mark as changed
		this.changed();

		// Inform
		msg("<b>The faction warp '%s' has been un-set since it is no longer in your territory.", warp.getName());

		// Return
		return false;
	}

	public boolean isValidWarp(PS ps)
	{
		if (ps == null) return true;
		if (!MConf.get().warpsMustBeInClaimedTerritory) return true;
		return BoardColl.get().getFactionAt(ps) == this;
	}

	public boolean warpExists(String warpName)
	{
		return this.warps.stream().anyMatch(warp -> warp.getName().equalsIgnoreCase(warpName));
	}

	// -------------------------------------------- //
	// FIELD: paypal
	// -------------------------------------------- //

	public String getPaypal()
	{
		String ret = this.paypal;
		if (ret == null) ret = "";
		return ret;
	}

	public void setPaypal(String paypal)
	{
		String target = paypal;

		if (target == null || target.equals("")) target = null;

		// Detect Nochange
		if (MUtil.equals(this.paypal, target)) return;

		// Apply
		this.paypal = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: discord
	// -------------------------------------------- //

	public String getDiscord()
	{
		String ret = this.discord;
		if (ret == null) ret = "";
		return ret;
	}

	public void setDiscord(String discord)
	{
		String target = discord;

		if (target == null || target.equals("")) target = null;

		// Detect Nochange
		if (MUtil.equals(this.discord, target)) return;

		// Apply
		this.discord = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: mission
	// -------------------------------------------- //

	public Mission getActiveMission()
	{
		return MissionsManager.get().getMissionByName(activeMission);
	}

	public void setActiveMission(String activeMission)
	{
		// Apply
		this.activeMission = activeMission;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: missionStart
	// -------------------------------------------- //

	public long getMissionStart()
	{
		return missionStart;
	}

	public void setMissionStart(long missionStart)
	{
		// Detect Nochange
		if (this.missionStart == missionStart) return;

		// Apply
		this.missionStart = missionStart;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: missionGoal
	// -------------------------------------------- //

	public Integer getMissionGoal()
	{
		Integer target = this.missionGoal;

		if (target == null) target = 0;

		return target;
	}

	public void setMissionGoal(Integer missionGoal)
	{
		// Clean input
		Integer target = missionGoal;

		if (target == null || target == 0.0D) target = null;

		// Detect Nochange
		if (MUtil.equals(this.missionGoal, target)) return;

		// Apply
		this.missionGoal = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: upgrades
	// -------------------------------------------- //

	public MassiveMap<String, Integer> getUpgrades()
	{
		return upgrades;
	}

	public void setUpgrades(MassiveMap<String, Integer> upgrades)
	{
		this.upgrades = upgrades;
	}

	public int getLevel(String upgrade)
	{
		return upgrades.get(upgrade) == null ? 0 : upgrades.get(upgrade);
	}

	public void increaseLevel(String upgrade)
	{
		// Args
		int newLevel = this.getLevel(upgrade) + 1;

		// Apply
		upgrades.remove(upgrade);
		upgrades.put(upgrade, newLevel);

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: credits
	// -------------------------------------------- //

	public int getCredits()
	{
		return credits;
	}

	public void setCredits(int credits)
	{
		// Detect Nochange
		if (this.credits == credits) return;

		// Apply
		this.credits = credits;

		// Mark as changed
		this.changed();
	}

	public void addCredits(int credits)
	{
		// Clean Input
		int newCredits = this.getCredits() + credits;

		// Detect Nochange
		if (this.credits == newCredits) return;

		// Apply
		this.setCredits(newCredits);

		// Mark as changed
		this.changed();
	}

	public void takeCredits(int credits)
	{
		// Clean Input
		int newCredits = this.getCredits() - credits;

		// Detect Negative
		if (newCredits < 0) newCredits = 0;

		// Apply
		this.setCredits(newCredits);

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: shards
	// -------------------------------------------- //

	public int getShards()
	{
		return shards;
	}

	public void setShards(int shards)
	{
		// Detect Nochange
		if (this.shards == shards) return;

		// Apply
		this.shards = shards;

		// Mark as changed
		this.changed();
	}

	public void addShards(int shards)
	{
		// Clean Input
		int newShards = this.getShards() + shards;

		// Detect Nochange
		if (this.shards == newShards) return;

		// Apply
		this.setShards(newShards);

		// Mark as changed
		this.changed();
	}

	public void takeShards(int shards)
	{
		// Clean Input
		int newShards = this.getShards() - shards;

		// Detect Negative
		if (newShards < 0) newShards = 0;

		// Apply
		this.setShards(newShards);

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: strikes
	// -------------------------------------------- //

	public MassiveSet<FactionStrike> getStrikes()
	{
		return strikes;
	}

	public void addStrike(FactionStrike strike)
	{
		strikes.add(strike);

		// Mark as changed
		this.changed();
	}

	public void deleteStrike(FactionStrike strike)
	{
		strikes.remove(strike);

		// Mark as changed
		this.changed();
	}

	public FactionStrike getStrikeFromId(String strikeId)
	{
		for (FactionStrike strike : this.strikes)
		{
			if (strike.getId().equals(strikeId)) return strike;
		}
		return null;
	}

	public int getStrikePoints()
	{
		int total = 0;
		for (FactionStrike strike : this.strikes)
		{
			total += strike.getPoints();
		}
		return total;
	}

	public void setStrikes(MassiveSet<FactionStrike> strikes)
	{
		this.strikes = strikes;
	}

	// -------------------------------------------- //
	// FIELD: baseRegion
	// -------------------------------------------- //

	public void setBaseRegion(MassiveSet<PS> baseRegion)
	{
		this.baseRegion = baseRegion;

		// Mark as changed
		this.changed();
	}

	public MassiveSet<PS> getBaseRegion()
	{
		return baseRegion;
	}

	// -------------------------------------------- //
	// FIELD: shieldedHour
	// -------------------------------------------- //

	public void setShieldedHour(Integer shieldedHour)
	{
		// Apply
		this.shieldedHour = shieldedHour;

		// Mark as changed
		this.changed();
	}

	public boolean isShieldedAt(int hour)
	{
		if (this.shieldedHour == null) return false;

		int minimum = this.shieldedHour;
		int maximum = this.shieldedHour + 10;

		if (maximum >= 24)
		{
			return hour < maximum % 24 || hour >= minimum;
		}
		else
		{
			return hour < maximum && hour >= minimum;
		}
	}

	public boolean isShieldedAtHour(int hour)
	{
		if (this.shieldedHour == null) return false;
		return this.shieldedHour == hour;
	}

	public boolean isShielded()
	{
		return this.shieldedHour != null;
	}

	public Integer getShieldedHour()
	{
		return shieldedHour;
	}

	// -------------------------------------------- //
	// FIELD: shieldString
	// -------------------------------------------- //

	public void setShieldString(String shieldString)
	{
		this.shieldString = shieldString;

		// Mark as changed
		this.changed();
	}

	public String getShieldString()
	{
		return shieldString;
	}

	// -------------------------------------------- //
	// FIELD: focusedPlayer
	// -------------------------------------------- //

	public void setFocusedPlayer(String focusedPlayer)
	{
		this.focusedPlayer = focusedPlayer;

		// Mark as changed
		this.changed();
	}

	public boolean isPlayerFocused(String uuid)
	{
		return this.focusedPlayer != null && this.focusedPlayer.equals(uuid);
	}

	public String getFocusedPlayer()
	{
		return focusedPlayer;
	}

	// -------------------------------------------- //
	// FIELD: banner
	// -------------------------------------------- //

	public void setBanner(List<String> patterns)
	{
		// Clear
		this.banner.clear();

		// Apply
		this.banner.addAll(patterns);

		// Mark as changed
		this.changed();
	}

	public void setBanner(MassiveList<String> patterns)
	{
		// Clear
		this.banner.clear();

		// Apply
		this.banner.addAll(patterns);

		// Mark as changed
		this.changed();
	}

	public ItemStack getBanner()
	{
		// Args
		ItemStack banner = new ItemStack(Material.BANNER);
		BannerMeta meta = (BannerMeta) banner.getItemMeta();
		DyeColor baseColor = DyeColor.valueOf(this.banner.get(0));

		// Pattern & Color
		meta.setBaseColor(baseColor);
		for (int i = 1; i < this.banner.size(); i++)
		{
			String[] temp = (this.banner).get(i).split(" ");
			String col = temp[0];
			String pat = temp[1];

			DyeColor color = DyeColor.BLACK;
			PatternType patterntype = PatternType.BASE;
			for (DyeColor dyeColor : DyeColor.values())
			{
				if (col.equalsIgnoreCase(dyeColor.name()))
				{
					color = dyeColor;
				}
			}
			for (PatternType patternType : PatternType.values())
			{
				if (pat.equalsIgnoreCase(patternType.toString()))
				{
					patterntype = patternType;
				}
			}
			Pattern pattern = new Pattern(color, patterntype);
			meta.addPattern(pattern);
		}

		// Lore
		List<String> lore = new ArrayList<>();
		lore.add(Txt.parse("<k>%s's Banner", this.getName()));
		meta.setLore(lore);

		// Apply
		banner.setItemMeta(meta);

		// Return
		return banner;
	}

	public boolean hasBanner()
	{
		return this.banner.size() != 0;
	}

	// -------------------------------------------- //
	// FIELD: roster
	// -------------------------------------------- //

	public MassiveMap<String, Rel> getRoster()
	{
		return roster;
	}

	public void addToRoster(MPlayer mplayer)
	{
		// Detect Nochange
		if (this.roster.containsKey(mplayer.getId())) return;

		// Apply
		this.roster.put(mplayer.getId(), Rel.RECRUIT);

		// Mark as changed
		this.changed();
	}

	public void removeFromRoster(MPlayer mplayer)
	{
		// Detect Nochange
		if ( ! this.roster.containsKey(mplayer.getId())) return;

		// Apply
		this.roster.remove(mplayer.getId());

		// Mark as changed
		this.changed();
	}

	public void setRosterRank(MPlayer mplayer, Rel role)
	{
		// Detect Nochange
		if (this.roster.get(mplayer.getId()) == role) return;

		// Apply
		this.roster.replace(mplayer.getId(), role);

		// Mark as changed
		this.changed();
	}

	public Rel getRosterRole(MPlayer mplayer)
	{
		return this.roster.get(mplayer.getId());
	}

	public boolean isInRoster(MPlayer mplayer)
	{
		return this.roster.containsKey(mplayer.getId());
	}

	public void setRoster(MassiveMap<String, Rel> roster)
	{
		this.roster = roster;
	}

	// -------------------------------------------- //
	// FIELD: sandAlts
	// -------------------------------------------- //

	public void setSandAlts(MassiveSet<SandAlt> sandAlts)
	{
		// Apply
		this.sandAlts = sandAlts;

		// Mark as changed
		this.changed();
	}

	public void addSandAlt(SandAlt sandAlt)
	{
		// Apply
		sandAlts.add(sandAlt);

		// Mark as changed
		this.changed();
	}

	public void despawnSandAlt(SandAlt sandAlt)
	{
		// Args
		NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(sandAlt.getNpcId());

		// Destroy
		if (npc != null)
		{
			npc.despawn(DespawnReason.PLUGIN);
			npc.destroy();
		}

		// Apply
		sandAlts.remove(sandAlt);

		// Mark as changed
		this.changed();
	}

	public SandAlt getSandAltAt(PS location)
	{
		for (SandAlt sandAlt : this.sandAlts)
		{
			if (sandAlt.getPs().equals(location)) return sandAlt;
		}
		return null;
	}

	public void startAllSandAlts()
	{
		for (SandAlt sandAlt : this.sandAlts)
		{
			if ( ! sandAlt.isPaused()) continue;
			sandAlt.setPaused(false);
			sandAlt.changed();
		}

		// Mark as changed
		this.changed();
	}

	public void stopAllSandAlts()
	{
		for (SandAlt sandAlt : this.sandAlts)
		{
			if (sandAlt.isPaused()) continue;
			sandAlt.setPaused(true);
			sandAlt.changed();
		}

		// Mark as changed
		this.changed();
	}

	public void despawnAllSandAlts()
	{
		// Loop - Sand Alts
		for (SandAlt sandAlt : new MassiveList<>(this.sandAlts))
		{
			this.despawnSandAlt(sandAlt);
		}

		// Mark as changed
		this.changed();
	}

	public Set<SandAlt> getSandAltsInChunk(PS chunk)
	{
		Set<SandAlt> sandAlts = new MassiveSet<>();
		for (SandAlt sandAlt : this.sandAlts)
		{
			if (sandAlt.getPs().getChunk(true).equals(chunk))
			{
				sandAlts.add(sandAlt);
			}
		}
		return sandAlts;
	}

	public MassiveSet<SandAlt> getSandAlts()
	{
		return sandAlts;
	}

	// -------------------------------------------- //
	// FIELD: bannedMembers
	// -------------------------------------------- //

	// RAW

	public EntityInternalMap<FactionBan> getBannedMembers() { return this.bannedMembers; }

	// FINER

	public boolean isBanned(String playerId)
	{
		return this.getBannedMembers().containsKey(playerId);
	}

	public boolean isBanned(MPlayer mplayer)
	{
		return this.isBanned(mplayer.getId());
	}

	public boolean unban(String playerId)
	{
		System.out.println(playerId);
		return this.getBannedMembers().detachId(playerId) != null;
	}

	public boolean unban(MPlayer mplayer)
	{
		return unban(mplayer.getId());
	}

	public void ban(String playerId, FactionBan factionBan)
	{
		unban(playerId);
		this.bannedMembers.attach(factionBan, playerId);
	}

	// -------------------------------------------- //
	// FIELD: open
	// -------------------------------------------- //

	// Nowadays this is a flag!

	@Deprecated
	public boolean isDefaultOpen()
	{
		return MFlag.getFlagOpen().isStandard();
	}

	@Deprecated
	public boolean isOpen()
	{
		return this.getFlag(MFlag.getFlagOpen());
	}

	@Deprecated
	public void setOpen(Boolean open)
	{
		MFlag flag = MFlag.getFlagOpen();
		if (open == null) open = flag.isStandard();
		this.setFlag(flag, open);
	}

	// -------------------------------------------- //
	// FIELD: invitedPlayerIds
	// -------------------------------------------- //

	// RAW

	public EntityInternalMap<Invitation> getInvitations() { return this.invitations; }

	// FINER

	public boolean isInvited(String playerId)
	{
		return this.getInvitations().containsKey(playerId);
	}

	public boolean isInvited(MPlayer mplayer)
	{
		return this.isInvited(mplayer.getId());
	}

	public boolean isInvitedAlt(String playerId)
	{
		return this.isInvited(playerId) && this.getInvitations().get(playerId).isAlt();
	}

	public boolean isInvitedAlt(MPlayer mPlayer)
	{
		return this.isInvitedAlt(mPlayer.getId());
	}

	public boolean uninvite(String playerId)
	{
		System.out.println(playerId);
		return this.getInvitations().detachId(playerId) != null;
	}

	public boolean uninvite(MPlayer mplayer)
	{
		return uninvite(mplayer.getId());
	}

	public void invite(String playerId, Invitation invitation)
	{
		uninvite(playerId);
		this.invitations.attach(invitation, playerId);
	}

	// -------------------------------------------- //
	// FIELD: relationWish
	// -------------------------------------------- //

	// RAW

	public Map<String, Rel> getRelationWishes()
	{
		return this.relationWishes;
	}

	public void setRelationWishes(Map<String, Rel> relationWishes)
	{
		// Clean input
		MassiveMapDef<String, Rel> target = new MassiveMapDef<>(relationWishes);

		// Detect Nochange
		if (MUtil.equals(this.relationWishes, target)) return;

		// Apply
		this.relationWishes = target;

		// Mark as changed
		this.changed();
	}

	// FINER

	public Rel getRelationWish(String factionId)
	{
		Rel ret = this.getRelationWishes().get(factionId);
		if (ret == null) ret = Rel.NEUTRAL;
		return ret;
	}

	public Rel getRelationWish(Faction faction)
	{
		return this.getRelationWish(faction.getId());
	}

	public void setRelationWish(String factionId, Rel rel)
	{
		Map<String, Rel> relationWishes = this.getRelationWishes();
		if (rel == null || rel == Rel.NEUTRAL)
		{
			relationWishes.remove(factionId);
		}
		else
		{
			relationWishes.put(factionId, rel);
		}
		this.setRelationWishes(relationWishes);
	}

	public void setRelationWish(Faction faction, Rel rel)
	{
		this.setRelationWish(faction.getId(), rel);
	}

	// -------------------------------------------- //
	// FIELD: flagOverrides
	// -------------------------------------------- //

	// RAW

	public Map<MFlag, Boolean> getFlags()
	{
		// We start with default values ...
		Map<MFlag, Boolean> ret = new MassiveMap<>();
		for (MFlag mflag : MFlag.getAll())
		{
			ret.put(mflag, mflag.isStandard());
		}

		// ... and if anything is explicitly set we use that info ...
		Iterator<Entry<String, Boolean>> iter = this.flags.entrySet().iterator();
		while (iter.hasNext())
		{
			// ... for each entry ...
			Entry<String, Boolean> entry = iter.next();

			// ... extract id and remove null values ...
			String id = entry.getKey();
			if (id == null)
			{
				iter.remove();
				this.changed();
				continue;
			}

			// ... resolve object and skip unknowns ...
			MFlag mflag = MFlag.get(id);
			if (mflag == null) continue;

			ret.put(mflag, entry.getValue());
		}

		return ret;
	}

	public void setFlags(Map<MFlag, Boolean> flags)
	{
		Map<String, Boolean> flagIds = new MassiveMap<>();
		for (Entry<MFlag, Boolean> entry : flags.entrySet())
		{
			flagIds.put(entry.getKey().getId(), entry.getValue());
		}
		setFlagIds(flagIds);
	}

	public void setFlagIds(Map<String, Boolean> flagIds)
	{
		// Clean input
		MassiveMapDef<String, Boolean> target = new MassiveMapDef<>();
		for (Entry<String, Boolean> entry : flagIds.entrySet())
		{
			String key = entry.getKey();
			if (key == null) continue;
			key = key.toLowerCase(); // Lowercased Keys Version 2.6.0 --> 2.7.0

			Boolean value = entry.getValue();
			if (value == null) continue;

			target.put(key, value);
		}

		// Detect Nochange
		if (MUtil.equals(this.flags, target)) return;

		// Apply
		this.flags = new MassiveMapDef<>(target);

		// Mark as changed
		this.changed();
	}

	// FINER

	public boolean getFlag(String flagId)
	{
		if (flagId == null) throw new NullPointerException("flagId");

		Boolean ret = this.flags.get(flagId);
		if (ret != null) return ret;

		MFlag flag = MFlag.get(flagId);
		if (flag == null) throw new NullPointerException("flag");

		return flag.isStandard();
	}

	public boolean getFlag(MFlag flag)
	{
		if (flag == null) throw new NullPointerException("flag");

		String flagId = flag.getId();
		if (flagId == null) throw new NullPointerException("flagId");

		Boolean ret = this.flags.get(flagId);
		if (ret != null) return ret;

		return flag.isStandard();
	}

	public Boolean setFlag(String flagId, boolean value)
	{
		if (flagId == null) throw new NullPointerException("flagId");

		Boolean ret = this.flags.put(flagId, value);
		if (ret == null || ret != value) this.changed();
		return ret;
	}

	public Boolean setFlag(MFlag flag, boolean value)
	{
		if (flag == null) throw new NullPointerException("flag");

		String flagId = flag.getId();
		if (flagId == null) throw new NullPointerException("flagId");

		Boolean ret = this.flags.put(flagId, value);
		if (ret == null || ret != value) this.changed();
		return ret;
	}

	// -------------------------------------------- //
	// FIELD: permOverrides
	// -------------------------------------------- //

	// RAW

	public Map<MPerm, Set<Rel>> getPerms()
	{
		// We start with default values ...
		Map<MPerm, Set<Rel>> ret = new MassiveMap<>();
		for (MPerm mperm : MPerm.getAll())
		{
			ret.put(mperm, new MassiveSet<>(mperm.getStandard()));
		}

		// ... and if anything is explicitly set we use that info ...
		Iterator<Entry<String, Set<Rel>>> iter = this.perms.entrySet().iterator();
		while (iter.hasNext())
		{
			// ... for each entry ...
			Entry<String, Set<Rel>> entry = iter.next();

			// ... extract id and remove null values ...
			String id = entry.getKey();
			if (id == null)
			{
				iter.remove();
				continue;
			}

			// ... resolve object and skip unknowns ...
			MPerm mperm = MPerm.get(id);
			if (mperm == null) continue;

			ret.put(mperm, new MassiveSet<>(entry.getValue()));
		}

		return ret;
	}

	public void setPerms(Map<MPerm, Set<Rel>> perms)
	{
		Map<String, Set<Rel>> permIds = new MassiveMap<>();
		for (Entry<MPerm, Set<Rel>> entry : perms.entrySet())
		{
			permIds.put(entry.getKey().getId(), entry.getValue());
		}
		setPermIds(permIds);
	}

	public void setPermIds(Map<String, Set<Rel>> perms)
	{
		// Clean input
		MassiveMapDef<String, Set<Rel>> target = new MassiveMapDef<>();
		for (Entry<String, Set<Rel>> entry : perms.entrySet())
		{
			String key = entry.getKey();
			if (key == null) continue;
			key = key.toLowerCase(); // Lowercased Keys Version 2.6.0 --> 2.7.0

			Set<Rel> value = entry.getValue();
			if (value == null) continue;

			target.put(key, value);
		}

		// Detect Nochange
		if (MUtil.equals(this.perms, target)) return;

		// Apply
		this.perms = target;

		// Mark as changed
		this.changed();
	}

	// FINER

	public boolean isPermitted(String permId, Rel rel)
	{
		if (permId == null) throw new NullPointerException("permId");

		Set<Rel> rels = this.perms.get(permId);
		if (rels != null) return rels.contains(rel);

		MPerm perm = MPerm.get(permId);
		if (perm == null) throw new NullPointerException("perm");

		return perm.getStandard().contains(rel);
	}

	public boolean isPermitted(MPerm perm, Rel rel)
	{
		if (perm == null) throw new NullPointerException("perm");

		String permId = perm.getId();
		if (permId == null) throw new NullPointerException("permId");

		Set<Rel> rels = this.perms.get(permId);
		if (rels != null) return rels.contains(rel);

		return perm.getStandard().contains(rel);
	}

	// ---

	public Set<Rel> getPermitted(MPerm perm)
	{
		if (perm == null) throw new NullPointerException("perm");

		String permId = perm.getId();
		if (permId == null) throw new NullPointerException("permId");

		Set<Rel> rels = this.perms.get(permId);
		if (rels != null) return rels;

		return perm.getStandard();
	}

	public Set<Rel> getPermitted(String permId)
	{
		if (permId == null) throw new NullPointerException("permId");

		Set<Rel> rels = this.perms.get(permId);
		if (rels != null) return rels;

		MPerm perm = MPerm.get(permId);
		if (perm == null) throw new NullPointerException("perm");

		return perm.getStandard();
	}

	@Deprecated
	// Use getPermitted instead. It's much quicker although not immutable.
	public Set<Rel> getPermittedRelations(MPerm perm)
	{
		return this.getPerms().get(perm);
	}

	// ---
	// TODO: Fix these below. They are reworking the whole map.

	public void setPermittedRelations(MPerm perm, Set<Rel> rels)
	{
		Map<MPerm, Set<Rel>> perms = this.getPerms();
		perms.put(perm, rels);
		this.setPerms(perms);
	}

	public void setPermittedRelations(MPerm perm, Rel... rels)
	{
		Set<Rel> temp = new HashSet<>();
		temp.addAll(Arrays.asList(rels));
		this.setPermittedRelations(perm, temp);
	}

	public void setRelationPermitted(MPerm perm, Rel rel, boolean permitted)
	{
		Map<MPerm, Set<Rel>> perms = this.getPerms();

		Set<Rel> rels = perms.get(perm);

		boolean changed;
		if (permitted)
		{
			changed = rels.add(rel);
		}
		else
		{
			changed = rels.remove(rel);
		}

		this.setPerms(perms);

		if (changed) this.changed();
	}

	// -------------------------------------------- //
	// OVERRIDE: RelationParticipator
	// -------------------------------------------- //

	@Override
	public String describeTo(RelationParticipator observer, boolean ucfirst)
	{
		return RelationUtil.describeThatToMe(this, observer, ucfirst);
	}

	@Override
	public String describeTo(RelationParticipator observer)
	{
		return RelationUtil.describeThatToMe(this, observer);
	}

	@Override
	public Rel getRelationTo(RelationParticipator observer)
	{
		return RelationUtil.getRelationOfThatToMe(this, observer);
	}

	@Override
	public Rel getRelationTo(RelationParticipator observer, boolean ignorePeaceful)
	{
		return RelationUtil.getRelationOfThatToMe(this, observer, ignorePeaceful);
	}

	@Override
	public ChatColor getColorTo(RelationParticipator observer)
	{
		return RelationUtil.getColorOfThatToMe(this, observer);
	}

	// -------------------------------------------- //
	// POWER
	// -------------------------------------------- //
	// TODO: Implement a has enough feature.

	public double getPower()
	{
		if (this.getFlag(MFlag.getFlagInfpower())) return 999999;

		double ret = 0;
		for (MPlayer mplayer : this.getMPlayers())
		{
			ret += mplayer.getPower();
		}

		ret = this.limitWithPowerMax(ret);
		ret += this.getPowerBoost();

		return ret;
	}

	public double getPowerMax()
	{
		if (this.getFlag(MFlag.getFlagInfpower())) return 999999;

		double ret = 0;
		for (MPlayer mplayer : this.getMPlayers())
		{
			ret += mplayer.getPowerMax();
		}

		ret = this.limitWithPowerMax(ret);
		ret += this.getPowerBoost();

		return ret;
	}

	private double limitWithPowerMax(double power)
	{
		// NOTE: 0.0 powerMax means there is no max power
		double powerMax = MConf.get().factionPowerMax;

		return powerMax <= 0 || power < powerMax ? power : powerMax;
	}

	public int getPowerRounded()
	{
		return (int) Math.round(this.getPower());
	}

	public int getPowerMaxRounded()
	{
		return (int) Math.round(this.getPowerMax());
	}

	public int getLandCount()
	{
		return BoardColl.get().getCount(this);
	}
	public int getLandCountInWorld(String worldName)
	{
		return Board.get(worldName).getCount(this);
	}

	public boolean hasLandInflation()
	{
		return this.getLandCount() > this.getPowerRounded();
	}

	// -------------------------------------------- //
	// WORLDS
	// -------------------------------------------- //

	public Set<String> getClaimedWorlds()
	{
		return BoardColl.get().getClaimedWorlds(this);
	}

	// -------------------------------------------- //
	// FOREIGN KEY: MPLAYER
	// -------------------------------------------- //

	public List<MPlayer> getMPlayers()
	{
		return new MassiveList<>(FactionsIndex.get().getMPlayers(this));
	}

	public List<MPlayer> getMPlayers(Predicate<? super MPlayer> where, Comparator<? super MPlayer> orderby, Integer limit, Integer offset)
	{
		return MUtil.transform(this.getMPlayers(), where, orderby, limit, offset);
	}

	public List<MPlayer> getMPlayersWhere(Predicate<? super MPlayer> predicate)
	{
		return this.getMPlayers(predicate, null, null, null);
	}

	public List<MPlayer> getMPlayersWhereOnline(boolean online)
	{
		return this.getMPlayersWhere(online ? SenderColl.PREDICATE_ONLINE : SenderColl.PREDICATE_OFFLINE);
	}

	public List<MPlayer> getMPlayersWhereOnlineTo(Object senderObject)
	{
		return this.getMPlayersWhere(PredicateAnd.get(SenderColl.PREDICATE_ONLINE, PredicateVisibleTo.get(senderObject)));
	}

	public List<MPlayer> getMPlayersWhereRole(Rel role)
	{
		return this.getMPlayersWhere(PredicateMPlayerRole.get(role));
	}

	public MPlayer getLeader()
	{
		List<MPlayer> ret = this.getMPlayersWhereRole(Rel.LEADER);
		if (ret.size() == 0) return null;
		return ret.get(0);
	}

	public List<CommandSender> getOnlineCommandSenders()
	{
		// Create Ret
		List<CommandSender> ret = new MassiveList<>();

		// Fill Ret
		for (CommandSender sender : IdUtil.getLocalSenders())
		{
			if (MUtil.isntSender(sender)) continue;

			MPlayer mplayer = MPlayer.get(sender);
			if (mplayer.getFaction() != this) continue;

			ret.add(sender);
		}

		// Return Ret
		return ret;
	}

	public List<Player> getOnlinePlayers()
	{
		// Create Ret
		List<Player> ret = new MassiveList<>();

		// Fill Ret
		for (Player player : MUtil.getOnlinePlayers())
		{
			if (MUtil.isntPlayer(player)) continue;

			MPlayer mplayer = MPlayer.get(player);
			if (mplayer.getFaction() != this) continue;

			ret.add(player);
		}

		// Return Ret
		return ret;
	}

	// used when current leader is about to be removed from the faction; promotes new leader, or disbands faction if no other members left
	public void promoteNewLeader()
	{
		if ( ! this.isNormal()) return;
		if (this.getFlag(MFlag.getFlagPermanent()) && MConf.get().permanentFactionsDisableLeaderPromotion) return;

		MPlayer oldLeader = this.getLeader();

		// get list of officers, or list of normal members if there are no officers
		List<MPlayer> replacements = this.getMPlayersWhereRole(Rel.COLEADER);
		if (replacements == null || replacements.isEmpty())
		{
			replacements = this.getMPlayersWhereRole(Rel.MEMBER);
		}

		if (replacements == null || replacements.isEmpty())
		{
			// faction leader is the only member; one-man faction
			if (this.getFlag(MFlag.getFlagPermanent()))
			{
				if (oldLeader != null)
				{
					// TODO: Where is the logic in this? Why MEMBER? Why not LEADER again? And why not OFFICER or RECRUIT?
					oldLeader.setRole(Rel.MEMBER);
				}
				return;
			}

			// no members left and faction isn't permanent, so disband it
			if (MConf.get().logFactionDisband)
			{
				Factions.get().log("The faction "+this.getName()+" ("+this.getId()+") has been disbanded since it has no members left.");
			}

			for (MPlayer mplayer : MPlayerColl.get().getAllOnline())
			{
				mplayer.msg("<i>The faction %s<i> was disbanded.", this.getName(mplayer));
			}

			this.detach();
		}
		else
		{
			// promote new faction leader
			if (oldLeader != null)
			{
				oldLeader.setRole(Rel.MEMBER);
			}

			replacements.get(0).setRole(Rel.LEADER);
			this.msg("<i>Faction leader <h>%s<i> has been removed. %s<i> has been promoted as the new faction leader.", oldLeader == null ? "" : oldLeader.getName(), replacements.get(0).getName());
			Factions.get().log("Faction "+this.getName()+" ("+this.getId()+") leader was removed. Replacement leader: "+replacements.get(0).getName());
		}
	}

	// -------------------------------------------- //
	// FACTION ONLINE STATE
	// -------------------------------------------- //

	public boolean isAllMPlayersOffline()
	{
		return this.getMPlayersWhereOnline(true).size() == 0;
	}

	public boolean isAnyMPlayersOnline()
	{
		return !this.isAllMPlayersOffline();
	}

	public boolean isFactionConsideredOffline()
	{
		return this.isAllMPlayersOffline();
	}

	public boolean isFactionConsideredOnline()
	{
		return !this.isFactionConsideredOffline();
	}

	public boolean isExplosionsAllowed()
	{
		boolean explosions = this.getFlag(MFlag.getFlagExplosions());
		boolean offlineexplosions = this.getFlag(MFlag.getFlagOfflineexplosions());

		if (explosions && offlineexplosions) return true;
		if ( ! explosions && ! offlineexplosions) return false;

		boolean online = this.isFactionConsideredOnline();

		return (online && explosions) || (!online && offlineexplosions);
	}

	// -------------------------------------------- //
	// MESSAGES
	// -------------------------------------------- //
	// These methods are simply proxied in from the Mixin.

	// CONVENIENCE SEND MESSAGE

	public boolean sendMessage(Object message)
	{
		return MixinMessage.get().messagePredicate(new PredicateCommandSenderFaction(this), message);
	}

	public boolean sendMessage(Object... messages)
	{
		return MixinMessage.get().messagePredicate(new PredicateCommandSenderFaction(this), messages);
	}

	public boolean sendMessage(Collection<Object> messages)
	{
		return MixinMessage.get().messagePredicate(new PredicateCommandSenderFaction(this), messages);
	}

	// CONVENIENCE MSG

	public boolean msg(String msg)
	{
		return MixinMessage.get().msgPredicate(new PredicateCommandSenderFaction(this), msg);
	}

	public boolean msg(String msg, Object... args)
	{
		return MixinMessage.get().msgPredicate(new PredicateCommandSenderFaction(this), msg, args);
	}

	public boolean msg(Collection<String> msgs)
	{
		return MixinMessage.get().msgPredicate(new PredicateCommandSenderFaction(this), msgs);
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	// FIXME this probably needs to be moved elsewhere
	public static String clean(String message)
	{
		String target = message;
		if (target == null) return null;

		target = target.trim();
		if (target.isEmpty()) target = null;

		return target;
	}

	@Override
	public Faction detach()
	{
		this.despawnAllSandAlts();
		return super.detach();
	}

}
