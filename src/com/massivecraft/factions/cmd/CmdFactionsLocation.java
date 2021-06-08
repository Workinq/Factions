package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.Location;

public class CmdFactionsLocation extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsLocation()
    {
        // Requirements
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Verify
        if ( ! MPerm.getPermLocation().has(msender, msenderFaction, true)) return;

        Location at = me.getLocation();
        msenderFaction.msg("%s <i>has pinged their location at x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <n>(<h>%s<n>)", msender.describeTo(msenderFaction, true), at.getBlockX(), at.getBlockY(), at.getBlockZ(), at.getWorld().getName());
    }

}
