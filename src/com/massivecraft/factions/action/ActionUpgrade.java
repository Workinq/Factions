package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionUpgrade extends ChestActionAbstract
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    private final MPlayer mplayer;
    private final Faction faction;
    private final Integer price;
    private final String upgradeName;
    private final Integer level;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionUpgrade(MPlayer mplayer, Faction faction, Integer price, String upgradeName, Integer level)
    {
        this.mplayer = mplayer;
        this.faction = faction;
        this.price = price;
        this.upgradeName = upgradeName;
        this.level = level;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Verify - Level
        if (price == 0)
        {
            mplayer.msg("<b>You already have the maximum level in this upgrade.");
            return true;
        }

        // MPerm
        if ( ! MPerm.getPermUpgrade().has(mplayer, faction, true) ) return true;

        // Verify - Balance
        if (faction.getCredits() < price)
        {
            mplayer.msg("<b>You do not have enough credits to complete this purchase.");
            return true;
        }

        // Args
        Mson mson = Mson.mson(Txt.parse("%s<i> has upgraded <a>%s <i>to level %s<i>.", mplayer.describeTo(faction, true), ChatColor.stripColor(upgradeName), level + 1));

        // Increase
        MUpgrade.get().increaseUpgrade(faction, MUpgrade.get().getUpgradeByName(upgradeName));

        // Apply
        faction.takeCredits(price);

        // Inform
        faction.sendMessage(mson);

        return true;
    }

}
