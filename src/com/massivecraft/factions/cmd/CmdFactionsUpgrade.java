package com.massivecraft.factions.cmd;

import com.massivecraft.factions.action.ActionUpgrade;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.entity.upgrade.AbstractUpgrade;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CmdFactionsUpgrade extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsUpgrade()
    {
        // Aliases
        this.setAliases("upgrade");

        // Desc
        this.setDescPermission("factions.upgrade");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg(msenderFaction);

        if ( ! MPerm.getPermUpgrade().has(msender, faction, true)) return;

        me.openInventory(this.getFactionUpgrades(faction));
    }

    private Inventory getFactionUpgrades(Faction faction)
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, 9, Txt.parse("<gray>Faction Upgrades"));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);
        NumberFormat priceFormat = NumberFormat.getInstance(); priceFormat.setGroupingUsed(true);
        int slot = 0;

        // Loop
        for (AbstractUpgrade upgrade : MUpgrade.get().upgrades)
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
            }
            else
            {
                upgradePrice = upgrade.getCost()[upgradeLevel];
                if (upgradeLevel == 0)
                {
                    lore.add(Txt.parse("<n>Current Level: <k>0"));
                    lore.add(Txt.parse("<n>Maximum Level: <k>%s", upgrade.getMaxLevel()));
                    lore.add("");
                    lore.add(Txt.parse("<n>Next Upgrade: <k>" + upgrade.getNextDescription()[upgradeLevel]));
                    lore.add("");
                }
                else
                {
                    lore.add(Txt.parse("<n>Current Level: <k>%s", upgradeLevel));
                    lore.add(Txt.parse("<n>Maximum Level: <k>%s", upgrade.getMaxLevel()));
                    lore.add("");
                    if (upgrade.getCurrentDescription()[upgradeLevel - 1] != null)
                    {
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

            // Assign
            chestGui.getInventory().setItem(slot, new ItemBuilder(upgrade.getUpgradeItem()).amount(1).name(Txt.parse("<k>" + upgrade.getUpgradeName())).setLore(lore));
            chestGui.setAction(slot, new ActionUpgrade(msender, faction, upgradePrice, upgrade.getUpgradeName(), upgradeLevel));
            slot++;
        }

        // Return
        return chestGui.getInventory();
    }

}
