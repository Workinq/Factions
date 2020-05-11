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
        this.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name)
    {
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(name);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> text)
    {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
        {
            lore = new ArrayList<>();
        }
        lore.addAll(text);
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String text)
    {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
        {
            lore = new ArrayList<>();
        }
        lore.add(text);
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String text)
    {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
        {
            lore = new ArrayList<>();
        }
        lore.add(text);
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable)
    {
        ItemMeta meta = this.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int durability)
    {
        this.setDurability((short) durability);
        return this;
    }

    public ItemStack flag(ItemFlag flag)
    {
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(flag);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder data(int data)
    {
        this.setData(new MaterialData(getType(), (byte) data));
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level)
    {
        this.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment)
    {
        this.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material)
    {
        this.setType(material);
        return this;
    }

    public ItemBuilder clearLore()
    {
        ItemMeta meta = this.getItemMeta();
        meta.setLore(new ArrayList<>());
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments()
    {
        this.getEnchantments().keySet().forEach(this::removeEnchantment);
        return this;
    }

    public ItemBuilder color(Color color)
    {
        if (getType() == Material.LEATHER_BOOTS || getType() == Material.LEATHER_CHESTPLATE || getType() == Material.LEATHER_HELMET || getType() == Material.LEATHER_LEGGINGS)
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) this.getItemMeta();
            meta.setColor(color);
            this.setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor!");
    }

}
