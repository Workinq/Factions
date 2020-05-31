package com.massivecraft.factions.cmd.loot;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import org.bukkit.Bukkit;

public class CmdFactionsLootClaim extends FactionsCommand
{

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsLootClaim()
    {
        // Parameters
        this.addParameter(TypeInteger.get(), "amount", "1");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        int amount = this.readArg(1);

        // MPerm
        if ( ! MPerm.getPermLoot().has(msender, msenderFaction, true) ) return;

        // Verify - Loot rewards
        if (msenderFaction.getLootRewards() - amount < 0)
        {
            msg("<b>You don't have %,d loot rewards to claim.", msenderFaction.getLootRewards());
            return;
        }

        // Apply
        msenderFaction.setLootRewards(msenderFaction.getLootRewards() - amount);

        // Command
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MConf.get().lootRewardCommand.replace("%player%", me.getName()).replace("%amount%", String.valueOf(amount)));

        // Inform
        msender.msg("%s <g>claimed <i>%,d <g>loot rewards from %s <g>.", msender.describeTo(msender, true), amount, msenderFaction.describeTo(msender));
    }

}
