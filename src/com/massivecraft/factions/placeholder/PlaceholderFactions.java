package com.massivecraft.factions.placeholder;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.util.Txt;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlaceholderFactions extends PlaceholderExpansion implements Relational
{

    @Override public String getAuthor() { return Txt.implodeCommaAnd(Factions.get().getDescription().getAuthors()); }
    @Override public String getVersion() { return Factions.get().getDescription().getVersion(); }
    @Override public String getIdentifier() { return "factions"; }

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        MPlayer mplayer = MPlayer.get(player);
        if (mplayer == null) return null;
        Faction faction = mplayer.getFaction();
        boolean hasFaction = !faction.isNone();

        switch (identifier)
        {
            case "onlinemembers":
                if (!hasFaction) return "0";
                return String.valueOf(faction.getOnlinePlayers().size());
            case "allmembers":
                if (!hasFaction) return "0";
                return String.valueOf(faction.getMPlayersWhereAlt(false).size());
            case "claims":
                if (!hasFaction) return "0";
                return String.valueOf(faction.getLandCount());
            case "faction":
                return faction.getName();
            case "role":
                if (!hasFaction) return null;
                return mplayer.getRole().getPrefix();
            case "factionpower":
                if (!hasFaction) return "0";
                return String.valueOf(faction.getPowerRounded());
            case "factionpowermax":
                if (!hasFaction) return "0";
                return String.valueOf(faction.getPowerMaxRounded());
            case "power":
                return String.valueOf(mplayer.getPowerRounded());
            case "powermax":
                return String.valueOf(mplayer.getPowerMaxRounded());
            case "title":
                if (!hasFaction) return null;
                return mplayer.getTitle();
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, Player target, String identifier)
    {
        if (player.getName().equals(target.getName())) return null;
        switch (identifier)
        {
            case "relation":
                return this.getRelation(player, target);
            case "relation_color":
                return this.getRelationColor(player, target);
        }
        return null;
    }

    private String getRelation(Player player, Player target)
    {
        MPlayer mplayer = MPlayer.get(player);
        MPlayer mtarget = MPlayer.get(target);
        if (mplayer == null || mtarget == null) return null;
        return mplayer.getRelationTo(mtarget).getName();
    }

    private String getRelationColor(Player player, Player target)
    {
        MPlayer mplayer = MPlayer.get(player);
        MPlayer mtarget = MPlayer.get(target);
        if (mplayer == null || mtarget == null) return null;
        ChatColor relationColor = mplayer.getColorTo(mtarget);
        if (relationColor == null) return null;
        return relationColor.toString();
    }

}
