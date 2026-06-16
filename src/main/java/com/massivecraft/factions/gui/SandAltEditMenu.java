package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SandAltEditMenu extends AltEditMenu<SandAlt>
{
    public SandAltEditMenu(Player player, MPlayer mplayer, Faction faction, SandAlt alt)
    {
        super(player, mplayer, faction, alt, "<gray>Edit Sand Alt");
    }

    @Override protected String altHeadName() { return "<k><bold>SAND ALT"; }

    @Override
    protected ItemStack toggleIcon()
    {
        int radius = MConf.get().sandSpawnRadius;
        if (alt.isPaused())
        {
            return new ItemBuilder(Material.LIME_DYE).name(Txt.parse("<g><bold>Start Placing")).withLore(Txt.parse(MUtil.list(
                    "<n>Click here to <g>start <n>your alt from",
                    "<n>printing sand in a " + radius + "x" + radius + "x" + radius + " radius.")));
        }
        return new ItemBuilder(Material.RED_DYE).name(Txt.parse("<b><bold>Stop Placing")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to <b>stop <n>your alt from",
                "<n>printing sand in a " + radius + "x" + radius + "x" + radius + " radius.")));
    }

    @Override
    protected ItemStack despawnIcon()
    {
        return new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to despawn this sand alt")));
    }

    @Override
    protected void onToggle()
    {
        alt.setPaused( ! alt.isPaused());
    }

    @Override
    protected void onDespawn(Player player)
    {
        faction.despawnSandAlt(alt);
        backMenu();
    }

    @Override
    protected void backMenu()
    {
        new SandAltMenu(getPlayer(), mplayer, faction).open();
    }
}
