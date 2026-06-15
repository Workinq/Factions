package com.massivecraft.factions.action.chunkalt;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.factions.util.AltUtil;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionChunkaltSpawn extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final Player player;
    private final Location location;
    private final int maxAlts;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionChunkaltSpawn(Faction faction, Player player, Location location, int maxAlts)
    {
        this.faction = faction;
        this.player = player;
        this.location = location;
        this.maxAlts = maxAlts;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Args
        MPlayer mplayer = MPlayer.get(player);

        // Verify - Faction
        if (BoardColl.get().getFactionAt(PS.valueOf(player)) != faction)
        {
            mplayer.msg("<b>You can only place chunk alts in your own faction territory.");
            return true;
        }

        // Verify - Maximum alts
        if (faction.getChunkAlts().size() + 1 > maxAlts)
        {
            mplayer.msg("%s <n>cannot spawn more chunk alts as you've reached the limit. Increase this limit using <k>/f upgrade<n>.", mplayer.describeTo(mplayer, true));
            return true;
        }

        // Verify - Solid block
        if ( ! location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
        {
            mplayer.msg("<b>You must spawn chunk alts above a solid block.");
            return true;
        }

        // Verify - Money (one-time spawn cost)
        if ( ! Money.despawn(faction, null, MConf.get().chunkAltSpawnCost))
        {
            mplayer.msg("<b>Your faction cannot afford the <h>$%,.0f <b>chunk alt spawn cost.", MConf.get().chunkAltSpawnCost);
            return true;
        }

        // Args
        ChunkAlt chunkAlt = new ChunkAlt(AltUtil.spawnNpc(player, location, MConf.get().chunkAltName), faction.getId(), location);

        // Apply
        faction.addChunkAlt(chunkAlt);

        // Inform
        mplayer.msg("%s <i>placed a chunk alt at x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <n>(<h>%s<n>)", mplayer.describeTo(mplayer, true), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());

        // Open
        player.openInventory(CmdFactions.get().cmdFactionsChunkAlt.cmdFactionsChunkAltGui.getChunkAltGui(player, mplayer, faction));

        // Return
        return true;
    }

}
