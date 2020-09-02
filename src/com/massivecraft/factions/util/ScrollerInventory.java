package com.massivecraft.factions.util;

import com.massivecraft.factions.action.ActionSwitchPage;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class ScrollerInventory
{

    // DATA STRUCTURES
    public static final Map<UUID, ScrollerInventory> USERS = new HashMap<>();

    // PAGES
    private final List<ChestGui> pages = new ArrayList<>();
    public List<ChestGui> getPages() { return pages; }

    // BUTTONS
    private final ItemStack nextPageButton;
    public ItemStack getNextPageButton() { return nextPageButton; }

    private final ItemStack previousPageButton;
    public ItemStack getPreviousPageButton() { return previousPageButton; }

    // PAGE NUMBER
    private int currentPage = 0;
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ScrollerInventory()
    {
        this.nextPageButton = new ItemBuilder(Material.ARROW).name(Txt.parse("<k><bold>Next Page"));
        this.previousPageButton = new ItemBuilder(Material.ARROW).name(Txt.parse("<k><bold>Previous Page"));
    }

    // -------------------------------------------- //
    // METHODS
    // -------------------------------------------- //

    public ChestGui getBlankPage(String name, int size, Player player)
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, size, name);
        ChestGui chestGui = InventoryUtil.getChestGui(inventory, false, false);

        // Next
        inventory.setItem(51, nextPageButton);
        chestGui.setAction(51, new ActionSwitchPage(this));

        // Previous
        inventory.setItem(47, previousPageButton);
        chestGui.setAction(47, new ActionSwitchPage(this));

        // Add
        pages.add(chestGui);
        USERS.put(player.getUniqueId(), this);

        // Return
        return chestGui;
    }

    // -------------------------------------------- //
    // ABSTRACT
    // -------------------------------------------- //

    public abstract void fillSidesWithItem(Inventory inventory, ItemStack item);
    public abstract List<Integer> getNonSideSlots(Inventory inventory);
    public abstract List<Integer> getEmptyNonSideSlots(Inventory inventory);

}
