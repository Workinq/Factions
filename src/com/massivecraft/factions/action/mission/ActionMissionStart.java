package com.massivecraft.factions.action.mission;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.AbstractMission;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Random;

public class ActionMissionStart extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionMissionStart(Faction faction)
    {
        this.faction = faction;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Args
        AbstractMission mission = MMission.get().getMissions().get(new Random().nextInt(MMission.get().getMissions().size()));

        // Apply
        faction.setMissionStart(System.currentTimeMillis());
        faction.setMissionGoal(0);
        faction.setActiveMission(mission.getMissionName());

        // Inform
        faction.msg("<g>The mission <i>%s <g>is now active. You have <i>1 day <g>to complete it.", mission.getMissionName());

        // Return
        return true;
    }

}
