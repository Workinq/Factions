package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionConfirmShield extends ChestActionAbstract
{

    private final int from;
    private final Faction faction;
    private final MPlayer mplayer;
    private final String time;

    public ActionConfirmShield(int from, Faction faction, MPlayer mplayer, String time)
    {
        this.from = from;
        this.faction = faction;
        this.mplayer = mplayer;
        this.time = time;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        faction.setShieldedHour(from);
        faction.setShieldString(time);

        faction.msg("%s <i>set the faction shield from %s", mplayer.describeTo(faction, true), time.substring(0, time.indexOf('(') - 2));
        return true;
    }

}
