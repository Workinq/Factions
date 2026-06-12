package com.massivecraft.factions.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializationUtil
{

    // -------------------------------------------- //
    // INVENTORY SERIALIZING & DESERIALIZING
    // -------------------------------------------- //

    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());
        return new String[]{content, armor};
    }

    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream))
        {
            dataOutput.writeInt(items.length);
            for (ItemStack item : items)
            {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static String toBase64(Inventory inventory) throws IllegalStateException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream))
        {
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); ++i)
            {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Inventory fromBase64(String data, String name)
    {
        if (data.trim().isEmpty())
        {
            return Bukkit.createInventory(null, 27, name);
        }
        else
        {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data)); BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream))
            {
                Inventory inventory = Bukkit.createInventory(null, dataInput.readInt(), name);
                for (int i = 0; i < inventory.getSize(); ++i)
                {
                    inventory.setItem(i, (ItemStack) dataInput.readObject());
                }
                dataInput.close();
                return inventory;
            }
            catch (Exception e)
            {
                Bukkit.getLogger().severe("Unable to decode class type. - " + name);
                return Bukkit.createInventory(null, 27, name);
            }
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException
    {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data)); BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream))
        {
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; ++i)
            {
                items[i] = (ItemStack)dataInput.readObject();
            }
            dataInput.close();
            return items;
        }
        catch (ClassNotFoundException e)
        {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}