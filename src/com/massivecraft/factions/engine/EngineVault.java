package com.massivecraft.factions.engine;

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

public class EngineVault extends Engine
{

    private static EngineVault i = new EngineVault();
    public static EngineVault get() { return i; }

    @EventHandler
    public void vaultExplodeEvent(BlockExplodeEvent event)
    {
        final Faction target = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock().getLocation()));
        if (target.getVault() == null)return;
        final Vault vault = target.getVault();
        for (Block block : event.blockList())
        {
            if (vault.getHitBottom(block.getLocation()))
            {
                event.blockList().remove(block);
                continue;
            }
            if (vault.getHitBreakable(block.getLocation()))
            {
                this.vaultDamaged(vault);
                return;
            }
        }
    }

    @EventHandler
    public void vaultBreakEvent(BlockBreakEvent event)
    {
        Faction target = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock().getLocation()));
        if (target.getVault() == null) return;

        Vault vault = target.getVault();
        if ( ! (vault.getHitBottom(event.getBlock().getLocation())) ) return;
        if ( ! (vault.getHitBreakable(event.getBlock().getLocation())) ) return;

        // Cancel
        event.setCancelled(true);

        // Args - MPlayer
        MPlayer mPlayer = MPlayer.get(event.getPlayer());
        if (mPlayer == null) return;
        if (mPlayer.getFaction() != target) return;

        // Inform
        mPlayer.msg("<b>You cannot break your own vault.");
    }


    public void vaultDamaged(Vault vault)
    {
        vault.setDamaged(true);
        final long time = System.currentTimeMillis() + (1000L * 1200);
        vault.setWhenCanRepair(time);
        // todo: start koth
    }

    public void vaultRepaired(Vault vault)
    {
        vault.setDamaged(false);
        vault.setWhenCanRepair(0);
        // todo: stop koth
        // todo: reschem vault
    }

}
