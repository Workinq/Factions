package com.massivecraft.factions.action.mission;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.AbstractMission;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ActionMissionView extends ChestActionAbstract
{
    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        player.openInventory(this.getMissionsGui());
        return true;
    }

    private Inventory getMissionsGui()
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, 27, Txt.parse("<gray>All Missions"));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);
        NumberFormat rewardFormat = NumberFormat.getInstance();
        int slot = 10;

        // Loop
        for (AbstractMission mission : MMission.get().getMissions())
        {
            List<String> lore = new ArrayList<>();
            lore.add(Txt.parse("<n>Challenge: <k>%s", mission.getDescription()));
            lore.add("");
            lore.add(Txt.parse("<n>Reward: <k>%s", rewardFormat.format(mission.getReward()) + " Credits"));
            chestGui.getInventory().setItem(slot, new ItemBuilder(mission.getItemMaterial()).name(Txt.parse("<k>" + mission.getItemName())).data(mission.getItemData()).setLore(lore).flag(ItemFlag.HIDE_ATTRIBUTES));
            slot++;
        }

        // FIll Inventory
        InventoryUtil.fillInventory(chestGui.getInventory());

        // Return
        return chestGui.getInventory();
    }

}
