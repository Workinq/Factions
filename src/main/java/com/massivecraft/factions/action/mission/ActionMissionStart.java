package com.massivecraft.factions.action.mission;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.Mission;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

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
        // Guard - never re-roll over an active mission.
        if (faction.getActiveMission() != null) return true;

        // Pre-calculate the winner ONCE (rarity-weighted). This is the only RNG.
        Mission winner = MMission.get().selectWeighted();
        if (winner == null) return true;

        // Apply immediately so closing the GUI mid-roll cannot change the result.
        faction.setMissionStart(System.currentTimeMillis());
        faction.setMissionGoal(0);
        faction.setActiveMission(winner.getMissionName());

        // Reveal it with a cosmetic reel that simply lands on the already-decided winner.
        new MissionReel(player, faction, winner).start();

        return true;
    }

}
