package com.massivecraft.factions.action;

import com.massivecraft.factions.util.ScrollerInventory;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionScollerInventorySwitchPage extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final ScrollerInventory scrollerInventory;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionScollerInventorySwitchPage(ScrollerInventory scrollerInventory)
    {
        this.scrollerInventory = scrollerInventory;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Verify
        if (!ScrollerInventory.OPEN_INVENTORIES.containsKey(player.getUniqueId())) return false;
        if (event.getCurrentItem() == null) return false;
        if (!event.getCurrentItem().hasItemMeta()) return false;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return false;

        // Next
        if (event.getRawSlot() == 51)
        {
            if (scrollerInventory.getCurrentPage() < scrollerInventory.getPages().size() - 1)
            {
                scrollerInventory.setCurrentPage(scrollerInventory.getCurrentPage() + 1);
                player.openInventory(scrollerInventory.getPages().get(scrollerInventory.getCurrentPage()).getInventory());
            }
            return true;
        }

        // Previous
        if (event.getRawSlot() == 47)
        {
            if (scrollerInventory.getCurrentPage() > 0)
            {
                scrollerInventory.setCurrentPage(scrollerInventory.getCurrentPage() - 1);
                player.openInventory(scrollerInventory.getPages().get(scrollerInventory.getCurrentPage()).getInventory());
            }
            return true;
        }

        // Return
        return false;
    }

}
