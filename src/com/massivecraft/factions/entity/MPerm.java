package com.massivecraft.factions.entity;

import com.massivecraft.factions.AccessStatus;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.TerritoryAccess;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.event.EventFactionsCreatePerms;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.Prioritized;
import com.massivecraft.massivecore.Registerable;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.predicate.PredicateIsRegistered;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MPerm extends Entity<MPerm> implements Prioritized, Registerable, Named
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static transient String ID_BUILD = "build";
	public final static transient String ID_PAINBUILD = "painbuild";
	public final static transient String ID_DOOR = "door";
	public final static transient String ID_BUTTON = "button";
	public final static transient String ID_LEVER = "lever";
	public final static transient String ID_CONTAINER = "container";
	public final static transient String ID_NAME = "name";
	public final static transient String ID_DESC = "desc";
	public final static transient String ID_MOTD = "motd";
	public final static transient String ID_INVITE = "invite";
	public final static transient String ID_KICK = "kick";
	public final static transient String ID_MUTE = "mute";
	public final static transient String ID_TITLE = "title";
	public final static transient String ID_HOME = "home";
	public final static transient String ID_SETHOME = "sethome";
	public final static transient String ID_DEPOSIT = "deposit";
	public final static transient String ID_WITHDRAW = "withdraw";
	public final static transient String ID_TERRITORY = "territory";
	public final static transient String ID_ACCESS = "access";
	public final static transient String ID_CLAIMNEAR = "claimnear";
	public final static transient String ID_REL = "rel";
	public final static transient String ID_DISBAND = "disband";
	public final static transient String ID_FLAGS = "flags";
	public final static transient String ID_PERMS = "perms";
	public final static transient String ID_STATUS = "status";
	public final static transient String ID_CHEST = "chest";
	public final static transient String ID_TNT = "tnt";
	public final static transient String ID_WARP = "warp";
	public final static transient String ID_SETWARP = "setwarp";
	public final static transient String ID_DELWARP = "delwarp";
	public final static transient String ID_DISCORD = "discord";
	public final static transient String ID_PAYPAL = "paypal";
	public final static transient String ID_BAN = "ban";
	public final static transient String ID_LOCATION = "location";
	public final static transient String ID_INSPECT = "inspect";
	public final static transient String ID_UPGRADE = "upgrade";
	public final static transient String ID_MISSION = "mission";
	public final static transient String ID_ALT = "alt";
	public final static transient String ID_CREDITS = "credits";
	public final static transient String ID_BASEREGION = "baseregion";
	public final static transient String ID_SHIELD = "shield";
	public final static transient String ID_EXPLOSIVES = "explosives";
	public final static transient String ID_SPAWNERS = "spawners";
	public final static transient String ID_FOCUS = "focus";
	public final static transient String ID_INVSEE = "invsee";
	public final static transient String ID_ROSTER = "roster";
	public final static transient String ID_DRAIN = "drain";
	public final static transient String ID_SANDALT = "sandalt";
	public final static transient String ID_FLY = "fly";
	public final static transient String ID_ALARM = "alarm";
	public final static transient String ID_CLEAR = "clear";

	public final static transient int PRIORITY_BUILD = 1000;
	public final static transient int PRIORITY_PAINBUILD = 2000;
	public final static transient int PRIORITY_DOOR = 3000;
	public final static transient int PRIORITY_BUTTON = 4000;
	public final static transient int PRIORITY_LEVER = 5000;
	public final static transient int PRIORITY_CONTAINER = 6000;
	public final static transient int PRIORITY_NAME = 7000;
	public final static transient int PRIORITY_DESC = 8000;
	public final static transient int PRIORITY_MOTD = 9000;
	public final static transient int PRIORITY_INVITE = 10000;
	public final static transient int PRIORITY_KICK = 11000;
	public final static transient int PRIORITY_TITLE = 12000;
	public final static transient int PRIORITY_HOME = 13000;
	public final static transient int PRIORITY_SETHOME = 14000;
	public final static transient int PRIORITY_DEPOSIT = 15000;
	public final static transient int PRIORITY_WITHDRAW = 16000;
	public final static transient int PRIORITY_TERRITORY = 17000;
	public final static transient int PRIORITY_ACCESS = 18000;
	public final static transient int PRIORITY_CLAIMNEAR = 19000;
	public final static transient int PRIORITY_REL = 20000;
	public final static transient int PRIORITY_DISBAND = 21000;
	public final static transient int PRIORITY_FLAGS = 22000;
	public final static transient int PRIORITY_PERMS = 23000;
	public final static transient int PRIORITY_STATUS = 24000;
	public final static transient int PRIORITY_CHEST = 25000;
	public final static transient int PRIORITY_TNT = 26000;
	public final static transient int PRIORITY_CREDITS = 27000;
	public final static transient int PRIORITY_WARP = 28000;
	public final static transient int PRIORITY_ALT = 29000;
	public final static transient int PRIORITY_SETWARP = 30000;
	public final static transient int PRIORITY_DELWARP = 31000;
	public final static transient int PRIORITY_DISCORD = 32000;
	public final static transient int PRIORITY_BAN = 33000;
	public final static transient int PRIORITY_LOCATION = 34000;
	public final static transient int PRIORITY_INSPECT = 35000;
	public final static transient int PRIORITY_UPGRADE = 36000;
	public final static transient int PRIORITY_MISSION = 37000;
	public final static transient int PRIORITY_PAYPAL = 38000;
	public final static transient int PRIORITY_BASEREGION = 39000;
	public final static transient int PRIORITY_SHIELD = 40000;
	public final static transient int PRIORITY_EXPLOSIVES = 41000;
	public final static transient int PRIORITY_SPAWNERS = 42000;
	public final static transient int PRIORITY_FOCUS = 43000;
	public final static transient int PRIORITY_INVSEE = 44000;
	public final static transient int PRIORITY_ROSTER = 45000;
	public final static transient int PRIORITY_DRAIN = 46000;
	public final static transient int PRIORITY_SANDALT = 47000;
	public final static transient int PRIORITY_FLY = 48000;
	public final static transient int PRIORITY_MUTE = 49000;
	public final static transient int PRIORITY_ALARM = 50000;
	public final static transient int PRIORITY_CLEAR = 51000;

	// -------------------------------------------- //
	// META: CORE
	// -------------------------------------------- //
	
	public static MPerm get(Object oid)
	{
		return MPermColl.get().get(oid);
	}
	
	public static List<MPerm> getAll()
	{
		return getAll(false);
	}
	
	public static List<MPerm> getAll(boolean isAsync)
	{
		setupStandardPerms();
		new EventFactionsCreatePerms().run();
		
		return MPermColl.get().getAll(PredicateIsRegistered.get(), ComparatorSmart.get());
	}
	
	public static void setupStandardPerms()
	{
		getPermBuild();
		getPermPainbuild();
		getPermDoor();
		getPermButton();
		getPermLever();
		getPermContainer();
		
		getPermName();
		getPermDesc();
		getPermMotd();
		getPermInvite();
		getPermKick();
		getPermTitle();
		getPermHome();
		getPermStatus();
		getPermSethome();
		getPermDeposit();
		getPermWithdraw();
		getPermTerritory();
		getPermAccess();
		getPermClaimnear();
		getPermRel();
		getPermDisband();
		getPermFlags();
		getPermPerms();
		getPermChest();
		getPermTnt();
		getPermWarp();
		getPermSetwarp();
		getPermDelwarp();
		getPermBan();
		getPermLocation();
		getPermInspect();
		getPermUpgrade();
		getPermMission();
		getPermMute();
		getPermDiscord();
		getPermPaypal();
		getPermAlt();
		getPermCredits();
		getPermShield();
		getPermExplosives();
		getPermSpawners();
		getPermFocus();
		getPermInvsee();
		getPermRoster();
		getPermDrain();
		getPermSandalt();
		getPermFly();
		getPermAlarm();
		getPermClear();
	}

	public static MPerm getPermBuild() { return getCreative(PRIORITY_BUILD, ID_BUILD, ID_BUILD, "edit the terrain", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER), true, true, true); }
	public static MPerm getPermPainbuild() { return getCreative(PRIORITY_PAINBUILD, ID_PAINBUILD, ID_PAINBUILD, "edit, take damage", new MassiveSet<>(), true, true, true); }
	public static MPerm getPermDoor() { return getCreative(PRIORITY_DOOR, ID_DOOR, ID_DOOR, "use doors", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), true, true, true); }
	public static MPerm getPermButton() { return getCreative(PRIORITY_BUTTON, ID_BUTTON, ID_BUTTON, "use stone buttons", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), true, true, true); }
	public static MPerm getPermLever() { return getCreative(PRIORITY_LEVER, ID_LEVER, ID_LEVER, "use levers", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), true, true, true); }
	public static MPerm getPermContainer() { return getCreative(PRIORITY_CONTAINER, ID_CONTAINER, ID_CONTAINER, "use containers", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY), true, false, true); }

	public static MPerm getPermName() { return getCreative(PRIORITY_NAME, ID_NAME, ID_NAME, "set name", MUtil.set(Rel.LEADER), false, true, true); }
	public static MPerm getPermDesc() { return getCreative(PRIORITY_DESC, ID_DESC, ID_DESC, "set description", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermMotd() { return getCreative(PRIORITY_MOTD, ID_MOTD, ID_MOTD, "set motd", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermInvite() { return getCreative(PRIORITY_INVITE, ID_INVITE, ID_INVITE, "invite players", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermStatus() { return getCreative(PRIORITY_STATUS, ID_STATUS, ID_STATUS, "show status", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermKick() { return getCreative(PRIORITY_KICK, ID_KICK, ID_KICK, "kick members", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermTitle() { return getCreative(PRIORITY_TITLE, ID_TITLE, ID_TITLE, "set titles", MUtil.set(Rel.LEADER,Rel.COLEADER,  Rel.OFFICER), false, true, true); }
	public static MPerm getPermHome() { return getCreative(PRIORITY_HOME, ID_HOME, ID_HOME, "teleport home", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), false, true, true); }
	public static MPerm getPermSethome() { return getCreative(PRIORITY_SETHOME, ID_SETHOME, ID_SETHOME, "set the home", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermDeposit() { return getCreative(PRIORITY_DEPOSIT, ID_DEPOSIT, ID_DEPOSIT, "deposit money", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY, Rel.TRUCE, Rel.NEUTRAL, Rel.ENEMY), false, false, false); } // non editable, non visible.
	public static MPerm getPermWithdraw() { return getCreative(PRIORITY_WITHDRAW, ID_WITHDRAW, ID_WITHDRAW, "withdraw money", MUtil.set(Rel.LEADER, Rel.COLEADER), false, true, true); }
	public static MPerm getPermTerritory() { return getCreative(PRIORITY_TERRITORY, ID_TERRITORY, ID_TERRITORY, "claim or unclaim", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermAccess() { return getCreative(PRIORITY_ACCESS, ID_ACCESS, ID_ACCESS, "grant territory", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermClaimnear() { return getCreative(PRIORITY_CLAIMNEAR, ID_CLAIMNEAR, ID_CLAIMNEAR, "claim nearby", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT, Rel.ALLY), false, false, false); } // non editable, non visible.
	public static MPerm getPermRel() { return getCreative(PRIORITY_REL, ID_REL, ID_REL, "change relations", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermDisband() { return getCreative(PRIORITY_DISBAND, ID_DISBAND, ID_DISBAND, "disband the faction", MUtil.set(Rel.LEADER), false, true, true); }
	public static MPerm getPermFlags() { return getCreative(PRIORITY_FLAGS, ID_FLAGS, ID_FLAGS, "manage flags", MUtil.set(Rel.LEADER, Rel.COLEADER), false, true, true); }
	public static MPerm getPermPerms() { return getCreative(PRIORITY_PERMS, ID_PERMS, ID_PERMS, "manage permissions", MUtil.set(Rel.LEADER, Rel.COLEADER), false, true, true); }
	public static MPerm getPermChest() { return getCreative(PRIORITY_CHEST, ID_CHEST, ID_CHEST, "manage the faction chest", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermTnt() { return getCreative(PRIORITY_TNT, ID_TNT, ID_TNT, "manage faction tnt", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermWarp() { return getCreative(PRIORITY_WARP, ID_WARP, ID_WARP, "teleport to a faction warp", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermSetwarp() { return getCreative(PRIORITY_SETWARP, ID_SETWARP, ID_SETWARP, "set a faction warp", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermDelwarp() { return getCreative(PRIORITY_DELWARP, ID_DELWARP, ID_DELWARP, "delete a faction warp", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermBan() { return getCreative(PRIORITY_BAN, ID_BAN, ID_BAN, "manage faction bans", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermLocation() { return getCreative(PRIORITY_LOCATION, ID_LOCATION, ID_LOCATION, "ping your location", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER), false, true, true); }
	public static MPerm getPermInspect() { return getCreative(PRIORITY_INSPECT, ID_INSPECT, ID_INSPECT, "inspect faction land", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermUpgrade() { return getCreative(PRIORITY_UPGRADE, ID_UPGRADE, ID_UPGRADE, "manage faction upgrades", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermMission() { return getCreative(PRIORITY_MISSION, ID_MISSION, ID_MISSION, "manage faction missions", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermMute() {return getCreative(PRIORITY_MUTE,ID_MUTE,ID_MUTE,"manage faction mutes",MUtil.set(Rel.LEADER, Rel.COLEADER,Rel.OFFICER),false,true,true);}
	public static MPerm getPermDiscord() { return getCreative(PRIORITY_DISCORD, ID_DISCORD, ID_DISCORD, "manage the faction discord", MUtil.set(Rel.LEADER), false, true, true); }
	public static MPerm getPermPaypal() { return getCreative(PRIORITY_PAYPAL, ID_PAYPAL, ID_PAYPAL, "manage the faction paypal", MUtil.set(Rel.LEADER), false, true, true); }
	public static MPerm getPermAlt() { return getCreative(PRIORITY_ALT, ID_ALT, ID_ALT, "manage faction alts", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermCredits() { return getCreative(PRIORITY_CREDITS, ID_CREDITS, ID_CREDITS, "manage faction credits", MUtil.set(Rel.LEADER, Rel.COLEADER), false, true, true); }
	public static MPerm getPermBaseregion() { return getCreative(PRIORITY_BASEREGION, ID_BASEREGION, ID_BASEREGION, "set your faction base region", MUtil.set(Rel.LEADER), false, true, true); }
	public static MPerm getPermShield() { return getCreative(PRIORITY_SHIELD, ID_SHIELD, ID_SHIELD, "mange your faction shield", MUtil.set(Rel.LEADER, Rel.COLEADER), false, true, true); }
	public static MPerm getPermExplosives() { return getCreative(PRIORITY_EXPLOSIVES, ID_EXPLOSIVES, ID_EXPLOSIVES, "place explosives down", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), true, true, true); }
	public static MPerm getPermSpawners() { return getCreative(PRIORITY_SPAWNERS, ID_SPAWNERS, ID_SPAWNERS, "interact with spawners", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), true, true, true); }
	public static MPerm getPermFocus() { return getCreative(PRIORITY_FOCUS, ID_FOCUS, ID_FOCUS, "focus a player", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermInvsee() { return getCreative(PRIORITY_INVSEE, ID_INVSEE, ID_INVSEE, "view a members inventory", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermRoster() { return getCreative(PRIORITY_ROSTER, ID_ROSTER, ID_ROSTER, "manage the faction roster", MUtil.set(Rel.LEADER, Rel.COLEADER), false, true, true); }
	public static MPerm getPermDrain() { return getCreative(PRIORITY_DRAIN, ID_DRAIN, ID_DRAIN, "drain members balances", MUtil.set(Rel.LEADER, Rel.COLEADER), false, true, true); }
	public static MPerm getPermSandalt() { return getCreative(PRIORITY_SANDALT, ID_SANDALT, ID_SANDALT, "mange faction sandalts", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermFly() { return getCreative(PRIORITY_FLY, ID_FLY, ID_FLY, "toggle faction fly", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER, Rel.MEMBER, Rel.RECRUIT), false, true, true); }
	public static MPerm getPermAlarm() { return getCreative(PRIORITY_ALARM, ID_ALARM, ID_ALARM, "toggle faction alarm", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }
	public static MPerm getPermClear() { return getCreative(PRIORITY_CLEAR, ID_CLEAR, ID_CLEAR, "mark walls as cleared", MUtil.set(Rel.LEADER, Rel.COLEADER, Rel.OFFICER), false, true, true); }

	public static MPerm getCreative(int priority, String id, String name, String desc, Set<Rel> standard, boolean territory, boolean editable, boolean visible)
	{
		MPerm ret = MPermColl.get().get(id, false);
		if (ret != null)
		{
			ret.setRegistered(true);
			return ret;
		}
		
		ret = new MPerm(priority, name, desc, standard, territory, editable, visible);
		MPermColl.get().attach(ret, id);
		ret.setRegistered(true);
		ret.sync();
		
		return ret;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public MPerm load(MPerm that)
	{
		this.priority = that.priority;
		this.name = that.name;
		this.desc = that.desc;
		this.standard = that.standard;
		this.territory = that.territory;
		this.editable = that.editable;
		this.visible = that.visible;
		
		return this;
	}
	
	// -------------------------------------------- //
	// TRANSIENT FIELDS (Registered)
	// -------------------------------------------- //
	
	private transient boolean registered = false;
	@Override public boolean isRegistered() { return this.registered; }
	public void setRegistered(boolean registered) { this.registered = registered; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// The sort priority. Low values appear first in sorted lists.
	// 1 is high up, 99999 is far down.
	// Standard Faction perms use "thousand values" like 1000, 2000, 3000 etc to allow adding new perms inbetween.
	// So 1000 might sound like a lot but it's actually the priority for the first perm.
	private int priority = 0;
	@Override public int getPriority() { return this.priority; }
	public MPerm setPriority(int priority) { this.priority = priority; this.changed(); return this; }
	
	// The name of the perm. According to standard it should be fully lowercase just like the perm id.
	// In fact the name and the id of all standard perms are the same.
	// I just added the name in case anyone feel like renaming their perms for some reason.
	// Example: "build"
	private String name = "defaultName";
	@Override public String getName() { return this.name; }
	public MPerm setName(String name) { this.name = name; this.changed(); return this; }
	
	// The perm function described as an "order".
	// The desc should match the format:
	// "You are not allowed to X."
	// "You are not allowed to edit the terrain."
	// Example: "edit the terrain"
	private String desc = "defaultDesc";
	public String getDesc() { return this.desc; }
	public MPerm setDesc(String desc) { this.desc = desc; this.changed(); return this; }
	
	// What is the standard (aka default) perm value?
	// This value will be set for factions from the beginning.
	// Example: ... set of relations ...
	private Set<Rel> standard = new MassiveSet<>();
	public Set<Rel> getStandard() { return this.standard; }
	public MPerm setStandard(Set<Rel> standard) { this.standard = standard; this.changed(); return this; }

	// Is this a territory perm meaning it has to do with territory construction, modification or interaction?
	// True Examples: build, container, door, lever etc.
	// False Examples: name, invite, home, sethome, deposit, withdraw etc.
	private boolean territory = false;
	public boolean isTerritory() { return this.territory; }
	public MPerm setTerritory(boolean territory) { this.territory = territory; this.changed(); return this; }
	
	// Is this perm editable by players?
	// With this we mean standard non administrator players.
	// All perms can be changed using /f override.
	// Example: true (all perms are editable by default)
	private boolean editable = false;
	public boolean isEditable() { return this.editable; }
	public MPerm setEditable(boolean editable) { this.editable = editable; this.changed(); return this; }
	
	// Is this perm visible to players?
	// With this we mean standard non administrator players.
	// All perms can be seen using /f override.
	// Some perms can be rendered meaningless by settings in Factions or external plugins.
	// Say we set "editable" to false.
	// In such case we might want to hide the perm by setting "visible" false.
	// If it can't be changed, why bother showing it?
	// Example: true (yeah we need to see this permission)
	private boolean visible = true;
	public boolean isVisible() { return this.visible; }
	public MPerm setVisible(boolean visible) { this.visible = visible; this.changed(); return this; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MPerm()
	{
		// No argument constructor for GSON
	}
	
	public MPerm(int priority, String name, String desc, Set<Rel> standard, boolean territory, boolean editable, boolean visible)
	{
		this.priority = priority;
		this.name = name;
		this.desc = desc;
		this.standard = standard;
		this.territory = territory;
		this.editable = editable;
		this.visible = visible;
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	public String createDeniedMessage(MPlayer mplayer, Faction hostFaction)
	{
		// Null Check
		if (mplayer == null) throw new NullPointerException("mplayer");
		if (hostFaction == null) throw new NullPointerException("hostFaction");
		
		String ret = Txt.parse("%s<b> does not allow you to %s<b>.", hostFaction.describeTo(mplayer, true), this.getDesc());
		
		Player player = mplayer.getPlayer();
		if (player != null && Perm.OVERRIDE.has(player))
		{
			ret += Txt.parse("\n<i>You can bypass by using " + CmdFactions.get().cmdFactionsOverride.getTemplate(false).toPlain(true));
		}
		
		return ret;
	}
	
	public String getDesc(boolean withName, boolean withDesc)
	{
		List<String> parts = new ArrayList<>();
		
		if (withName)
		{
			String nameFormat;
			if ( ! this.isVisible())
			{
				nameFormat = "<silver>%s";
			}
			else if (this.isEditable())
			{
				nameFormat = "<pink>%s";
			}
			else
			{
				nameFormat = "<aqua>%s";
			}
			String name = this.getName();
			String nameDesc = Txt.parse(nameFormat, name);
			parts.add(nameDesc);
		}
		
		if (withDesc)
		{
			String desc = this.getDesc();
			
			String descDesc = Txt.parse("<i>%s", desc);
			parts.add(descDesc);
		}
		
		return Txt.implode(parts, " ");
	}
	
	public boolean has(Faction faction, Faction hostFaction)
	{
		// Null Check
		if (faction == null) throw new NullPointerException("faction");
		if (hostFaction == null) throw new NullPointerException("hostFaction");
		
		Rel rel = faction.getRelationTo(hostFaction);
		return hostFaction.isPermitted(this, rel);
	}
	
	public boolean has(MPlayer mplayer, Faction hostFaction, boolean verboose)
	{
		// Null Check
		if (mplayer == null) throw new NullPointerException("mplayer");
		if (hostFaction == null) throw new NullPointerException("hostFaction");
		
		if (mplayer.isOverriding()) return true;
		
		Rel rel = mplayer.getRelationTo(hostFaction);
		if (hostFaction.isPermitted(this, rel)) return true;
		
		if (verboose) mplayer.message(this.createDeniedMessage(mplayer, hostFaction));
		
		return false;
	}
	
	public boolean has(MPlayer mplayer, PS ps, boolean verboose)
	{
		// Null Check
		if (mplayer == null) throw new NullPointerException("mplayer");
		if (ps == null) throw new NullPointerException("ps");
		
		if (mplayer.isOverriding()) return true;
		
		TerritoryAccess ta = BoardColl.get().getTerritoryAccessAt(ps);
		Faction hostFaction = ta.getHostFaction();
		
		if (this.isTerritory())
		{
			AccessStatus accessStatus = ta.getTerritoryAccess(mplayer);
			if (accessStatus != AccessStatus.STANDARD)
			{
				if (verboose && accessStatus == AccessStatus.DECREASED)
				{
					mplayer.message(this.createDeniedMessage(mplayer, hostFaction));
				}
				
				return accessStatus.hasAccess();
			}
		}
		
		return this.has(mplayer, hostFaction, verboose);
	}

	// -------------------------------------------- //
	// UTIL: ASCII
	// -------------------------------------------- //
	
	public static String getStateHeaders()
	{
		StringBuilder ret = new StringBuilder();
		for (Rel rel : Rel.values())
		{
			ret.append(rel.getColor().toString());
			ret.append(rel.toString(), 0, 3);
			ret.append(" ");
		}
		
		return ret.toString();
	}
	
	public String getStateInfo(Set<Rel> value, boolean withDesc)
	{
		StringBuilder ret = new StringBuilder();
		
		for (Rel rel : Rel.values())
		{
			if (value.contains(rel))
			{
				ret.append("<g>YES");
			}
			else
			{
				ret.append("<b>NOO");
			}
			ret.append(" ");
		}
		
		String color = "<aqua>";
		if (!this.isVisible())
		{
			color = "<silver>";
		}
		else if (this.isEditable())
		{
			color = "<pink>";
		}
		
		ret.append(color);
		ret.append(this.getName());
		
		ret = new StringBuilder(Txt.parse(ret.toString()));
		
		if (withDesc) ret.append(" <i>").append(this.getDesc());
		
		return ret.toString();
	}

}
