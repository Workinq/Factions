package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.entity.object.Alt;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.PagedGui;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AltMenu<T extends Alt<T>> extends PagedGui<T>
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    protected final MPlayer mplayer;
    protected final Faction faction;
    protected final Location spawnLocation;
    protected final int maxAlts;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    protected AltMenu(Player player, MPlayer mplayer, Faction faction, String title)
    {
        super(player, 6, title);
        this.mplayer = mplayer;
        this.faction = faction;
        this.spawnLocation = player.getLocation();
        this.maxAlts = computeMaxAlts(faction, this.upgradeName());
    }

    private static int computeMaxAlts(Faction faction, String upgradeName)
    {
        int level = faction.getLevel(upgradeName);
        if (level == 0) return 5;
        return Integer.parseInt(MUpgrade.get().getUpgradeByName(upgradeName).getCurrentDescription()[level - 1].split(" ")[0]);
    }

    // -------------------------------------------- //
    // HOOKS (sand vs chunk)
    // -------------------------------------------- //

    protected abstract String upgradeName();
    protected abstract String altHeadName();
    protected abstract String altLabel();
    protected abstract ItemStack spawnIcon();
    protected abstract ItemStack startAllIcon();
    protected abstract ItemStack stopAllIcon();
    protected abstract ItemStack despawnAllIcon();
    protected abstract void onSpawn(Player player);
    protected abstract void onStartAll(Player player);
    protected abstract void onStopAll(Player player);
    protected abstract void onDespawnAll(Player player);
    protected abstract void openEdit(T alt);

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        super.build();
        button(45, spawnIcon(), ctx -> onSpawn(ctx.getPlayer()));
        button(46, startAllIcon(), ctx -> onStartAll(ctx.getPlayer()));
        button(47, stopAllIcon(), ctx -> onStopAll(ctx.getPlayer()));
        button(49, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
        button(52, despawnAllIcon(), ctx -> onDespawnAll(ctx.getPlayer()));
    }

    @Override
    protected ItemStack icon(T alt)
    {
        Location location = alt.getLocation();
        return new ItemBuilder(Material.PLAYER_HEAD).name(Txt.parse(altHeadName())).withLore(Txt.parse(MUtil.list(
                "<n>Click to manage the " + altLabel() + " alt at",
                Txt.parse("<i>x: <h>%,d <i>y: <h>%,d <i>z: <h>%,d <i>world: <h>%s", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()))));
    }

    @Override
    protected void onPick(Player player, T alt)
    {
        openEdit(alt);
    }

    // Exposed so subclasses can wrap the faction's alt collection.
    protected List<T> wrap(java.util.Collection<T> alts)
    {
        return new MassiveList<>(alts);
    }
}
