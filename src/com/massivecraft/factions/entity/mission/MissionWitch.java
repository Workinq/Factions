package com.massivecraft.factions.entity.mission;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public class MissionWitch extends AbstractMission
{

    @Override
    public String getMissionName()
    {
        return "Witch";
    }

    @Override
    public String getItemName()
    {
        return MMission.get().witchMission.getItemName();
    }

    @Override
    public String getDescription()
    {
        return MMission.get().witchMission.getDescription();
    }

    @Override
    public Double getRequirement()
    {
        return MMission.get().witchMission.getGoal();
    }

    @Override
    public Integer getReward()
    {
        return MMission.get().witchMission.getCreditsReward();
    }

    @Override
    public Material getItemMaterial()
    {
        return MMission.get().witchMission.getItemMaterial();
    }

    @Override
    public Integer getItemData()
    {
        return MMission.get().witchMission.getItemData();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMobKill(EntityDeathEvent event)
    {
        if (event.getEntity().getKiller() == null) return;
        if (event.getEntityType() != EntityType.WITCH) return;

        MMission.get().incrementProgress(this, MPlayer.get(MPlayer.get(event.getEntity().getKiller())));
    }

}
