package com.massivecraft.factions.util;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil
{

    // -------------------------------------------- //
    // TNT COUNTING
    // -------------------------------------------- //

    public static int getTntIn(Inventory inventory)
    {
        int totalTnt = 0;
        for (ItemStack item : inventory.getContents())
        {
            if (item == null || item.getType() != Material.TNT) continue;

            if (!item.hasItemMeta() && !item.getItemMeta().hasDisplayName() && !item.getItemMeta().hasLore())
            {
                totalTnt += item.getAmount();
            }
        }
        return totalTnt;
    }

    public static int getOtherIn(Inventory inventory)
    {
        int totalNotTnt = 0;
        for (ItemStack item : inventory.getContents())
        {
            if (item == null || item.getType() == Material.TNT) continue;

            if (item.hasItemMeta() || item.getItemMeta().hasDisplayName() || item.getItemMeta().hasLore())
            {
                totalNotTnt += 64;
            }
        }
        return totalNotTnt;
    }

    public static void fillInventory(Inventory inventory)
    {
        ItemStack filler = new ItemBuilder(MConf.get().fillerItemMaterial).name(Txt.parse(MConf.get().fillerItemName)).durability(MConf.get().fillerItemData);
        for (int i = 0; i < inventory.getSize(); i++)
        {
            if (inventory.getItem(i) != null) continue;
            inventory.setItem(i, filler);
        }
    }

}
