package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.Vault;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class EngineVault extends Engine
{

    private static EngineVault i = new EngineVault();
    public static EngineVault get() { return i; }

    @EventHandler
    public void vaultExplodeEvent(EntityExplodeEvent event) {
        if ( ! (event.getEntity() instanceof TNTPrimed) ) return;
        final TNTPrimed tnt = (TNTPrimed) event.getEntity();
        final Faction target = BoardColl.get().getFactionAt(PS.valueOf(tnt.getLocation()));
        if (target.getVault() == null) return;
        if(target.getVault().getLocation().asBukkitLocation().getWorld() != tnt.getWorld())return;
        if(target.getVault().getLocation().asBukkitLocation().distance(tnt.getLocation()) >= 6)return;
        final Vault vault = target.getVault();
        for(int value = (event.blockList().size()-1);value > -1;value--) {
            if (vault.getHitBottom(event.blockList().get(value).getLocation())) {
                event.blockList().remove(value);
            } else if (vault.getHitBreakable(event.blockList().get(value).getLocation())) {
                vault.damageVault();
            }
        }
    }

    @EventHandler
    public void vaultBreakEvent(BlockBreakEvent event) {
        final Faction target = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock().getLocation()));
        if (target.getVault() == null) return;

        final Vault vault = target.getVault();
        if (!(vault.getHitBottom(event.getBlock().getLocation()) || vault.getHitBreakable(event.getBlock().getLocation())))return;
        // Cancel
        event.setCancelled(true);

        // Args - MPlayer
        final MPlayer mPlayer = MPlayer.get(event.getPlayer());
        if (mPlayer == null) return;
        if (mPlayer.getFaction() != target) return;

        // Inform
        mPlayer.msg("<b>You cannot break your own vault.");
    }
}
