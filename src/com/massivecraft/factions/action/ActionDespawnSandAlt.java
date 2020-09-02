package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nullable;

public class ActionDespawnSandAlt extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final MPlayer mplayer;
    private final SandAlt sandAlt;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionDespawnSandAlt(Faction faction, MPlayer mplayer, @Nullable SandAlt sandAlt)
    {
        this.faction = faction;
        this.mplayer = mplayer;
        this.sandAlt = sandAlt;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        if (sandAlt == null)
        {
            faction.despawnAllSandAlts();
            faction.msg("%s <i>despawned all of <g>your faction's <i>active sand alts.", mplayer.describeTo(faction, true));
        }
        else
        {
            faction.despawnSandAlt(sandAlt);
        }
        return true;
    }

}
