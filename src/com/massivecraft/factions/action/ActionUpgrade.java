package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.upgrade.UpgradesManager;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionUpgrade extends ChestActionAbstract
{

    private final Faction faction;
    private final Integer price;
    private final String upgradeName;
    private final Integer level;

    public ActionUpgrade(Faction faction, Integer price, String upgradeName, Integer level)
    {
        this.faction = faction;
        this.price = price;
        this.upgradeName = upgradeName;
        this.level = level;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        MPlayer mPlayer = MPlayer.get(player);
        if (price == 0)
        {
            mPlayer.msg("<b>You already have the maximum level in this upgrade.");
            return true;
        }
        if (!MPerm.getPermUpgrade().has(mPlayer, faction, true))
        {
            return true;
        }
        if (faction.getCredits() < price)
        {
            mPlayer.msg("<b>You do not have enough credits to complete this purchase.");
        }
        else
        {
            Mson mson = Mson.mson(Txt.parse("%s<i> has upgraded <a>%s <i>to level %s<i>.", mPlayer.describeTo(faction, true), ChatColor.stripColor(upgradeName), level + 1));
            UpgradesManager.get().increaseUpgrade(faction, UpgradesManager.get().getUpgradeByName(upgradeName));
            faction.takeCredits(price);
            faction.sendMessage(mson);
        }
        return true;
    }

}
