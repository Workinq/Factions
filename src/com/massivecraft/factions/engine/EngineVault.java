package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.Vault;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EngineVault extends Engine {

    private static EngineVault i = new EngineVault();

    public static EngineVault get() {
        return i;
    }

    @EventHandler
    public void vaultExplodeEvent(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed)) {
            Faction target = BoardColl.get().getFactionAt(PS.valueOf(event.getEntity().getLocation()));
            if (target.getVault() == null) return;
            if (target.getVault().getLocation().asBukkitLocation().getWorld() != event.getEntity().getWorld()) return;
            if (target.getVault().getLocation().asBukkitLocation().distance(event.getEntity().getLocation()) >= 12) return;
            Vault vault = target.getVault();
            for (int value = event.blockList().size() - 1; value > -1; value--) {
                if (vault.getHitVault(event.blockList().get(value).getLocation())) {
                    event.blockList().remove(value);
                }
            }
        }

        TNTPrimed tnt = (TNTPrimed) event.getEntity();
        Faction target = BoardColl.get().getFactionAt(PS.valueOf(tnt.getLocation()));
        if (target.getVault() == null) return;
        if (target.getVault().getLocation().asBukkitLocation().getWorld() != tnt.getWorld()) return;
        if (target.getVault().getLocation().asBukkitLocation().distance(tnt.getLocation()) >= 6) return;

        Vault vault = target.getVault();
        for (int value = (event.blockList().size() - 1); value > -1; value--) {
            if (!(vault.getHitVault(event.blockList().get(value).getLocation()))) continue;
            if (!vault.isDamaged()) {
                vault.damageVault();
            }
            event.blockList().get(value).setType(Material.AIR);
            event.blockList().remove(value);
            if (tnt.getSourceLoc() == null) continue;
            Location source = tnt.getSourceLoc();

            Faction from = BoardColl.get().getFactionAt(PS.valueOf(source));
            if (!from.isNone() && !vault.isDamaged()) {
                from.msg("<b>You have broken into the enemies vault go capture it to steal their f balance");
            }
        }
    }

    @EventHandler
    public void vaultPlaceEvent(BlockPlaceEvent event) {
        Faction target = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock().getLocation()));
        if (target.getVault() == null) return;

        Vault vault = target.getVault();
        if (!(vault.getHitVault(event.getBlock().getLocation()))) return;
        // Cancel
        event.setCancelled(true);

        // Args - MPlayer
        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if (mplayer == null) return;
        if (mplayer.getFaction() != target) return;

        // Inform
        mplayer.msg("<b>You cannot place within your vault.");
    }

    @EventHandler
    public void vaultBreakEvent(BlockBreakEvent event) {
        Faction target = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock().getLocation()));
        if (target.getVault() == null) return;

        final Vault vault = target.getVault();
        if (!(vault.getHitVault(event.getBlock().getLocation()))) return;

        // Cancel
        event.setCancelled(true);

        // Args - MPlayer
        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if (mplayer == null) return;
        if (mplayer.getFaction() != target) return;

        // Inform
        mplayer.msg("<b>You cannot break your own vault.");
    }
}
