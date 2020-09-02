package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.AbstractMission;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Random;

public class ActionStartMission extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionStartMission(Faction faction)
    {
        this.faction = faction;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        AbstractMission mission = MMission.get().getMissions().get(new Random().nextInt(MMission.get().getMissions().size()));
        faction.msg("<g>The mission <i>%s <g>is now active. You have <i>1 day <g>to complete it.", mission.getMissionName());
        faction.setMissionStart(System.currentTimeMillis());
        faction.setMissionGoal(0);
        faction.setActiveMission(mission.getMissionName());
        player.closeInventory();
        return true;
    }

}
