package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.factions.mission.Mission;
import com.massivecraft.factions.mission.MissionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Random;

public class ActionStartMission extends ChestActionAbstract
{

    private final Faction faction;

    public ActionStartMission(Faction faction)
    {
        this.faction = faction;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        Mission mission = MissionsManager.get().getMissions().get(new Random().nextInt(MissionsManager.get().getMissions().size()));
        faction.msg("<g>The mission <i>%s <g>is now active. You have <i>1 day <g>to complete it.", mission.getName());
        faction.setMissionStart(System.currentTimeMillis());
        faction.setMissionGoal(0);
        faction.setActiveMission(mission.getName());
        player.closeInventory();
        return true;
    }

}
