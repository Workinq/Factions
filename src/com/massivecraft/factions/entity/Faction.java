package com.massivecraft.factions.entity;

import com.massivecraft.factions.*;
import com.massivecraft.factions.entity.mission.AbstractMission;
import com.massivecraft.factions.entity.object.*;
import com.massivecraft.factions.predicate.PredicateCommandSenderFaction;
import com.massivecraft.factions.predicate.PredicateMPlayerRole;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.util.SerializationUtil;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.collections.MassiveSetDef;
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
		this.setWarpLocations(that.warpLocations);
		this.setWarpPasswords(that.warpPasswords);
		this.setPaypal(that.paypal);
		this.setDiscord(that.discord);
		this.setBannedMembers(that.bannedMembers);
		this.setUpgrades(that.upgrades);
		this.setMissionGoal(that.missionGoal);
		this.setActiveMission(that.activeMission);
		this.setMissionStart(that.missionStart);
		this.setCredits(that.credits);
		this.setStrikes(that.strikes);
		this.setBaseRegion(that.baseRegion);
		this.setShieldedHour(that.shieldedHour);
		this.setFocusedPlayer(that.focusedPlayer);
		this.setBanner(that.banner);
		this.setRoster(that.roster);
		this.setRosterKickTimes(that.rosterKickTimes);
		this.setSandAlts(that.sandAlts);
		this.setBannedMembers(that.bannedMembers);
		this.setMutedMembers(that.mutedMembers);
		this.setAlarmEnabled(that.alarmEnabled);
		return this;
	}

	@Override
	public void preDetach(String id)
	{
		if ( ! this.isLive() ) return;

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
	// Is transient as the Inventory object cannot be serialised.
	// TODO: Maybe look into using MassiveCore's AdapterInventory class
	private transient Inventory inventory = null;

	// This is the serialized string for the inventories contents.
	// This cannot corrupt in the slightest or the entire inventory will reset.
	// Or worse, the server will spit errors non-stop.
	private String inventorySerialized = null;

	// This contains all the interactions that have been made with the faction chest.
	// Whenever a player takes or adds an item to the chest, an action will be added.
	// These items can later be viewed using /f chest log <page>.
	private MassiveSetDef<ChestAction> chestActions = new MassiveSetDef<>();

	// This contains all the faction warps which have been set.
	// By default this is empty however, warps can be added using /f setwarp <warp name>.
	// These warps can later be viewed using /f warp list.
	private MassiveMapDef<String, PS> warpLocations = new MassiveMapDef<>();
	private MassiveMapDef<String, String> warpPasswords = new MassiveMapDef<>();

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
	// null will mean there is no ongoing mission.
	private Long missionStart = null;

	// This will store the current progress for the faction's current mission.
	// Null either means there is no mission ongoing or there is no progress.
	private Integer missionGoal = null;

	// This will store faction upgrades along with their level.
	private MassiveMapDef<String, Integer> upgrades = new MassiveMapDef<>();

	// This stores the faction's credits.
	// Credits are awarded when a faction completes a mission.
	// By default the faction starts with 0 credits.
	private Integer credits = 0;

	// This will store a list of strikes the faction has acquired.
	// A strike can be given using /f strike <faction> <points> <reason>.
	private MassiveSetDef<FactionStrike> strikes = new MassiveSetDef<>();

	// The faction's base region will be stored here.
	// Using /f setbaseregion will run a loop to save chunks in a 60x60 radius from the sender.
	// Empty most likely means the base region is empty.
	private MassiveSetDef<PS> baseRegion = new MassiveSetDef<>();

	// This will store the hour for which the faction is protected.
	// Null means no hour has been set.
	private Integer shieldedHour = null;

	// Stores the uuid of the focused player.
	// Null means no player has been focused.
	private UUID focusedPlayer = null;

	// Stores whether or not the faction's alarm is active.
	// Null means it isn't.
	private Boolean alarmEnabled = null;

	// This stores the last time walls were checked.
	// Null means walls have never been checked.
	private Long lastCheckedMillis = null;

	// This stores the patterns for the faction banner.
	// By default there are 0 patterns (i think).
	private MassiveList<String> banner = new MassiveList<>();

	// This will store the faction roster, you can add and remove members from the roster using /f roster.
	// By default the roster will be empty however, the owner might be in it.
	private MassiveMapDef<String, Rel> roster = new MassiveMapDef<>();

	// This will store the timestamps of when a player was kicked from the faction.
	// By default it's empty and it can store up to 5 values (can be changed in config).
	private MassiveSetDef<Long> rosterKickTimes = new MassiveSetDef<>();

	// This will store the faction's currently active sand alts.
	// By default there will be none, obviously.
	private MassiveSetDef<SandAlt> sandAlts = new MassiveSetDef<>();

	// This will store a list of all the banned members.
	// By default it's empty and members can be banned using /f ban <player>.
	private MassiveSetDef<FactionBan> bannedMembers = new MassiveSetDef<>();

	// Can anyone join the Faction?
	// If the faction is open they can.
	// If the faction is closed an invite is required.
	// Null means default.
	// private Boolean open = null;

	// This will store a list of all the muted members.
	// By default it's empty and members can be banned using /f mute <player>.
	private MassiveSetDef<FactionMute> mutedMembers = new MassiveSetDef<>();

	// This is the ids of the invited players.
	// They are actually "senderIds" since you can invite "@console" to your faction.
	// Null means no one is invited
	private final EntityInternalMap<Invitation> invitations = new EntityInternalMap<>(this, Invitation.class);

	// The keys in this map are factionIds.
	// Null means no special relation wishes.
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
		// Detect no change
		if (MUtil.equals(this.name, name)) return;

		// Apply
		this.name = name;

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

		// Detect no change
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

		// Detect no change
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
		// Detect no change
		if (MUtil.equals(this.createdAtMillis, createdAtMillis)) return;

		// Apply
		this.createdAtMillis = createdAtMillis;

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
		// Detect no change
		if (MUtil.equals(this.home, home)) return;

		// Apply
		this.home = home;

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

		// Detect no change
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
		// Clean input
		Integer ret = this.tnt;
		if (ret == null) ret = 0;

		return ret;
	}

	public void setTnt(Integer tnt)
	{
		// Clean input
		Integer target = tnt;
		if (target == null || target == 0) target = null;

		// Detect no change
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
			ret = SerializationUtil.fromBase64(inventorySerialized, Txt.parse("<gray>%s - Faction Chest", this.getName()));

			// Set inventory.
			this.inventory = ret;

			// Mark as changed
			this.changed();
		}

		return ret;
	}

	public void setInventory(Inventory inventory)
	{
		// Detect no change
		if (MUtil.equals(this.inventory, inventory)) return;

		// Apply
		this.inventory = inventory;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: inventorySerialized
	// -------------------------------------------- //

	public String getInventorySerialized()
	{
		// Clean input
		String ret = this.inventorySerialized;
		if (ret == null) ret = "";

		return ret;
	}

	public void setInventorySerialized(String inventorySerialized)
	{
		// Clean input
		String target = inventorySerialized;
		if (target == null || target.equals("")) target = null;

		// Detect no change
		if (MUtil.equals(this.inventorySerialized, target)) return;

		// Apply
		this.inventorySerialized = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: chestActions
	// -------------------------------------------- //

	public MassiveSetDef<ChestAction> getChestActions()
	{
		return chestActions;
	}

	public void addChestAction(ChestAction chestAction)
	{
		// Add
		chestActions.add(chestAction);

		// Mark as changed
		this.changed();
	}

	public void setChestActions(MassiveSetDef<ChestAction> chestActions)
	{
		// Apply
		this.chestActions = chestActions;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: warps
	// -------------------------------------------- //

	public MassiveMapDef<String, PS> getWarpLocations()
	{
		return warpLocations;
	}
	
	public MassiveMapDef<String, String> getWarpPasswords()
	{
		return warpPasswords;
	}

	public void addWarp(String warp, PS location, String password)
	{
		warpLocations.put(warp, location);
		if (password != null) warpPasswords.put(warp, password);

		// Mark as changed
		this.changed();
	}

	public void deleteWarp(String warp)
	{
		// Remove
		warpLocations.remove(warp);
		warpPasswords.remove(warp);

		// Mark as changed
		this.changed();
	}

	public void setWarpLocations(MassiveMapDef<String, PS> warpLocations)
	{
		// Apply
		this.warpLocations = warpLocations;

		// Mark as changed
		this.changed();
	}

	public void setWarpPasswords(MassiveMapDef<String, String> warpPasswords)
	{
		// Apply
		this.warpPasswords = warpPasswords;

		// Mark as changed
		this.changed();
	}

	public boolean verifyWarpIsValid(String warp)
	{
		// Verify
		if ( ! warpLocations.containsKey(warp) ) return false;
		if (this.isValidWarp(warpLocations.get(warp))) return true;

		// Apply
		this.deleteWarp(warp);

		// Mark as changed
		this.changed();

		// Inform
		this.msg("<b>The faction warp '%s' has been un-set since it is no longer in your territory.", warp);

		// Return
		return false;
	}

	public boolean isValidWarp(PS ps)
	{
		if (ps == null) return true;
		if ( ! MConf.get().warpsMustBeInClaimedTerritory ) return true;
		return BoardColl.get().getFactionAt(ps) == this;
	}

	public List<String> getWarpNames()
	{
		return new ArrayList<>(this.warpLocations.keySet());
	}

	public boolean warpExists(String warpName)
	{
		return this.warpLocations.containsKey(warpName);
	}

	public boolean warpHasPassword(String warp)
	{
		return this.warpPasswords.containsKey(warp);
	}

	public String getWarpPassword(String warp)
	{
		return this.warpPasswords.get(warp);
	}

	public PS getWarpLocation(String warp)
	{
		return this.warpLocations.get(warp);
	}

	// -------------------------------------------- //
	// FIELD: paypal
	// -------------------------------------------- //
	
	public String getPaypal()
	{
		// Clean input
		String ret = this.paypal;
		if (ret == null) ret = "none";

		return ret;
	}

	public void setPaypal(String paypal)
	{
		// Clean input
		String target = paypal;
		if (target == null || target.equals("")) target = null;

		// Detect no change
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
		if (ret == null) ret = "none";

		return ret;
	}

	public void setDiscord(String discord)
	{
		// Clean input
		String target = discord;
		if (target == null || target.equals("")) target = null;

		// Detect no change
		if (MUtil.equals(this.discord, target)) return;

		// Apply
		this.discord = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: mission
	// -------------------------------------------- //
	
	public AbstractMission getActiveMission()
	{
		return MMission.get().getMissionByName(activeMission);
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
		Long ret = this.missionStart;
		if (ret == null) ret = 0L;
		return ret;
	}

	public void setMissionStart(Long missionStart)
	{
		// Clean input
		Long target = missionStart;
		if (target == null || target == 0L) target = null;

		// Detect no change
		if (MUtil.equals(this.missionStart, target)) return;

		// Apply
		this.missionStart = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: missionGoal
	// -------------------------------------------- //

	public int getMissionGoal()
	{
		// Clean input
		Integer target = this.missionGoal;
		if (target == null) target = 0;

		return target;
	}

	public void setMissionGoal(Integer missionGoal)
	{
		// Clean input
		Integer target = missionGoal;

		if (target == null || target == 0) target = null;

		// Detect no change
		if (MUtil.equals(this.missionGoal, target)) return;

		// Apply
		this.missionGoal = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: upgrades
	// -------------------------------------------- //
	
	public MassiveMapDef<String, Integer> getUpgrades()
	{
		return upgrades;
	}

	public void setUpgrades(MassiveMapDef<String, Integer> upgrades)
	{
		// Apply
		this.upgrades = upgrades;

		// Mark as changed
		this.changed();
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
		// Clean input
		Integer target = this.credits;
		if (target == null) target = 0;

		return target;
	}

	public void setCredits(Integer credits)
	{
		// Clean input
		Integer target = credits;

		if (target == null || target == 0) target = null;

		// Detect no change
		if (MUtil.equals(this.credits, target)) return;

		// Apply
		this.credits = target;

		// Mark as changed
		this.changed();
	}

	public void addCredits(Integer credits)
	{
		// Clean Input
		Integer ret = credits;
		if (ret == null) ret = 0;

		// Args
		Integer newCredits = this.getCredits() + ret;

		// Apply
		this.setCredits(newCredits);

		// Mark as changed
		this.changed();
	}

	public void takeCredits(Integer credits)
	{
		// Clean Input
		Integer ret = credits;
		if (ret == null) ret = 0;

		// Args
		int newCredits = this.getCredits() - ret;

		// Detect Negative
		if (newCredits < 0) newCredits = 0;

		// Apply
		this.setCredits(newCredits);

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: strikes
	// -------------------------------------------- //
	
	public MassiveSetDef<FactionStrike> getStrikes()
	{
		return strikes;
	}

	public void addStrike(FactionStrike strike)
	{
		// Add
		strikes.add(strike);

		// Mark as changed
		this.changed();
	}

	public void deleteStrike(FactionStrike strike)
	{
		// Remove
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

	public void setStrikes(MassiveSetDef<FactionStrike> strikes)
	{
		// Apply
		this.strikes = strikes;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: baseRegion
	// -------------------------------------------- //

	public void setBaseRegion(MassiveSetDef<PS> baseRegion)
	{
		// Apply
		this.baseRegion = baseRegion;

		// Mark as changed
		this.changed();
	}

	public MassiveSetDef<PS> getBaseRegion()
	{
		return baseRegion;
	}

	public boolean hasBaseRegion()
	{
		return this.baseRegion != null && ! this.baseRegion.isEmpty();
	}

	// -------------------------------------------- //
	// FIELD: shieldedHour
	// -------------------------------------------- //

	public void setShieldedHour(Integer shieldedHour)
	{
		// Detect no change
		if (MUtil.equals(this.shieldedHour, shieldedHour)) return;

		// Apply
		this.shieldedHour = shieldedHour;

		// Mark as changed
		this.changed();
	}

	public boolean isShieldedAt(int hour)
	{
		if (this.shieldedHour == null) return false;

		int minimum = this.shieldedHour;
		int maximum = this.shieldedHour + MConf.get().shieldHours;

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

	public boolean hasShield()
	{
		return this.shieldedHour != null;
	}

	public Integer getShieldedHour()
	{
		return shieldedHour;
	}

	// -------------------------------------------- //
	// FIELD: focusedPlayer
	// -------------------------------------------- //

	public void setFocusedPlayer(UUID focusedPlayer)
	{
		// Apply
		this.focusedPlayer = focusedPlayer;

		// Mark as changed
		this.changed();
	}

	public boolean isPlayerFocused(UUID uuid)
	{
		return this.focusedPlayer != null && this.focusedPlayer.equals(uuid);
	}
	
	public UUID getFocusedPlayer()
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

	@SuppressWarnings("deprecation")
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
	
	public MassiveMapDef<String, Rel> getRoster()
	{
		return roster;
	}

	public void addToRoster(MPlayer mplayer)
	{
		// Detect no change
		if (this.roster.containsKey(mplayer.getId())) return;

		// Apply
		this.roster.put(mplayer.getId(), Rel.RECRUIT);

		// Mark as changed
		this.changed();
	}

	public void addToRoster(MPlayer mplayer, Rel rank)
	{
		// Apply
		this.roster.put(mplayer.getId(), rank);

		// Mark as changed
		this.changed();
	}

	public void removeFromRoster(MPlayer mplayer)
	{
		// Detect no change
		if ( ! this.roster.containsKey(mplayer.getId())) return;

		// Apply
		this.roster.remove(mplayer.getId());

		// Mark as changed
		this.changed();
	}

	public void setRosterRank(MPlayer mplayer, Rel role)
	{
		// Detect no change
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

	public void setRoster(MassiveMapDef<String, Rel> roster)
	{
		// Apply
		this.roster = roster;

		// Mark as changed
		this.changed();
	}

	public Set<String> getRosterUuids()
	{
		return new HashSet<>(roster.keySet());
	}

	// -------------------------------------------- //
	// FIELD: rosterKickTimes
	// -------------------------------------------- //

	public void setRosterKickTimes(MassiveSetDef<Long> rosterKickTimes)
	{
		// Apply
		this.rosterKickTimes = rosterKickTimes;

		// Mark as changed
		this.changed();
	}
	
	public MassiveSetDef<Long> getRosterKickTimes()
	{
		return rosterKickTimes;
	}

	public void addRosterKick(long time)
	{
		// Apply
		this.rosterKickTimes.add(time);

		// Mark as changed
		this.changed();
	}
	public void addRosterKick() { this.addRosterKick(System.currentTimeMillis()); }

	public void removeRosterKick(long time)
	{
		// Verify
		if (!this.rosterKickTimes.contains(time)) return;

		// Apply
		this.rosterKickTimes.remove(time);

		// Mark as changed
		this.changed();
	}

	public int getNumberOfRosterKicks()
	{
		return this.rosterKickTimes.size();
	}

	// -------------------------------------------- //
	// FIELD: sandAlts
	// -------------------------------------------- //

	public void setSandAlts(MassiveSetDef<SandAlt> sandAlts)
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
			if ( ! sandAlt.isPaused() ) continue;
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
		Set<SandAlt> sandAlts = new MassiveSetDef<>();
		for (SandAlt sandAlt : this.sandAlts)
		{
			if (sandAlt.getPs().getChunk(true).equals(chunk))
			{
				sandAlts.add(sandAlt);
			}
		}
		return sandAlts;
	}
	
	public MassiveSetDef<SandAlt> getSandAlts()
	{
		return sandAlts;
	}

	// -------------------------------------------- //
	// FIELD: alarmEnabled
	// -------------------------------------------- //

	public boolean isAlarmEnabled()
	{
		// Clean input
		Boolean ret = this.alarmEnabled;
		if (ret == null) ret = false;

		return ret;
	}

	public void setAlarmEnabled(Boolean alarmEnabled)
	{
		// Clean input
		Boolean target = alarmEnabled;
		if (target == null || !target) target = null;

		// Detect no change
		if (MUtil.equals(this.alarmEnabled, target)) return;

		// Apply
		this.alarmEnabled = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: lastCheckedMillis
	// -------------------------------------------- //

	public long getLastCheckedMillis()
	{
		// Clean input
		Long ret = this.lastCheckedMillis;
		if (ret == null) ret = 0L;

		return ret;
	}

	public void setLastCheckedMillis(Long lastCheckedMillis)
	{
		// Clean input
		Long target = lastCheckedMillis;
		if (target == null || target == 0L) target = null;

		// Detect no change
		if (MUtil.equals(this.lastCheckedMillis, target)) return;

		// Apply
		this.lastCheckedMillis = target;

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: bannedMembers
	// -------------------------------------------- //

	// RAW

	public MassiveSetDef<FactionBan> getBannedMembers() { return this.bannedMembers; }

	public void setBannedMembers(MassiveSetDef<FactionBan> bannedMembers)
	{
		this.bannedMembers = bannedMembers;

		// Mark as changed
		this.changed();
	}

	// FINER

	public boolean isBanned(String playerId)
	{
		return bannedMembers.stream().anyMatch(factionBan -> factionBan.getBannedId().equals(playerId));
	}

	public boolean isBanned(MPlayer mplayer)
	{
		return this.isBanned(mplayer.getId());
	}

	public void unban(String playerId)
	{
		Optional<FactionBan> optional = bannedMembers.stream().filter(factionBan -> factionBan.getBannedId().equals(playerId)).findAny();
		boolean result = optional.filter(factionBan -> bannedMembers.remove(factionBan)).isPresent();

		if ( ! result ) return;

		// Mark as changed
		this.changed();
	}
	public void unban(MPlayer mplayer) { this.unban(mplayer.getId()); }
	public void unban(FactionBan factionBan)
	{
		boolean result = bannedMembers.remove(factionBan);

		if ( ! result ) return;

		// Mark as changed
		this.changed();
	}

	public void ban(FactionBan factionBan)
	{
		this.unban(factionBan);
		this.bannedMembers.add(factionBan);

		// Mark as changed
		this.changed();
	}

	// -------------------------------------------- //
	// FIELD: mutedMembers
	// -------------------------------------------- //

	public void setMutedMembers(MassiveSetDef<FactionMute> mutedMembers)
	{
		// Apply
		this.mutedMembers = mutedMembers;

		// Mark as changed
		this.changed();
	}

	public MassiveSetDef<FactionMute> getMutedMembers()
	{
		return this.mutedMembers;
	}

	public boolean isMuted(String playerId)
	{
		return mutedMembers.stream().anyMatch(factionMute -> factionMute.getMutedId().equals(playerId));
	}

	public boolean isMuted(MPlayer mplayer)
	{
		return this.isMuted(mplayer.getId());
	}

	public boolean unmute(String playerId)
	{
		Optional<FactionMute> optional = mutedMembers.stream().filter(factionMute -> factionMute.getMutedId().equals(playerId)).findAny();
		boolean result = optional.filter(factionMute -> mutedMembers.remove(factionMute)).isPresent();

		if ( ! result ) return false;

		// Mark as changed
		this.changed();
		return true;
	}

	public boolean unmute(MPlayer mplayer)
	{
		return this.unmute(mplayer.getId());
	}

	public boolean unmute(FactionMute factionMute)
	{
		boolean result = mutedMembers.remove(factionMute);

		// Detect no change
		if ( ! result ) return false;

		// Mark as changed
		this.changed();

		// Return
		return true;
	}

	public void mute(FactionMute factionMute)
	{
		// Unmute
		this.unmute(factionMute);

		// Mute
		this.mutedMembers.add(factionMute);

		// Mark as changed
		this.changed();
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

	public boolean isInvitedAlt(MPlayer mplayer)
	{
		return this.isInvitedAlt(mplayer.getId());
	}

	public boolean uninvite(String playerId)
	{
		return this.getInvitations().detachId(playerId) != null;
	}

	public boolean uninvite(MPlayer mplayer)
	{
		return this.uninvite(mplayer.getId());
	}

	public void invite(String playerId, Invitation invitation)
	{
		this.uninvite(playerId);
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

		// Detect no change
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

		// Detect no change
		if (MUtil.equals(this.flags, target)) return;

		// Apply
		this.flags = new MassiveMapDef<>(target);

		// Mark as changed
		this.changed();
	}

	// FINER

	public boolean getFlag(String flagId)
	{
		Boolean ret = this.flags.get(flagId);
		if (ret != null) return ret;

		MFlag flag = MFlag.get(flagId);
		if (flag == null) throw new NullPointerException("flag");

		return flag.isStandard();
	}

	public boolean getFlag(MFlag flag)
	{
		String flagId = flag.getId();
		if (flagId == null) throw new NullPointerException("flagId");

		Boolean ret = this.flags.get(flagId);
		if (ret != null) return ret;

		return flag.isStandard();
	}

	public Boolean setFlag(String flagId, boolean value)
	{
		Boolean ret = this.flags.put(flagId, value);
		if (ret == null || ret != value) this.changed();
		return ret;
	}

	public Boolean setFlag(MFlag flag, boolean value)
	{
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
			ret.put(mperm, new MassiveSetDef<>(mperm.getStandard()));
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

			ret.put(mperm, new MassiveSetDef<>(entry.getValue()));
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
		this.setPermIds(permIds);
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

		// Detect no change
		if (MUtil.equals(this.perms, target)) return;

		// Apply
		this.perms = target;

		// Mark as changed
		this.changed();
	}

	// FINER

	public boolean isPermitted(String permId, Rel rel)
	{
		Set<Rel> rels = this.perms.get(permId);
		if (rels != null) return rels.contains(rel);

		MPerm perm = MPerm.get(permId);
		if (perm == null) throw new NullPointerException("perm");

		return perm.getStandard().contains(rel);
	}

	public boolean isPermitted(MPerm perm, Rel rel)
	{
		String permId = perm.getId();
		if (permId == null) throw new NullPointerException("permId");

		Set<Rel> rels = this.perms.get(permId);
		if (rels != null) return rels.contains(rel);

		return perm.getStandard().contains(rel);
	}

	// ---

	public Set<Rel> getPermitted(MPerm perm)
	{
		String permId = perm.getId();
		if (permId == null) throw new NullPointerException("permId");

		Set<Rel> rels = this.perms.get(permId);
		if (rels != null) return rels;

		return perm.getStandard();
	}
	
	public Set<Rel> getPermitted(String permId)
	{
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
		Set<Rel> temp = new HashSet<>(Arrays.asList(rels));
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

	public List<MPlayer> getMPlayersWhereAlt(boolean alt)
	{
		return this.getMPlayersWhere(alt ? MPlayerColl.PREDICATE_ALT : MPlayerColl.PREDICATE_NALT);
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
				this.setRosterRank(oldLeader, Rel.MEMBER);
			}

			// New leader
			MPlayer newLeader = replacements.get(0);

			// Promote
			newLeader.setRole(Rel.LEADER);
			this.setRosterRank(newLeader, Rel.LEADER);

			// Inform
			this.msg("<i>Faction leader <h>%s<i> has been removed. %s<i> has been promoted as the new faction leader.", oldLeader == null ? "" : oldLeader.getName(), newLeader.getName());
			Factions.get().log("Faction "+this.getName()+" ("+this.getId()+") leader was removed. Replacement leader: "+newLeader.getName());
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
