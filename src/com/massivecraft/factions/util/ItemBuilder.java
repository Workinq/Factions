package com.massivecraft.factions.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder extends ItemStack
{

    public ItemBuilder(Material material)
    {
        super(material);
    }

    public ItemBuilder(ItemStack item)
    {
        super(item);
    }

    public ItemBuilder amount(int amount)
    {
        setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name)
    {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> text)
    {
        ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
        {
            lore = new ArrayList<>();
        }
        lore.addAll(text);
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String text)
    {
        ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
        {
            lore = new ArrayList<>();
        }
        lore.add(text);
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String text)
    {
        ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
        {
            lore = new ArrayList<>();
        }
        lore.add(text);
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable)
    {
        ItemMeta meta = getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int durability)
    {
        setDurability((short) durability);
        return this;
    }

    public ItemStack flag(ItemFlag flag)
    {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder data(int data)
    {
        setData(new MaterialData(getType(), (byte) data));
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level)
    {
        addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment)
    {
        addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material)
    {
        setType(material);
        return this;
    }

    public ItemBuilder clearLore()
    {
        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>());
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments()
    {
        getEnchantments().keySet().forEach(this::removeEnchantment);
        return this;
    }

    public ItemBuilder color(Color color)
    {
        if (getType() == Material.LEATHER_BOOTS || getType() == Material.LEATHER_CHESTPLATE || getType() == Material.LEATHER_HELMET || getType() == Material.LEATHER_LEGGINGS)
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
            meta.setColor(color);
            setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor!");
    }

}
