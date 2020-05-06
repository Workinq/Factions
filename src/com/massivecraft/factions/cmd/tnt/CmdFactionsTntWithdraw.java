package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CmdFactionsTntWithdraw extends FactionsCommand
{

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsTntWithdraw()
    {
        // Parameters
        this.addParameter(TypeInteger.get(), "amount");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MPerm.getPermTnt().has(msender, msenderFaction, true)) return;

        int tntInInventory = 0, otherInInventory = 0;

        if (me.getInventory().contains(Material.TNT))
        {
            tntInInventory = InventoryUtil.getTntIn(me.getInventory());
            otherInInventory = InventoryUtil.getOtherIn(me.getInventory());
        }

        int spaceLeft = 2304 - (tntInInventory + otherInInventory);
        int amount = this.readArg();


        int factionTnt = msenderFaction.getTnt();

        if (factionTnt <= 0)
        {
            msender.msg("<b>Your faction doesn't have any tnt to withdraw.");
            return;
        }

        if (amount > factionTnt)
        {
            msender.msg("<b>Your faction doesn't have enough to withdraw %,d tnt.", amount);
            return;
        }

        if (amount > spaceLeft)
        {
            msender.msg("<b>You do not have enough room in your inventory to withdraw %,d tnt.", amount);
            return;
        }

        msenderFaction.setTnt(factionTnt - amount);
        msenderFaction.msg("%s<i> withdrew <a>%,d <i>tnt from %s<i>.", msender.describeTo(msender, true), amount, msenderFaction.describeTo(msender));
        me.getInventory().addItem(new ItemStack(Material.TNT, amount));
        me.updateInventory();

        // Log
        if (MConf.get().logTntTransactions)
        {
            Factions.get().log(ChatColor.stripColor(Txt.parse("%s withdrew %s TNT from the faction bank: %s", msender.getName(), String.format("%,d", amount), msenderFaction.describeTo(null))));
        }
    }

}
