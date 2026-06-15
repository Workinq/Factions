package com.massivecraft.factions.action.chunkalt;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionChunkaltDespawn extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final MPlayer mplayer;
    private final ChunkAlt chunkAlt;
    private final boolean redirect;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionChunkaltDespawn(Faction faction, MPlayer mplayer, ChunkAlt chunkAlt, boolean redirect)
    {
        this.faction = faction;
        this.mplayer = mplayer;
        this.chunkAlt = chunkAlt;
        this.redirect = redirect;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Verify
        if (chunkAlt == null)
        {
            // Despawn all
            faction.despawnAllChunkAlts();
            faction.msg("%s <i>despawned all of <g>your faction's <i>active chunk alts.", mplayer.describeTo(faction, true));
        }
        else
        {
            // Despawn one
            faction.despawnChunkAlt(chunkAlt);
        }

        // Open gui
        if (redirect || chunkAlt == null)
        {
            player.openInventory(CmdFactions.get().cmdFactionsChunkAlt.cmdFactionsChunkAltGui.getChunkAltGui(player, mplayer, faction));
        }

        // Return
        return true;
    }

}
