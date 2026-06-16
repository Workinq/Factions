package com.massivecraft.factions.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    // -------------------------------------------- //
    // TNT COUNTING
    // -------------------------------------------- //

    public static int getTntIn(Inventory inventory)
    {
        int totalTnt = 0;
        for (ItemStack item : inventory.getContents())
        {
            if (item == null || item.getType() != Material.TNT) continue;
            if (!item.hasItemMeta() && !item.getItemMeta().hasDisplayName() && !item.getItemMeta().hasLore()) totalTnt += item.getAmount();
        }
        return totalTnt;
    }

    public static int getOtherIn(Inventory inventory)
    {
        int totalNotTnt = 0;
        for (ItemStack item : inventory.getContents())
        {
            if (item == null || item.getType() == Material.TNT) continue;
            totalNotTnt += 64;
        }
        return totalNotTnt;
    }

}
