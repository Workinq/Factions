package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CmdFactionsTntFill extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsTntFill()
    {
        this.addParameter(TypeInteger.get(), "amount", "576");
        this.addParameter(TypeInteger.get(), "radius", "20");
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if (msenderFaction.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) <= 0)
        {
            msender.msg("<b>You can't use /f tnt fill as you haven't unlocked the TNT upgrade.");
            return;
        }

        // Args
        int tntPer = this.readArg(576);
        if (tntPer < 0) tntPer *= -1;

        int radius = this.readArg(MConf.get().maximumFillRadius);
        if (radius < 0) radius *= -1;

        if (radius > MConf.get().maximumFillRadius)
        {
            msg("<b>The maximum tnt fill radius is %,d", MConf.get().maximumFillRadius);
            return;
        }

        if ( ! MPerm.getPermTnt().has(msender, msenderFaction, true)) return;

        int balance = msenderFaction.getTnt();
        if (tntPer > 576)
        {
            msg("<b>The maximum TNT per dispenser is <h>576<b>.");
            return;
        }

        if (balance <= 0)
        {
            msg("<b>Your faction doesn't have any tnt stored.");
            return;
        }

        Block block = me.getWorld().getBlockAt(me.getLocation());
        Set<Dispenser> dispensers = new HashSet<>();

        int tntUsed;
        int dispensersFilled;
        for (tntUsed = -radius; tntUsed <= radius; tntUsed++)
        {
            for (dispensersFilled = -radius; dispensersFilled <= radius; dispensersFilled++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    Block relative = block.getRelative(tntUsed, dispensersFilled, z);

                    if (relative.getType() != Material.DISPENSER) continue;

                    dispensers.add((Dispenser) relative.getState());
                }
            }
        }

        if (dispensers.isEmpty())
        {
            msg("<b>There are no dispensers nearby to be filled.");
            return;
        }

        tntUsed = 0;
        dispensersFilled = 0;
        Iterator<Dispenser> iterator = dispensers.iterator();

        fillLoop: while (true)
        {
            Dispenser dispenser;
            int tntInDispenser;
            int otherInDispenser;
            while (true)
            {
                if (!iterator.hasNext())
                {
                    break fillLoop;
                }
                dispenser = iterator.next();
                tntInDispenser = 0;
                otherInDispenser = 0;
                if (!dispenser.getInventory().contains(Material.TNT))
                {
                    break;
                }
                tntInDispenser = InventoryUtil.getTntIn(dispenser.getInventory());
                if (tntInDispenser < tntPer)
                {
                    otherInDispenser = InventoryUtil.getOtherIn(dispenser.getInventory());
                    break;
                }
            }

            // Args
            int inDispenser = tntInDispenser + otherInDispenser;
            int tntToAdd = tntPer - tntInDispenser;

            // Fill
            if (inDispenser < 576)
            {
                int spaceLeft = 576 - inDispenser;
                if (spaceLeft > tntToAdd)
                {
                    if (tntUsed + tntToAdd > balance) break;

                    dispenser.getInventory().addItem(new ItemStack(Material.TNT, tntToAdd));
                    tntUsed += tntToAdd;
                    dispensersFilled++;
                }
                else
                {
                    if (tntUsed + tntToAdd > balance) break;

                    dispenser.getInventory().addItem(new ItemStack(Material.TNT, spaceLeft));
                    tntUsed += spaceLeft;
                    dispensersFilled++;
                }
            }
        }

        // Inform
        msenderFaction.setTnt(msenderFaction.getTnt() - tntUsed);
        msg("%s<i> filled <h>%,d <i>dispensers with <h>%,d <i>tnt in each, totalling to <h>%,d <i>tnt.", msender.describeTo(msender, true), dispensersFilled, tntPer, tntUsed);
    }

}
