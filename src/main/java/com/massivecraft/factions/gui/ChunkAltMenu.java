package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.factions.util.AltUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChunkAltMenu extends AltMenu<ChunkAlt>
{
    public ChunkAltMenu(Player player, MPlayer mplayer, Faction faction)
    {
        super(player, mplayer, faction, MConf.get().chunkAltGuiName);
    }

    @Override
    protected List<ChunkAlt> items()
    {
        return wrap(faction.getChunkAlts());
    }

    @Override protected String upgradeName() { return MUpgrade.get().chunkAltUpgrade.getUpgradeName(); }
    @Override protected String altHeadName() { return "<k><bold>CHUNK ALT"; }
    @Override protected String altLabel() { return "chunk"; }

    @Override
    protected ItemStack spawnIcon()
    {
        return new ItemBuilder(Material.PLAYER_HEAD).name(Txt.parse("<k><bold>Spawn Alt")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to spawn a chunk alt at",
                "<n>where you are currently standing",
                "",
                "<n>It will cost your faction bank",
                Txt.parse("<k>$%,.0f <n>to spawn", MConf.get().chunkAltSpawnCost),
                "",
                Txt.parse("<n>Faction Limit: <k>%d", maxAlts),
                "<n>Upgrade limit in <k>/f upgrade")));
    }

    @Override
    protected ItemStack startAllIcon()
    {
        return new ItemBuilder(Material.LIME_DYE).name(Txt.parse("<g><bold>Start All")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to make all your",
                "<n>chunk alts keep their chunks loaded")));
    }

    @Override
    protected ItemStack stopAllIcon()
    {
        return new ItemBuilder(Material.RED_DYE).name(Txt.parse("<b><bold>Stop All")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to make all your",
                "<n>chunk alts stop keeping chunks loaded")));
    }

    @Override
    protected ItemStack despawnAllIcon()
    {
        return new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn All")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to despawn all of",
                "<n>your active chunk alts")));
    }

    @Override
    protected void onSpawn(Player player)
    {
        if (BoardColl.get().getFactionAt(PS.valueOf(player)) != faction)
        {
            mplayer.msg("<b>You can only place chunk alts in your own faction territory.");
            return;
        }
        if (faction.getChunkAlts().size() + 1 > maxAlts)
        {
            mplayer.msg("%s <n>cannot spawn more chunk alts as you've reached the limit. Increase this limit using <k>/f upgrade<n>.", mplayer.describeTo(mplayer, true));
            return;
        }
        if ( ! spawnLocation.getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
        {
            mplayer.msg("<b>You must spawn chunk alts above a solid block.");
            return;
        }
        if ( ! Money.despawn(faction, null, MConf.get().chunkAltSpawnCost))
        {
            mplayer.msg("<b>Your faction cannot afford the <h>$%,.0f <b>chunk alt spawn cost.", MConf.get().chunkAltSpawnCost);
            return;
        }

        ChunkAlt chunkAlt = new ChunkAlt(AltUtil.spawnNpc(player, spawnLocation, MConf.get().chunkAltName), faction.getId(), spawnLocation);
        faction.addChunkAlt(chunkAlt);
        mplayer.msg("%s <i>placed a chunk alt at x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <n>(<h>%s<n>)", mplayer.describeTo(mplayer, true), spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation.getBlockZ(), spawnLocation.getWorld().getName());
        refreshItems();
    }

    @Override
    protected void onStartAll(Player player)
    {
        faction.startAllChunkAlts();
        mplayer.msg("%s <h>started <i>all active chunk alts.", mplayer.describeTo(mplayer, true));
    }

    @Override
    protected void onStopAll(Player player)
    {
        faction.stopAllChunkAlts();
        mplayer.msg("%s <h>stopped <i>all active chunk alts.", mplayer.describeTo(mplayer, true));
    }

    @Override
    protected void onDespawnAll(Player player)
    {
        faction.despawnAllChunkAlts();
        faction.msg("%s <i>despawned all of <g>your faction's <i>active chunk alts.", mplayer.describeTo(faction, true));
        refreshItems();
    }

    @Override
    protected void openEdit(ChunkAlt alt)
    {
        new ChunkAltEditMenu(getPlayer(), mplayer, faction, alt).open();
    }
}
