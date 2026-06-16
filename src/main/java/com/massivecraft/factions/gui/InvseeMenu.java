package com.massivecraft.factions.gui;

import com.massivecraft.massivecore.chestgui.type.StandardGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InvseeMenu extends StandardGui
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Player target;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public InvseeMenu(Player viewer, Player target)
    {
        super(viewer, 5, "<gray>" + target.getName() + "'s Inventory");
        this.target = target;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        PlayerInventory inventory = target.getInventory();

        ItemStack[] storage = inventory.getStorageContents();
        for (int slot = 0; slot < storage.length && slot < 36; slot++)
        {
            ItemStack item = storage[slot];
            if (item != null) set(slot, item);
        }

        ItemStack helmet = inventory.getHelmet();
        if (helmet != null) set(36, helmet);

        ItemStack chestplate = inventory.getChestplate();
        if (chestplate != null) set(37, chestplate);

        ItemStack leggings = inventory.getLeggings();
        if (leggings != null) set(38, leggings);

        ItemStack boots = inventory.getBoots();
        if (boots != null) set(39, boots);
    }
}
