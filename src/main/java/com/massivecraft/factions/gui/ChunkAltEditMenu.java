package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.factions.util.AltUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChunkAltEditMenu extends AltEditMenu<ChunkAlt>
{
    public ChunkAltEditMenu(Player player, MPlayer mplayer, Faction faction, ChunkAlt alt)
    {
        super(player, mplayer, faction, alt, "<gray>Edit Chunk Alt");
    }

    @Override protected String altHeadName() { return "<k><bold>CHUNK ALT"; }

    @Override
    protected ItemStack toggleIcon()
    {
        int span = MConf.get().chunkAltRadius * 2 + 1;
        if (alt.isPaused())
        {
            return new ItemBuilder(Material.LIME_DYE).name(Txt.parse("<g><bold>Start Loading")).withLore(Txt.parse(MUtil.list(
                    "<n>Click here to <g>start <n>keeping a",
                    "<n>" + span + "x" + span + " chunk area loaded.")));
        }
        return new ItemBuilder(Material.RED_DYE).name(Txt.parse("<b><bold>Stop Loading")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to <b>stop <n>keeping a",
                "<n>" + span + "x" + span + " chunk area loaded.")));
    }

    @Override
    protected ItemStack despawnIcon()
    {
        return new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to despawn this chunk alt")));
    }

    @Override
    protected void onToggle()
    {
        boolean paused = ! alt.isPaused();
        alt.setPaused(paused);
        AltUtil.setLoaded(alt, ! paused);
    }

    @Override
    protected void onDespawn(Player player)
    {
        faction.despawnChunkAlt(alt);
        backMenu();
    }

    @Override
    protected void backMenu()
    {
        new ChunkAltMenu(getPlayer(), mplayer, faction).open();
    }
}
