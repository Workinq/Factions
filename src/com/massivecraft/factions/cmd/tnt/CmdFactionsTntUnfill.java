package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.*;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CmdFactionsTntUnfill extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsTntUnfill()
    {
        // Aliases
        this.addAliases("unfill");

        // Desc
        this.setDescPermission("factions.tnt.unfill");

        // Parameters
        this.addParameter(TypeInteger.get(), "radius", "20");

        // Requirements
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
        int radius = this.readArg(MConf.get().maximumFillRadius);
        if (radius < 0) radius *= -1;

        if (radius > MConf.get().maximumFillRadius)
        {
            msg("<b>The maximum tnt unfill radius is %,d", MConf.get().maximumFillRadius);
            return;
        }

        if ( ! MPerm.getPermTnt().has(msender, msenderFaction, true)) return;

        Block block = me.getWorld().getBlockAt(me.getLocation());
        Set<Dispenser> dispensers = new HashSet<>();

        int maximumTnt;
        int tntToMove;
        int dispensersChanged;
        for (maximumTnt = -radius; maximumTnt <= radius; ++maximumTnt)
        {
            for (tntToMove = -radius; tntToMove <= radius; ++tntToMove)
            {
                for (dispensersChanged = -radius; dispensersChanged <= radius; ++dispensersChanged)
                {
                    Block relative = block.getRelative(maximumTnt, tntToMove, dispensersChanged);

                    if (relative.getType() != Material.DISPENSER) continue;

                    dispensers.add((Dispenser) relative.getState());
                }
            }
        }

        if (dispensers.isEmpty())
        {
            msg("<b>There are no dispensers nearby to be unfilled.");
            return;
        }

        maximumTnt = Integer.parseInt(MUpgrade.get().getUpgradeByName(MUpgrade.get().tntUpgrade.getUpgradeName()).getCurrentDescription()[msenderFaction.getLevel(MUpgrade.get().tntUpgrade.getUpgradeName()) - 1].split(" ")[0].replaceAll(",", ""));
        tntToMove = 0;
        dispensersChanged = 0;

        Iterator<Dispenser> iterator = dispensers.iterator();

        // Loop
        unfillLoop: while (true)
        {
            Dispenser dispenser;
            Faction faction;
            do
            {
                if (!iterator.hasNext()) break unfillLoop;

                dispenser = iterator.next();
                if (tntToMove == maximumTnt) break unfillLoop;

                faction = BoardColl.get().getFactionAt(PS.valueOf(dispenser.getLocation()));
            } while (msenderFaction != faction && !faction.isNone());

            ItemStack[] contents = dispenser.getInventory().getContents();
            int has = 0;

            for (ItemStack item : contents)
            {
                if (item != null && item.getType() == Material.TNT && item.getAmount() > 0)
                {
                    has += item.getAmount();
                }
            }

            if (tntToMove + has + msenderFaction.getTnt() > maximumTnt)
            {
                has -= tntToMove + has + msenderFaction.getTnt() - maximumTnt;
            }

            dispenser.getInventory().removeItem(new ItemStack(Material.TNT, has));
            if (has > 0) dispensersChanged++;

            tntToMove += has;
        }

        // Apply
        msenderFaction.setTnt(msenderFaction.getTnt() + tntToMove);

        // Inform
        if (tntToMove <= 0)
        {
            msender.msg("<b>No dispensers nearby could be unfilled.");
            return;
        }
        msg("%s<i> unfilled <h>%,d <i>dispensers totalling to <h>%,d <i>tnt.", msender.describeTo(msender, true), dispensersChanged, tntToMove);
    }

}
