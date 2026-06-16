package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.entity.upgrade.AbstractUpgrade;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.StandardGui;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class UpgradeMenu extends StandardGui
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private static final int[] UPGRADE_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};

    private final MPlayer mplayer;
    private final Faction faction;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public UpgradeMenu(Player player, Faction faction)
    {
        super(player, 4, "<gray>Faction Upgrades");
        this.mplayer = MPlayer.get(player);
        this.faction = faction;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        fillBorder();

        NumberFormat priceFormat = NumberFormat.getInstance();
        priceFormat.setGroupingUsed(true);

        int index = 0;
        for (AbstractUpgrade upgrade : MUpgrade.get().upgrades)
        {
            if (index >= UPGRADE_SLOTS.length) break;

            int upgradeLevel = faction.getLevel(upgrade.getUpgradeName());
            int upgradePrice;
            List<String> lore = new ArrayList<>();

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

            ItemStack icon = new ItemBuilder(upgrade.getUpgradeItem())
                .amount(1)
                .name(Txt.parse("<k>" + upgrade.getUpgradeName()))
                .withLore(lore);

            int price = upgradePrice;
            int level = upgradeLevel;
            button(UPGRADE_SLOTS[index], icon, ctx -> purchase(upgrade, price, level));
            index++;
        }

        button(31, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
    }

    // -------------------------------------------- //
    // PURCHASE
    // -------------------------------------------- //

    private void purchase(AbstractUpgrade upgrade, int price, int level)
    {
        // Verify - Level
        if (price == 0)
        {
            mplayer.msg("<b>You already have the maximum level in this upgrade.");
            return;
        }

        // MPerm
        if ( ! MPerm.getPermUpgrade().has(mplayer, faction, true)) return;

        // Verify - Balance
        if (faction.getCredits() < price)
        {
            mplayer.msg("<b>You do not have enough credits to complete this purchase.");
            return;
        }

        // Args
        Mson mson = Mson.mson(Txt.parse("%s<i> has upgraded <a>%s <i>to level %s<i>.", mplayer.describeTo(faction, true), ChatColor.stripColor(upgrade.getUpgradeName()), level + 1));

        // Increase
        MUpgrade.get().increaseUpgrade(faction, upgrade);

        // Apply
        faction.takeCredits(price);

        // Inform
        faction.sendMessage(mson);

        // Refresh
        clearContent();
        build();
        render();
    }
}
