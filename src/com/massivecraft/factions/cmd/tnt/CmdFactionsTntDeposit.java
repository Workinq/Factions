package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CmdFactionsTntDeposit extends FactionsCommand
{

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsTntDeposit()
    {
        // Parameters
        this.addParameter(TypeInteger.get(), "amount");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MPerm.getPermTnt().has(msender, msenderFaction, true)) return;

        Inventory inventory = me.getInventory();
        if ( ! inventory.contains(Material.TNT))
        {
            msg("<b>You don't have any TNT in your inventory to deposit.");
            return;
        }

        int amount = this.readArg();

        if (msenderFaction.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) == 0)
        {
            msg("<b>You can't store any TNT as you haven't upgraded your faction bank yet.");
            return;
        }

        int maximumTnt = Integer.parseInt(MUpgrade.get().getUpgradeByName(MUpgrade.get().tntUpgrade.getUpgradeName()).getCurrentDescription()[msenderFaction.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) - 1].split(" ")[0].replaceAll(",", ""));
        int tntInInventory = InventoryUtil.getTntIn(me.getInventory());
        int factionTnt = msenderFaction.getTnt();

        if (factionTnt + amount > maximumTnt)
        {
            amount = amount - (amount + factionTnt - maximumTnt);
        }

        if (tntInInventory >= amount)
        {
            msg("%s<i> deposited <a>%,d <i>tnt to %s<i>.", msender.describeTo(msender, true), amount, msenderFaction.describeTo(msender));
            inventory.removeItem(new ItemStack(Material.TNT, amount));
            msenderFaction.setTnt(factionTnt + amount);
            me.updateInventory();

            // Log
            if (MConf.get().logTntTransactions)
            {
                Factions.get().log(ChatColor.stripColor(Txt.parse("%s deposited %s TNT in the faction bank: %s", msender.getName(), String.format("%,d", amount), msenderFaction.describeTo(null))));
            }
        }
        else
        {
            msg("<b>You do not have %,d tnt in your inventory to deposit.", amount);
        }
    }

}
