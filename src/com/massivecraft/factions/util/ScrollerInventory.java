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

    private final Map<UUID, ScrollerInventory> users = new HashMap<>();
    private final List<ChestGui> pages = new ArrayList<>();
    private final ItemStack nextPageButton, previousPageButton;
    private int currentPage = 0;
    private UUID id;

    public ScrollerInventory()
    {
        this.nextPageButton = new ItemBuilder(Material.ARROW).name(Txt.parse("<k><bold>Next Page"));
        this.previousPageButton = new ItemBuilder(Material.ARROW).name(Txt.parse("<k><bold>Previous Page"));
    }

    public ChestGui getBlankPage(String name, int size, Player player)
    {
        // Args
        this.id = UUID.randomUUID();
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
        users.put(player.getUniqueId(), this);

        // Return
        return chestGui;
    }

    public abstract void fillSidesWithItem(Inventory inventory, ItemStack item);
    public abstract List<Integer> getNonSideSlots(Inventory inventory);
    public abstract List<Integer> getEmptyNonSideSlots(Inventory inventory);

    public Map<UUID, ScrollerInventory> getUsers() { return users; }
    public List<ChestGui> getPages() { return pages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage += currentPage; }
    public UUID getId() { return id; }
    public ItemStack getNextPageButton() { return nextPageButton; }
    public ItemStack getPreviousPageButton() { return previousPageButton; }
    public String getNextPageButtonName() { return nextPageButton.getItemMeta().getDisplayName(); }
    public String getPreviousPageButtonName() { return previousPageButton.getItemMeta().getDisplayName(); }

}
