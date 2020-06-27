package com.massivecraft.factions.engine.vault;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.Vault;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;

public class EngineVault extends Engine {

    private static EngineVault i = new EngineVault();
    public static EngineVault get() { return i; }

    @EventHandler
    public void vaultExplodeEvent(BlockExplodeEvent e) {
        final Faction target = BoardColl.get().getFactionAt(PS.valueOf(e.getBlock().getLocation()));
        if(target.getVault() == null)return;
        final Vault vault = target.getVault();
        for(Block block : e.blockList()) {
            if(vault.getHitBottom(block.getLocation())) {e.blockList().remove(block);continue; }
            if(vault.getHitBreakable(block.getLocation())) { vaultDamaged(vault);return; }
        }
    }

    @EventHandler
    public void vaultBreakEvent(BlockBreakEvent e) {
        final Faction target = BoardColl.get().getFactionAt(PS.valueOf(e.getBlock().getLocation()));
        if(target.getVault() == null)return;
        final Vault vault = target.getVault();
        if(!(vault.getHitBottom(e.getBlock().getLocation())))return;
        if(!(vault.getHitBreakable(e.getBlock().getLocation())))return;
        e.setCancelled(true);
        MPlayer mPlayer = MPlayer.get(e.getPlayer());
        if(mPlayer == null)return;
        if(mPlayer.getFaction() != target)return;
        mPlayer.msg("You cannot break your own vault.");
    }


    public void vaultDamaged(Vault vault) {
        vault.setDamaged(true);
        final long time = System.currentTimeMillis() + (1000L * 1200);
        vault.setWhenCanRepair(time);
        // todo: start koth
    }

    public void vaultRepaired(Vault vault) {
        vault.setDamaged(false);
        vault.setWhenCanRepair(0);
        // todo: stop koth
        // todo: reschem vault
    }
}
