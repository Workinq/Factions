package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.factions.util.AltUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SandAltMenu extends AltMenu<SandAlt>
{
    public SandAltMenu(Player player, MPlayer mplayer, Faction faction)
    {
        super(player, mplayer, faction, MConf.get().sandAltGuiName);
    }

    @Override
    protected List<SandAlt> items()
    {
        return wrap(faction.getSandAlts());
    }

    @Override protected String upgradeName() { return MUpgrade.get().sandAltUpgrade.getUpgradeName(); }
    @Override protected String altHeadName() { return "<k><bold>SAND ALT"; }
    @Override protected String altLabel() { return "sand"; }

    @Override
    protected ItemStack spawnIcon()
    {
        return new ItemBuilder(Material.PLAYER_HEAD).name(Txt.parse("<k><bold>Spawn Alt")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to spawn a sand alt at",
                "<n>where you are currently standing",
                "",
                "<n>It will cost your faction bank",
                Txt.parse("<k>$%.1f <n>per sand placed", MConf.get().sandCost),
                "",
                Txt.parse("<n>Faction Limit: <k>%d", maxAlts),
                "<n>Upgrade limit in <k>/f upgrade")));
    }

    @Override
    protected ItemStack startAllIcon()
    {
        return new ItemBuilder(Material.LIME_DYE).name(Txt.parse("<g><bold>Start All")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to make all your active",
                "<n>sand alts start printing sand")));
    }

    @Override
    protected ItemStack stopAllIcon()
    {
        return new ItemBuilder(Material.RED_DYE).name(Txt.parse("<b><bold>Stop All")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to make all your active",
                "<n>sand alts stop printing sand")));
    }

    @Override
    protected ItemStack despawnAllIcon()
    {
        return new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn All")).withLore(Txt.parse(MUtil.list(
                "<n>Click here to despawn all of",
                "<n>your active sand alts")));
    }

    @Override
    protected void onSpawn(Player player)
    {
        if (BoardColl.get().getFactionAt(PS.valueOf(player)) != faction)
        {
            mplayer.msg("<b>You can only place sand alts in your own faction territory.");
            return;
        }
        if (faction.getSandAlts().size() + 1 > maxAlts)
        {
            mplayer.msg("%s <n>cannot spawn more sand alts as you've reached the limit. Increase this limit using <k>/f upgrade<n>.", mplayer.describeTo(mplayer, true));
            return;
        }
        if ( ! spawnLocation.getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
        {
            mplayer.msg("<b>You must spawn sandalts above a solid block.");
            return;
        }
        if (player.getLocation().getBlockY() > 254)
        {
            mplayer.msg("<b>You must be standing at Y:254 or below to spawn a sand alt.");
            return;
        }

        SandAlt sandAlt = new SandAlt(AltUtil.spawnNpc(player, spawnLocation, MConf.get().sandAltName), faction.getId(), spawnLocation);
        faction.addSandAlt(sandAlt);
        mplayer.msg("%s <i>placed a sand alt at x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <n>(<h>%s<n>)", mplayer.describeTo(mplayer, true), spawnLocation.getBlockX(), spawnLocation.getBlockY(), spawnLocation.getBlockZ(), spawnLocation.getWorld().getName());
        refreshItems();
    }

    @Override
    protected void onStartAll(Player player)
    {
        faction.startAllSandAlts();
        mplayer.msg("%s <h>started <i>all active sand alts.", mplayer.describeTo(mplayer, true));
    }

    @Override
    protected void onStopAll(Player player)
    {
        faction.stopAllSandAlts();
        mplayer.msg("%s <h>stopped <i>all active sand alts.", mplayer.describeTo(mplayer, true));
    }

    @Override
    protected void onDespawnAll(Player player)
    {
        faction.despawnAllSandAlts();
        faction.msg("%s <i>despawned all of <g>your faction's <i>active sand alts.", mplayer.describeTo(faction, true));
        refreshItems();
    }

    @Override
    protected void openEdit(SandAlt alt)
    {
        new SandAltEditMenu(getPlayer(), mplayer, faction, alt).open();
    }
}
