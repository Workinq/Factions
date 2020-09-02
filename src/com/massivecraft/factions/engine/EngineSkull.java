package com.massivecraft.factions.engine;

import com.google.common.collect.Iterables;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EngineSkull extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineSkull i = new EngineSkull();
    public static EngineSkull get() { return i; }

    // -------------------------------------------- //
    // LOGIN
    // -------------------------------------------- //

    @EventHandler public void login(PlayerJoinEvent event) { this.handleTexture(event.getPlayer()); }

    private void handleTexture(Player player)
    {
        // Args
        MPlayer mplayer = MPlayer.get(player);

        // Update
        try
        {
            Method getHandle = player.getClass().getDeclaredMethod("getHandle");
            getHandle.setAccessible(true);
            Object entityPlayer = getHandle.invoke(player);
            if (entityPlayer == null) return;

            Method getProfile = entityPlayer.getClass().getMethod("getProfile");
            getProfile.setAccessible(true);
            GameProfile profile = (GameProfile) getProfile.invoke(entityPlayer);
            if (profile == null) return;

            Property textureProperty = Iterables.getFirst(profile.getProperties().get("textures"), null);
            if (textureProperty == null) return;

            // Apply
            mplayer.setSkullTexture(textureProperty.getValue());
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored)
        {
        }
    }

    public ItemStack getSkullItem(OfflinePlayer player)
    {
        // Args
        MPlayer mplayer = MPlayer.get(player);
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

        // Verify
        if (mplayer != null)
        {
            String texture = mplayer.getSkullTexture();
            if (texture != null)
            {
                try
                {
                    // Args
                    String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);

                    // getting the item as nms
                    Class<?> craftItemStack = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
                    Method asNmsCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
                    asNmsCopyMethod.setAccessible(true);
                    Object vanillaStack = asNmsCopyMethod.invoke(null, skull);
                    if (vanillaStack == null) return skull;

                    // check if there is an nbt tag
                    Method hasTagMethod = vanillaStack.getClass().getDeclaredMethod("hasTag");
                    hasTagMethod.setAccessible(true);
                    boolean hasTag = (boolean) hasTagMethod.invoke(vanillaStack);

                    // get the nbt tag
                    Object baseCompound;
                    if (hasTag)
                    {
                        Method getTagMethod = vanillaStack.getClass().getDeclaredMethod("getTag");
                        baseCompound = getTagMethod.invoke(vanillaStack);
                    }
                    else
                    {
                        baseCompound = Class.forName("net.minecraft.server." + version + ".NBTTagCompound").newInstance();
                    }

                    // skin stuff
                    GameProfile profile = new GameProfile(player.getUniqueId(), player.getName());
                    profile.getProperties().put("textures", new Property("textures", texture));
                    Object skullOwner = Class.forName("net.minecraft.server." + version + ".NBTTagCompound").newInstance();
                    Class<?> gameProfileSerializer = Class.forName("net.minecraft.server." + version + ".GameProfileSerializer");
                    Method serializeMethod = gameProfileSerializer.getDeclaredMethod("serialize", Class.forName("net.minecraft.server." + version + ".NBTTagCompound"), GameProfile.class);
                    serializeMethod.setAccessible(true);
                    serializeMethod.invoke(null, skullOwner, profile);

                    // set the required tags to set the skull skin
                    Method setMethod = baseCompound.getClass().getDeclaredMethod("set", String.class, Class.forName("net.minecraft.server." + version + ".NBTBase"));
                    setMethod.setAccessible(true);
                    setMethod.invoke(baseCompound, "SkullOwner", skullOwner);

                    Method setTagMethod = vanillaStack.getClass().getDeclaredMethod("setTag", Class.forName("net.minecraft.server." + version + ".NBTTagCompound"));
                    setTagMethod.setAccessible(true);
                    setTagMethod.invoke(vanillaStack, baseCompound);

                    // return
                    Method asBukkitCopyMethod = craftItemStack.getDeclaredMethod("asBukkitCopy", Class.forName("net.minecraft.server." + version + ".ItemStack"));
                    asBukkitCopyMethod.setAccessible(true);
                    ItemStack asBukkitCopy = (ItemStack) asBukkitCopyMethod.invoke(null, vanillaStack);

                    Method asCraftCopyMethod = craftItemStack.getDeclaredMethod("asCraftCopy", ItemStack.class);
                    asCraftCopyMethod.setAccessible(true);
                    Object asCraftCopy = asCraftCopyMethod.invoke(null, asBukkitCopy);

                    return (ItemStack) asCraftCopy;
                }
                catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | InstantiationException ignored)
                {
                }
            }
        }

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(player.getName());
        skull.setItemMeta(skullMeta);
        return skull;
    }

}
