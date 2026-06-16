package com.massivecraft.factions.util;

import com.massivecraft.factions.entity.AuditEntry;
import com.massivecraft.factions.entity.object.AuditAction;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AuditFormat
{
	private AuditFormat() {}

	public static String describe(AuditEntry entry, CommandSender watcher)
	{
		AuditAction action = entry.getAction();
		Map<String, String> details = entry.getDetails();
		String target = targetName(entry, watcher);

        return switch (action)
        {
            case JOIN -> Txt.parse("<g>joined<i> the faction");
            case JOIN_CREATE -> Txt.parse("<g>founded<i> the faction");
            case LEAVE -> Txt.parse("<b>left<i> the faction");
            case KICK -> Txt.parse("<b>kicked <h>%s", target);
            case MOVE -> Txt.parse("<i>moved <h>%s<i> across factions", target);
            case INVITE_ADD -> Txt.parse("<g>invited <h>%s", target);
            case INVITE_REVOKE -> Txt.parse("<b>uninvited <h>%s", target);
            case BAN_ADD -> Txt.parse("<b>banned <h>%s", target);
            case BAN_REMOVE -> Txt.parse("<g>unbanned <h>%s", target);
            case MUTE_ADD -> Txt.parse("<b>muted <h>%s", target);
            case MUTE_REMOVE -> Txt.parse("<g>unmuted <h>%s", target);
            case RANK_SET -> Txt.parse("<i>set <h>%s<i> rank from <h>%s<i> to <h>%s", target, nice(details.get("oldRank")), nice(details.get("newRank")));
            case LEADER_TRANSFER -> Txt.parse("<v>gave leadership<i> to <h>%s", target);
            case TITLE_SET -> Txt.parse("<i>set <h>%s<i> title to <h>%s", target, str(details.get("newTitle")));
            case CLAIM -> Txt.parse("<g>claimed <h>%s<i> chunks <silver>(%s)", str(details.get("count")), str(details.get("sample")));
            case UNCLAIM -> Txt.parse("<b>unclaimed <h>%s<i> chunks", str(details.get("count")));
            case DEPOSIT -> Txt.parse("<g>deposited <h>%s", str(details.get("amount")));
            case WITHDRAW -> Txt.parse("<b>withdrew <h>%s", str(details.get("amount")));
            case TRANSFER_OUT -> Txt.parse("<i>transferred <h>%s<i> out to <h>%s", str(details.get("amount")), target);
            case TRANSFER_IN -> Txt.parse("<i>received <h>%s<i> from <h>%s", str(details.get("amount")), target);
            case INCOME_SPAWNER -> Txt.parse("<g>earned <h>%s<i> from a spawner", str(details.get("amount")));
            case CHEST_PUT -> Txt.parse("<g>stored <h>%s<i> x<h>%s", str(details.get("item")), str(details.get("amount")));
            case CHEST_TAKE -> Txt.parse("<b>took <h>%s<i> x<h>%s", str(details.get("item")), str(details.get("amount")));
            case CREATE -> Txt.parse("<g>created<i> the faction");
            case DISBAND -> Txt.parse("<b>disbanded<i> the faction <silver>(%s members)", str(details.get("memberCount")));
            case NAME_SET -> Txt.parse("<i>renamed the faction from <h>%s<i> to <h>%s", str(details.get("oldName")), str(details.get("newName")));
            case DESCRIPTION_SET -> Txt.parse("<i>changed the description to <h>%s", str(details.get("newDescription")));
            case MOTD_SET -> Txt.parse("<i>changed the motd to <h>%s", str(details.get("newMotd")));
            case FLAG_SET -> Txt.parse("<i>set flag <h>%s<i> to <h>%s", str(details.get("flag")), str(details.get("value")));
            case PERM_SET -> Txt.parse("<i>set perm <h>%s<i> for <h>%s<i> to <h>%s", str(details.get("perm")), nice(details.get("rel")), str(details.get("value")));
            case RELATION_SET -> Txt.parse("<i>set relation with <h>%s<i> to <h>%s", str(details.get("otherName")), nice(details.get("newRelation")));
            case HOME_SET -> Txt.parse("<i>set the faction home to <h>%s", str(details.get("loc")));
            case HOME_UNSET -> Txt.parse("<i>removed the faction home");
            case STRIKE_ADD -> Txt.parse("<b>added a strike <silver>(%s pts)<i>: <h>%s", str(details.get("points")), str(details.get("reason")));
            case STRIKE_REMOVE -> Txt.parse("<g>removed a strike <silver>(%s pts)", str(details.get("points")));
            case WARP_CREATE -> Txt.parse("<g>created warp <h>%s<i> at <h>%s", str(details.get("warp")), str(details.get("loc")));
            case WARP_DELETE -> Txt.parse("<b>deleted warp <h>%s", str(details.get("warp")));
            default -> Txt.parse("<i>%s", Txt.getNicedEnum(action));
        };
	}

	public static String actorName(AuditEntry entry, CommandSender watcher)
	{
		String id = entry.getActorId();
		if (id == null) return Txt.parse("<silver>system");
		String name = MixinDisplayName.get().getDisplayName(id, watcher);
		if (name != null) return name;
		return entry.getActorName() != null ? entry.getActorName() : id;
	}

	public static String age(AuditEntry entry)
	{
		long millis = System.currentTimeMillis() - entry.getCreatedMillis();
		LinkedHashMap<TimeUnit, Long> units = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
		return TimeDiffUtil.formatedMinimal(units, "<i>");
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	private static String targetName(AuditEntry entry, CommandSender watcher)
	{
		String cached = entry.getTargetName();
		if (cached != null) return cached;
		String id = entry.getTargetId();
		if (id == null) return "?";
		String name = MixinDisplayName.get().getDisplayName(id, watcher);
		return name != null ? name : id;
	}

	private static String str(String parameter)
	{
		return parameter == null || parameter.isEmpty() ? "?" : parameter;
	}

	private static String nice(String parameter)
	{
		if (parameter == null || parameter.isEmpty()) return "?";
		return Txt.getNicedEnumString(parameter);
	}
}
