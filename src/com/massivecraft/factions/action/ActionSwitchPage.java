package com.massivecraft.factions.action;

import com.massivecraft.factions.util.ScrollerInventory;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionSwitchPage extends ChestActionAbstract
{

    private final ScrollerInventory scrollerInventory;

    public ActionSwitchPage(ScrollerInventory scrollerInventory)
    {
        this.scrollerInventory = scrollerInventory;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Verify
        if (!scrollerInventory.getUsers().containsKey(player.getUniqueId())) return false;
        if (event.getCurrentItem() == null) return false;
        if (!event.getCurrentItem().hasItemMeta()) return false;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return false;

        // Next
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Txt.parse(scrollerInventory.getNextPageButtonName())))
        {
            if (scrollerInventory.getCurrentPage() < scrollerInventory.getPages().size() - 1)
            {
                scrollerInventory.setCurrentPage(scrollerInventory.getCurrentPage() + 1);
                player.openInventory(scrollerInventory.getPages().get(scrollerInventory.getCurrentPage()).getInventory());
            }
            return true;
        }

        // Previous
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Txt.parse(scrollerInventory.getPreviousPageButtonName())))
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
