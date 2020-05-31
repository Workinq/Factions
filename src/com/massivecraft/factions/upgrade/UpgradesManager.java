package com.massivecraft.factions.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.upgrade.upgrades.*;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class UpgradesManager
{

    private static final UpgradesManager i = new UpgradesManager();
    public static UpgradesManager get() { return i; }

    private final List<Upgrade> upgrades = new ArrayList<>();

    public Inventory getFactionUpgrades(Faction faction)
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, 9, Txt.parse("<gray>Faction Upgrades"));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        NumberFormat priceFormat = NumberFormat.getInstance();
        int slot = 0;
        // Arg Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);
        priceFormat.setGroupingUsed(true);
        // Loop
        for (Upgrade upgrade : upgrades)
        {
            List<String> lore = new ArrayList<>();
            int upgradeLevel = faction.getLevel(upgrade.getUpgradeName());
            int upgradePrice;
            if (upgrade.getCost().length <= upgradeLevel)
            {
                upgradePrice = 0;
                lore.add(Txt.parse("<n>Current Level: <k>" + upgrade.getMaxLevel()));
                lore.add(Txt.parse("<n>Maximum Level: <k>" + upgrade.getMaxLevel()));
                lore.add("");
                lore.add(Txt.parse("<n>Current Benefits: <k>" + upgrade.getCurrentDescription()[upgradeLevel - 1]));
            } else {
                upgradePrice = upgrade.getCost()[upgradeLevel];
                if (upgradeLevel == 0)
                {
                    lore.add(Txt.parse("<n>Current Level: <k>0"));
                    lore.add(Txt.parse("<n>Maximum Level: <k>%s", upgrade.getMaxLevel()));
                    lore.add("");
                    lore.add(Txt.parse("<n>Next Upgrade: <k>" + upgrade.getNextDescription()[upgradeLevel]));
                    lore.add("");
                } else {
                    lore.add(Txt.parse("<n>Current Level: <k>%s", upgradeLevel));
                    lore.add(Txt.parse("<n>Maximum Level: <k>%s", upgrade.getMaxLevel()));
                    lore.add("");
                    if (upgrade.getCurrentDescription()[upgradeLevel - 1] != null) {
                        lore.add(Txt.parse("<n>Current Benefits: <k>" + upgrade.getCurrentDescription()[upgradeLevel - 1]));
                    }
                    if (upgrade.getNextDescription()[upgradeLevel] != null)
                    {
                        lore.add(Txt.parse("<n>Next Upgrade: <k>" + upgrade.getNextDescription()[upgradeLevel]));
                        lore.add("");
                    }
                }
                if (upgradeLevel < upgrade.getMaxLevel())
                {
                    lore.add(Txt.parse("<n>Cost: <k>%s Credits", priceFormat.format(upgradePrice)));
                }
            }
            chestGui.getInventory().setItem(slot, new ItemBuilder(upgrade.getUpgradeItem()).amount(1).name(Txt.parse("<k>" + upgrade.getUpgradeName())).setLore(lore));
            chestGui.setAction(slot, new com.massivecraft.factions.action.ActionUpgrade(faction, upgradePrice, upgrade.getUpgradeName(), upgradeLevel));
            slot++;
        }
        return chestGui.getInventory();
    }

    public List<Upgrade> getUpgrades()
    {
        return upgrades;
    }

    public void load()
    {
        upgrades.add(new SpawnerRateUpgrade());
        upgrades.add(new CropGrowthUpgrade());
        upgrades.add(new FactionChestUpgrade());
        upgrades.add(new TNTStorageUpgrade());
        upgrades.add(new WarpUpgrade());
        upgrades.add(new PowerboostUpgrade());
        upgrades.add(new SandAltUpgrade());
    }

    public Upgrade getUpgradeByName(String string)
    {
        for (Upgrade upgrade : upgrades)
        {
            if (upgrade.getUpgradeName().equalsIgnoreCase(string))
            {
                return upgrade;
            }
        }
        return null;
    }

    public void increaseUpgrade(Faction faction, Upgrade upgrade)
    {
        if (faction.getLevel(upgrade.getUpgradeName()) < upgrade.getMaxLevel())
        {
            faction.increaseLevel(upgrade.getUpgradeName());
            upgrade.onUpgrade(faction);
        }
    }

}