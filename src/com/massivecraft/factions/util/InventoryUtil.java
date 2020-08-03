package com.massivecraft.factions.util;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

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
            if (item.hasItemMeta() || item.getItemMeta().hasDisplayName() || item.getItemMeta().hasLore()) totalNotTnt += 64;
        }
        return totalNotTnt;
    }

    public static void fillInventory(Inventory inventory)
    {
        fillInventory(inventory, new int[]{});
    }

    public static void fillInventory(Inventory inventory, int[] slots)
    {
        ItemStack filler = new ItemBuilder(MConf.get().fillerItemMaterial).name(Txt.parse(MConf.get().fillerItemName)).durability(MConf.get().fillerItemData);
        for (int i = 0; i < inventory.getSize(); i++)
        {
            final int slot = i;
            if (Arrays.stream(slots).anyMatch(num -> num == slot)) continue;
            if (inventory.getItem(slot) != null) continue;
            inventory.setItem(slot, filler);
        }
    }

    public static ChestGui getChestGui(Inventory inventory, boolean autoClosing, boolean autoRemoving)
    {
        // Args
        ChestGui chestGui = ChestGui.getCreative(inventory);

        // Chest Setup
        chestGui.setAutoclosing(autoClosing);
        chestGui.setAutoremoving(autoRemoving);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        // Return
        return chestGui;
    }
    public static ChestGui getChestGui(Inventory inventory, boolean autoClosing) { return getChestGui(inventory, autoClosing, true); }
    public static ChestGui getChestGui(Inventory inventory) { return getChestGui(inventory, true); }

}
