package com.massivecraft.factions.cmd.type;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.FactionWarp;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TypeWarp extends TypeAbstract<FactionWarp>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeWarp i = new TypeWarp();
    public static TypeWarp get() { return i; }
    public TypeWarp() { super(FactionWarp.class); }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getNameInner(FactionWarp value)
    {
        return ChatColor.stripColor(value.getName());
    }

    @Override
    public FactionWarp read(String arg, CommandSender sender) throws MassiveException
    {
        MPlayer mPlayer = MPlayer.get(sender);
        Faction faction = mPlayer.getFaction();
        FactionWarp factionWarp = null;
        for (FactionWarp warp : faction.getWarps())
        {
            if (warp.getName().equalsIgnoreCase(arg))
            {
                factionWarp = warp;
            }
        }
        if (factionWarp != null)
        {
            return factionWarp;
        }
        throw new MassiveException().addMsg("<b>%s<b> does not have any warps with the name %s<b>.", faction.describeTo(mPlayer, true), arg);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg)
    {
        Set<String> ret = new TreeSet<>(ComparatorCaseInsensitive.get());

        if (!(sender instanceof Player)) return ret;

        MPlayer mPlayer = MPlayer.get(sender);
        Faction faction = mPlayer.getFaction();

        if (faction.isNone()) return ret;

        for (FactionWarp warp : faction.getWarps())
        {
            ret.add(ChatColor.stripColor(warp.getName()));
        }

        return ret;
    }

}
