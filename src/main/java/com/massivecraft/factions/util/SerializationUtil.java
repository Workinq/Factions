package com.massivecraft.factions.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializationUtil
{
    // -------------------------------------------- //
    // INVENTORY SERIALIZING & DESERIALIZING
    // -------------------------------------------- //

    public static String toBase64(Inventory inventory) throws IllegalStateException
    {
        return toBase64(inventory.getContents());
    }

    public static String toBase64(ItemStack[] items) throws IllegalStateException
    {
        try
        {
            return Base64Coder.encodeLines(ItemStack.serializeItemsAsBytes(items));
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Inventory fromBase64(String data, String name)
    {
        if (data == null || data.trim().isEmpty())
        {
            return Bukkit.createInventory(null, 27, name);
        }

        try
        {
            ItemStack[] items = ItemStack.deserializeItemsFromBytes(Base64Coder.decodeLines(data));
            Inventory inventory = Bukkit.createInventory(null, items.length, name);
            for (int slot = 0; slot < items.length; ++slot)
            {
                ItemStack item = items[slot];
                if (item != null && !item.getType().isAir()) inventory.setItem(slot, item);
            }
            return inventory;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe("Unable to decode faction chest contents. - " + name);
            return Bukkit.createInventory(null, 27, name);
        }
    }

}
