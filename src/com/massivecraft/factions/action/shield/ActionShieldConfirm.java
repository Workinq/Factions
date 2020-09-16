package com.massivecraft.factions.action.shield;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionShieldConfirm extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final int from;
    private final Faction faction;
    private final MPlayer mplayer;
    private final String fromText, toText;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionShieldConfirm(int from, Faction faction, MPlayer mplayer, String fromText, String toText)
    {
        this.from = from;
        this.faction = faction;
        this.mplayer = mplayer;
        this.fromText = fromText;
        this.toText = toText;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Apply
        faction.setShieldedHour(from);

        // Inform
        faction.msg("%s <i>set the faction shield from <h>%s <i>to <h>%s<i>.", mplayer.describeTo(faction, true), fromText, toText);
        return true;
    }

}
