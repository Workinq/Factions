package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionPrintSandAlt extends ChestActionAbstract
{

    private final SandAlt sandAlt;
    private final boolean paused;

    public ActionPrintSandAlt(SandAlt sandAlt, boolean paused)
    {
        this.sandAlt = sandAlt;
        this.paused = paused;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Apply
        sandAlt.setPaused(paused);

        // Mark as changed
        sandAlt.changed();

        return true;
    }

}
