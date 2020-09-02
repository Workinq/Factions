package com.massivecraft.factions;

import com.massivecraft.factions.adapter.BoardAdapter;
import com.massivecraft.factions.adapter.BoardMapAdapter;
import com.massivecraft.factions.adapter.RelAdapter;
import com.massivecraft.factions.adapter.TerritoryAccessAdapter;
import com.massivecraft.factions.chat.modifier.*;
import com.massivecraft.factions.chat.tag.*;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.cmd.type.TypeFactionChunkChangeType;
import com.massivecraft.factions.cmd.type.TypeRel;
import com.massivecraft.factions.engine.*;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.entity.migrator.MigratorFaction001Invitations;
import com.massivecraft.factions.entity.migrator.MigratorMConf001EnumerationUtil;
import com.massivecraft.factions.entity.migrator.MigratorMConf002CleanInactivity;
import com.massivecraft.factions.entity.migrator.MigratorMConf003CleanInactivity;
import com.massivecraft.factions.event.EventFactionsChunkChangeType;
import com.massivecraft.factions.integration.V18.IntegrationV18;
import com.massivecraft.factions.integration.V19.IntegrationV19;
import com.massivecraft.factions.integration.coreprotect.IntegrationCoreProtect;
import com.massivecraft.factions.integration.holographicdisplays.IntegrationHolographicDisplays;
import com.massivecraft.factions.integration.mobextras.IntegrationMobExtras;
import com.massivecraft.factions.integration.spigot.IntegrationSpigot;
import com.massivecraft.factions.integration.worldguard.IntegrationWorldGuard;
import com.massivecraft.factions.mixin.PowerMixin;
import com.massivecraft.factions.task.*;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.RegistryType;
import com.massivecraft.massivecore.store.migrator.MigratorUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.ExclusionStrategy;
import com.massivecraft.massivecore.xlib.gson.FieldAttributes;
import com.massivecraft.massivecore.xlib.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Factions extends MassivePlugin
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //

	public final static String FACTION_MONEY_ACCOUNT_ID_PREFIX = "faction-";

	public final static String ID_NONE = "none";
	public final static String ID_SAFEZONE = "safezone";
	public final static String ID_WARZONE = "warzone";

	public final static String NAME_NONE_DEFAULT = ChatColor.DARK_GREEN.toString() + "Wilderness";
	public final static String NAME_SAFEZONE_DEFAULT = "SafeZone";
	public final static String NAME_WARZONE_DEFAULT = "WarZone";

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static Factions i;
	public static Factions get() { return i; }
	public Factions() { Factions.i = this; }

	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	// Mixins
	@Deprecated public PowerMixin getPowerMixin() { return PowerMixin.get(); }
	@Deprecated public void setPowerMixin(PowerMixin powerMixin) { PowerMixin.get().setInstance(powerMixin); }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void onEnableInner()
	{
		// Register types
		RegistryType.register(Rel.class, TypeRel.get());
		RegistryType.register(EventFactionsChunkChangeType.class, TypeFactionChunkChangeType.get());

		// Register Faction accountId Extractor
		// TODO: Perhaps this should be placed in the econ integration somewhere?
		MUtil.registerExtractor(String.class, "accountId", ExtractorFactionAccountId.get());

		MigratorUtil.addJsonRepresentation(Board.class, Board.MAP_TYPE);

		// Activate
		this.activate(this.getClassesActiveMigrators()); // Migrators
		this.activate(this.getClassesActiveColls()); // Colls
		this.activate(this.getClassesActiveCommands()); // Commands
		this.activate(this.getClassesActiveEngines()); // Engines
		this.activate(this.getClassesActiveIntegrations()); // Integrations
		this.activate(this.getClassesActiveTasks()); // Tasks
		this.activate(this.getClassesActiveChat()); // Chat
	}

	@Override
	public void onDisable()
	{
		for (Player player : Bukkit.getOnlinePlayers()) player.closeInventory();
		super.onDisable();
	}

	@Override
	public List<Class<?>> getClassesActiveMigrators()
	{
		return new MassiveList<>(
				MigratorFaction001Invitations.class,
				MigratorMConf001EnumerationUtil.class,
				MigratorMConf002CleanInactivity.class,
				MigratorMConf003CleanInactivity.class
		);
	}

	@Override
	public List<Class<?>> getClassesActiveColls()
	{
		// MConf should always be activated first for all plugins. It's simply a standard. The config should have no dependencies.
		// MFlag and MPerm are both dependency free.
		// Next we activate Faction, MPlayer and Board. The order is carefully chosen based on foreign keys and indexing direction.
		// MPlayer --> Faction
		// We actually only have an index that we maintain for the MPlayer --> Faction one.
		// The Board could currently be activated in any order but the current placement is an educated guess.
		// In the future we might want to find all chunks from the faction or something similar.
		// We also have the /f access system where the player can be granted specific access, possibly supporting the idea of such a reverse index.
		return new MassiveList<>(
				MConfColl.class,
				MFlagColl.class,
				MPermColl.class,
				FactionColl.class,
				MPlayerColl.class,
				BoardColl.class,
				MOptionColl.class,
				MUpgradeColl.class,
				MMissionColl.class
		);
	}

	@Override
	public List<Class<?>> getClassesActiveCommands()
	{
		return new MassiveList<>(CmdFactions.class);
	}

	@Override
	public List<Class<?>> getClassesActiveEngines()
	{
		List<Class<?>> ret = new MassiveList<>(EngineCanCombatHappen.class, EngineChat.class, EngineChest.class, EngineChunkChange.class, EngineCleanInactivity.class, EngineDenyCommands.class, EngineExploit.class, EngineExtras.class, EngineFactionChat.class, EngineFlagEndergrief.class, EngineFlagExplosion.class, EngineFlagFireSpread.class, EngineFlagSpawn.class, EngineFlagZombiegrief.class, EngineFly.class, EngineLastActivity.class, EngineLogin.class, EngineMoney.class, EngineMotd.class, EngineMoveChunk.class, EnginePermBuild.class, EnginePlayerData.class, EnginePower.class, EngineSand.class, EngineScoreboard.class, EngineSeeChunk.class, EngineShow.class, EngineSkull.class, EngineTeleportHomeOnDeath.class, EngineTerritoryShield.class, EngineTnt.class, EngineVisualizations.class, EngineEcon.class);

		ret.remove(EngineEcon.class);
		ret.add(EngineEcon.class);

		return ret;
	}

	@Override
	public List<Class<?>> getClassesActiveIntegrations()
	{
		return new MassiveList<>(
				IntegrationCoreProtect.class,
				IntegrationHolographicDisplays.class,
				IntegrationMobExtras.class,
				IntegrationSpigot.class,
				IntegrationV18.class,
				IntegrationV19.class,
				IntegrationWorldGuard.class
		);
	}

	@Override
	public List<Class<?>> getClassesActiveTasks()
	{
		return new MassiveList<>(
				TaskEconLandReward.class,
				TaskFactionsFly.class,
				TaskFlagPermCreate.class,
				TaskPlaceSand.class,
				TaskPlayerPowerUpdate.class,
				TaskRemindBaseRegion.class,
				TaskRemindClear.class,
				TaskRingFactionAlarm.class
		);
	}

	public List<Class<?>> getClassesActiveChat()
	{
		return new MassiveList<>(
				ChatModifierLc.class,
				ChatModifierLp.class,
				ChatModifierParse.class,
				ChatModifierRp.class,
				ChatModifierUc.class,
				ChatModifierUcf.class,
				ChatTagName.class,
				ChatTagNameforce.class,
				ChatTagRelcolor.class,
				ChatTagRole.class,
				ChatTagRoleprefix.class,
				ChatTagRoleprefixforce.class,
				ChatTagTitle.class
		);
	}

	@Override
	public GsonBuilder getGsonBuilder()
	{
		return super.getGsonBuilder()
		.registerTypeAdapter(TerritoryAccess.class, TerritoryAccessAdapter.get())
		.registerTypeAdapter(Board.class, BoardAdapter.get())
		.registerTypeAdapter(Board.MAP_TYPE, BoardMapAdapter.get())
		.registerTypeAdapter(Rel.class, RelAdapter.get())
		.addSerializationExclusionStrategy(new ExclusionStrategy()
		{
			@Override public boolean shouldSkipField(FieldAttributes field) { return field.getName().equals("b"); }
			@Override public boolean shouldSkipClass(Class<?> clazz) { return false; }
		})
		;
	}

}
