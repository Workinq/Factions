package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandDeprecated;
import com.massivecraft.massivecore.command.MassiveCommandVersion;

import java.util.List;

@SuppressWarnings("unused")
public class CmdFactions extends FactionsCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdFactions i = new CmdFactions();
	public static CmdFactions get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsList cmdFactionsList = new CmdFactionsList();
	public CmdFactionsFaction cmdFactionsFaction = new CmdFactionsFaction();
	public CmdFactionsPlayer cmdFactionsPlayer = new CmdFactionsPlayer();
	public CmdFactionsJoin cmdFactionsJoin = new CmdFactionsJoin();
	public CmdFactionsLeave cmdFactionsLeave = new CmdFactionsLeave();
	public CmdFactionsHome cmdFactionsHome = new CmdFactionsHome();
	public CmdFactionsMap cmdFactionsMap = new CmdFactionsMap();
	public CmdFactionsNear cmdFactionsNear = new CmdFactionsNear();
	public CmdFactionsCreate cmdFactionsCreate = new CmdFactionsCreate();
	public CmdFactionsName cmdFactionsName = new CmdFactionsName();
	public CmdFactionsDescription cmdFactionsDescription = new CmdFactionsDescription();
	public CmdFactionsMotd cmdFactionsMotd = new CmdFactionsMotd();
	public CmdFactionsSethome cmdFactionsSethome = new CmdFactionsSethome();
	public CmdFactionsUnsethome cmdFactionsUnsethome = new CmdFactionsUnsethome();
	public CmdFactionsInvite cmdFactionsInvite = new CmdFactionsInvite();
	public CmdFactionsDeinvite cmdFactionsDeinvite = new CmdFactionsDeinvite();
	public CmdFactionsInviteList cmdFactionsInviteList = new CmdFactionsInviteList();
	public CmdFactionsKick cmdFactionsKick = new CmdFactionsKick();
	public CmdFactionsTitle cmdFactionsTitle = new CmdFactionsTitle();
	public CmdFactionsRank cmdFactionsRank = new CmdFactionsRank();
	public CmdFactionsRankOld cmdFactionsRankOldLeader = new CmdFactionsRankOld("leader");
	public CmdFactionsRankOld cmdFactionsRankOldOwner = new CmdFactionsRankOld("owner");
	public CmdFactionsRankOld cmdFactionsRankOldColeader = new CmdFactionsRankOld("coleader");
	public CmdFactionsRankOld cmdFactionsRankOldOfficer = new CmdFactionsRankOld("officer");
	public CmdFactionsRankOld cmdFactionsRankOldModerator = new CmdFactionsRankOld("moderator");
	public CmdFactionsRankOld cmdFactionsRankOldPromote = new CmdFactionsRankOld("promote");
	public CmdFactionsRankOld cmdFactionsRankOldDemote = new CmdFactionsRankOld("demote");
	public CmdFactionsRankViewAll cmdFactionsRankViewAll = new CmdFactionsRankViewAll();
	public CmdFactionsMoney cmdFactionsMoney = new CmdFactionsMoney();
	public CmdFactionsSeeChunk cmdFactionsSeeChunk = new CmdFactionsSeeChunk();
	public CmdFactionsSeeChunkOld cmdFactionsSeeChunkOld = new CmdFactionsSeeChunkOld();
	public CmdFactionsTerritorytitles cmdFactionsTerritorytitles = new CmdFactionsTerritorytitles();
	public CmdFactionsStatus cmdFactionsStatus = new CmdFactionsStatus();
	public CmdFactionsClaim cmdFactionsClaim = new CmdFactionsClaim();
	public CmdFactionsUnclaim cmdFactionsUnclaim = new CmdFactionsUnclaim();
	public CmdFactionsAccess cmdFactionsAccess = new CmdFactionsAccess();
	public CmdFactionsRelation cmdFactionsRelation = new CmdFactionsRelation();
	public CmdFactionsRelationOld cmdFactionsRelationOldAlly = new CmdFactionsRelationOld("ally");
	public CmdFactionsRelationOld cmdFactionsRelationOldTruce = new CmdFactionsRelationOld("truce");
	public CmdFactionsRelationOld cmdFactionsRelationOldNeutral = new CmdFactionsRelationOld("neutral");
	public CmdFactionsRelationOld cmdFactionsRelationOldEnemy = new CmdFactionsRelationOld("enemy");
	public CmdFactionsPerm cmdFactionsPerm = new CmdFactionsPerm();
	public CmdFactionsFlag cmdFactionsFlag = new CmdFactionsFlag();
	public CmdFactionsUnstuck cmdFactionsUnstuck = new CmdFactionsUnstuck();
	public CmdFactionsExpansions cmdFactionsExpansions = new CmdFactionsExpansions();
	public CmdFactionsXPlaceholder cmdFactionsTax = new CmdFactionsXPlaceholder("FactionsTax", "tax");
	public CmdFactionsXPlaceholder cmdFactionsDynmap = new CmdFactionsXPlaceholder("FactionsDynmap", "dynmap");
	public CmdFactionsOverride cmdFactionsOverride = new CmdFactionsOverride();
	public CmdFactionsDisband cmdFactionsDisband = new CmdFactionsDisband();
	public CmdFactionsPowerBoost cmdFactionsPowerBoost = new CmdFactionsPowerBoost();
	public CmdFactionsSetpower cmdFactionsSetpower = new CmdFactionsSetpower();
	public CmdFactionsConfig cmdFactionsConfig = new CmdFactionsConfig();
	public CmdFactionsClean cmdFactionsClean = new CmdFactionsClean();
	public CmdFactionsTnt cmdFactionsTnt = new CmdFactionsTnt();
	public CmdFactionsChest cmdFactionsChest = new CmdFactionsChest();
	public CmdFactionsVault cmdFactionsVault = new CmdFactionsVault();
	public CmdFactionsStealth cmdFactionsStealth = new CmdFactionsStealth();
	public CmdFactionsFly cmdFactionsFly = new CmdFactionsFly();
	public CmdFactionsWarp cmdFactionsWarp = new CmdFactionsWarp();
	public CmdFactionsWarplist cmdFactionsWarpList = new CmdFactionsWarplist();
	public CmdFactionsSetwarp cmdFactionsSetwarp = new CmdFactionsSetwarp();
	public CmdFactionsDelwarp cmdFactionsDelwarp = new CmdFactionsDelwarp();
	public CmdFactionsDiscord cmdFactionsDiscord = new CmdFactionsDiscord();
	public CmdFactionsPaypal cmdFactionsPaypal = new CmdFactionsPaypal();
	public CmdFactionsBan cmdFactionsBan = new CmdFactionsBan();
	public CmdFactionsUnban cmdFactionsUnban = new CmdFactionsUnban();
	public CmdFactionsBanlist cmdFactionsBanList = new CmdFactionsBanlist();
	public CmdFactionsMute cmdFactionsMute = new CmdFactionsMute();
	public CmdFactionsUnmute cmdFactionsUnmute = new CmdFactionsUnmute();
	public CmdFactionsMuteList cmdFactionsMuteList = new CmdFactionsMuteList();
	public CmdFactionsLocation cmdFactionsLocation = new CmdFactionsLocation();
	public CmdFactionsCoords cmdFactionsCoords = new CmdFactionsCoords();
	public CmdFactionsChat cmdFactionsChat = new CmdFactionsChat();
	public CmdFactionsSpy cmdFactionsSpy = new CmdFactionsSpy();
	public CmdFactionsInspect cmdFactionsInspect = new CmdFactionsInspect();
	public CmdFactionsLastinspected cmdFactionsLastInspected = new CmdFactionsLastinspected();
	public CmdFactionsIgnore cmdFactionsIgnore = new CmdFactionsIgnore();
	public CmdFactionsUnignore cmdFactionsUnignore = new CmdFactionsUnignore();
	public CmdFactionsAlt cmdFactionsAlt = new CmdFactionsAlt();
	public CmdFactionsMission cmdFactionsMission = new CmdFactionsMission();
	public CmdFactionsUpgrade cmdFactionsUpgrade = new CmdFactionsUpgrade();
	public CmdFactionsCredits cmdFactionsCredits = new CmdFactionsCredits();
	public CmdFactionsStrike cmdFactionsStrike = new CmdFactionsStrike();
	public CmdFactionsAudit cmdFactionsAudit = new CmdFactionsAudit();
	public CmdFactionsSetbaseregion cmdFactionsSetBaseRegion = new CmdFactionsSetbaseregion();
	public CmdFactionsShield cmdFactionsShield = new CmdFactionsShield();
	public CmdFactionsFocus cmdFactionsFocus = new CmdFactionsFocus();
	public CmdFactionsUnfocus cmdFactionsUnfocus = new CmdFactionsUnfocus();
	public CmdFactionsBaltop cmdFactionsBaltop = new CmdFactionsBaltop();
	public CmdFactionsInvsee cmdFactionsInvsee = new CmdFactionsInvsee();
	public CmdFactionsRoster cmdFactionsRoster = new CmdFactionsRoster();
	public CmdFactionsDrain cmdFactionsDrain = new CmdFactionsDrain();
	public CmdFactionsDrainToggle cmdFactionsDrainToggle = new CmdFactionsDrainToggle();
	public CmdFactionsSandalt cmdFactionsSandAlt = new CmdFactionsSandalt();
	public CmdFactionsChunkalt cmdFactionsChunkAlt = new CmdFactionsChunkalt();
	public CmdFactionsLogin cmdFactionsLogin = new CmdFactionsLogin();
	public CmdFactionsFf cmdFactionsFf = new CmdFactionsFf();
	public CmdFactionsToggle cmdFactionsToggle = new CmdFactionsToggle();
	public CmdFactionsAlarm cmdFactionsAlarm = new CmdFactionsAlarm();
	public CmdFactionsClear cmdFactionsClear = new CmdFactionsClear();
	public CmdFactionsAlertNotifications cmdFactionsAlertNotifications = new CmdFactionsAlertNotifications();
	public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(Factions.get()).setAliases("v", "version");

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactions()
	{
		// Deprecated Commands
		this.addChild(new MassiveCommandDeprecated(this.cmdFactionsClaim.cmdFactionsClaimAuto, "autoclaim"));
		this.addChild(new MassiveCommandDeprecated(this.cmdFactionsFlag, "open"));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesF;
	}

}
