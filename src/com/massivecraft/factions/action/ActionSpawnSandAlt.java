package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionSpawnSandAlt extends ChestActionAbstract
{

    private final Faction faction;
    private final MPlayer mplayer;
    private final PS location;

    public ActionSpawnSandAlt(Faction faction, MPlayer mplayer, PS location)
    {
        this.faction = faction;
        this.mplayer = mplayer;
        this.location = location;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        return true;
    }

}
