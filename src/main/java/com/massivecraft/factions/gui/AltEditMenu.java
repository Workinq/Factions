package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.Alt;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.StandardGui;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AltEditMenu<T extends Alt<T>> extends StandardGui
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    protected final MPlayer mplayer;
    protected final Faction faction;
    protected final T alt;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    protected AltEditMenu(Player player, MPlayer mplayer, Faction faction, T alt, String title)
    {
        super(player, 3, title);
        this.mplayer = mplayer;
        this.faction = faction;
        this.alt = alt;
    }

    // -------------------------------------------- //
    // HOOKS (sand vs chunk)
    // -------------------------------------------- //

    protected abstract String altHeadName();
    protected abstract ItemStack toggleIcon();
    protected abstract ItemStack despawnIcon();
    protected abstract void onToggle();
    protected abstract void onDespawn(Player player);
    protected abstract void backMenu();

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        fillBorder();
        button(11, toggleIcon(), ctx -> { onToggle(); refresh(); });
        set(13, infoIcon());
        button(15, despawnIcon(), ctx -> onDespawn(ctx.getPlayer()));
        button(22, new ItemBuilder(Material.ARROW).name(Txt.parse("<i>Back")), ctx -> backMenu());
    }

    // Rebuild so the toggle button reflects the new paused state.
    protected void refresh()
    {
        clearContent();
        build();
        render();
    }

    protected ItemStack infoIcon()
    {
        Location location = alt.getLocation();
        return new ItemBuilder(Material.PLAYER_HEAD).name(Txt.parse(altHeadName())).withLore(Txt.parse(MUtil.list(
                Txt.parse("<i>x: <h>%,d <i>y: <h>%,d <i>z: <h>%,d <i>world: <h>%s", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()))));
    }
}
