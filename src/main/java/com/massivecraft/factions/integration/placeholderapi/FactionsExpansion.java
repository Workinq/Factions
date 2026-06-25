package com.massivecraft.factions.integration.placeholderapi;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Locale;

public class FactionsExpansion extends PlaceholderExpansion implements Relational
{
    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public @NotNull String getIdentifier()
    {
        return "factions";
    }

    @Override
    public @NotNull String getAuthor()
    {
        return "Kieran";
    }

    @Override
    public @NotNull String getVersion()
    {
        return Factions.get().getDescription().getVersion();
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    // -------------------------------------------- //
    // REQUEST
    // -------------------------------------------- //

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params)
    {
        MPlayer mplayer = MPlayer.get(player);
        if (mplayer == null) return null;

        Faction faction = mplayer.getFaction();
        boolean hasFaction = mplayer.hasFaction();

        params = params.toLowerCase(Locale.ROOT);

        switch (params)
        {
            case "faction": return faction.getName();
            case "hasfaction": return String.valueOf(hasFaction);
            case "power": return String.valueOf(mplayer.getPowerRounded());
            case "powermax": return String.valueOf(mplayer.getPowerMaxRounded());
            case "role": return hasFaction ? mplayer.getRole().getPrefix() : "";
            case "rolename": return hasFaction ? mplayer.getRole().getName() : "";
            case "title": return mplayer.hasTitle() ? Txt.parse(mplayer.getTitle()) : "";
        }

        if (params.equals("count") || params.startsWith("count_"))
        {
            return hasFaction ? this.count(faction, params) : "0";
        }

        return switch (params)
        {
            case "factionpower" -> hasFaction ? String.valueOf(faction.getPowerRounded()) : "0";
            case "factionpowermax" -> hasFaction ? String.valueOf(faction.getPowerMaxRounded()) : "0";
            case "claims" -> hasFaction ? String.valueOf(faction.getLandCount()) : "0";
            case "onlinemembers" -> hasFaction ? String.valueOf(faction.getMPlayersWhereOnline(true).size()) : "0";
            case "allmembers" -> hasFaction ? String.valueOf(faction.getMPlayersWhereAlt(false).size()) : "0";
            case "warps" -> hasFaction ? String.valueOf(faction.getWarpNames().size()) : "0";
            case "balance" -> hasFaction ? this.balance(faction) : "0";
            case "home" -> String.valueOf(hasFaction && faction.hasHome());
            case "overclaimed" -> String.valueOf(hasFaction && faction.hasLandInflation());
            case "leader" -> hasFaction ? this.leader(faction) : "";
            case "description" -> hasFaction ? this.description(faction) : "";
            case "age" -> hasFaction ? this.age(faction) : "";
            case "founded" -> hasFaction ? this.founded(faction) : "";
            default -> null;
        };

    }

    @Override
    public String onPlaceholderRequest(Player one, Player two, String identifier)
    {
        if (one == null || two == null) return null;
        if (one.getName().equals(two.getName())) return null;

        MPlayer mone = MPlayer.get(one);
        MPlayer mtwo = MPlayer.get(two);
        if (mone == null || mtwo == null) return null;

        return switch (identifier.toLowerCase(Locale.ROOT))
        {
            case "relation" -> mone.getRelationTo(mtwo).getName();
            case "relation_color" ->
            {
                ChatColor color = mone.getColorTo(mtwo);
                yield color == null ? null : color.toString();
            }
            default -> null;
        };

    }

    // -------------------------------------------- //
    // UTIL
    // -------------------------------------------- //

    private String count(Faction faction, String params)
    {
        if (params.equals("count")) return String.valueOf(faction.getMPlayers().size());

        return switch (params.substring("count_".length()))
        {
            case "online" -> String.valueOf(faction.getMPlayersWhereOnline(true).size());
            case "offline" -> String.valueOf(faction.getMPlayersWhereOnline(false).size());
            case "alt" -> String.valueOf(faction.getMPlayersWhereAlt(true).size());
            case "recruit" -> String.valueOf(faction.getMPlayersWhereRole(Rel.RECRUIT).size());
            case "member" -> String.valueOf(faction.getMPlayersWhereRole(Rel.MEMBER).size());
            case "officer" -> String.valueOf(faction.getMPlayersWhereRole(Rel.OFFICER).size());
            case "coleader" -> String.valueOf(faction.getMPlayersWhereRole(Rel.COLEADER).size());
            case "leader" -> String.valueOf(faction.getMPlayersWhereRole(Rel.LEADER).size());
            default -> null;
        };

    }

    private String leader(Faction faction)
    {
        MPlayer leader = faction.getLeader();
        return leader == null ? "" : leader.getName();
    }

    private String description(Faction faction)
    {
        String description = faction.getDescription();
        return description == null ? "" : Txt.parse(description);
    }

    private String age(Faction faction)
    {
        LinkedHashMap<TimeUnit, Long> unitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(faction.getAge(), TimeUnit.getAllButMillis()), 2);
        return TimeDiffUtil.formattedMinimal(unitcounts);
    }

    private String founded(Faction faction)
    {
        return Instant.ofEpochMilli(faction.getCreatedAtMillis()).atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }

    private String balance(Faction faction)
    {
        if (!Econ.isEnabled()) return "0";
        return Money.format(Money.get(faction));
    }

}
