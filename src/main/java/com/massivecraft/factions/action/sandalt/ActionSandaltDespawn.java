package com.massivecraft.factions.action.sandalt;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionSandaltDespawn extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final MPlayer mplayer;
    private final SandAlt sandAlt;
    private final boolean redirect;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionSandaltDespawn(Faction faction, MPlayer mplayer, SandAlt sandAlt, boolean redirect)
    {
        this.faction = faction;
        this.mplayer = mplayer;
        this.sandAlt = sandAlt;
        this.redirect = redirect;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Verify
        if (sandAlt == null)
        {
            // Despawn all
            faction.despawnAllSandAlts();
            faction.msg("%s <i>despawned all of <g>your faction's <i>active sand alts.", mplayer.describeTo(faction, true));
        }
        else
        {
            // Despawn one
            faction.despawnSandAlt(sandAlt);
        }

        // Open gui
        if (redirect || sandAlt == null)
        {
            player.openInventory(CmdFactions.get().cmdFactionsSandAlt.cmdFactionsSandAltGui.getSandAltGui(player, mplayer, faction));
        }

        // Return
        return true;
    }

}
