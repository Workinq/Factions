package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.Location;

public class CmdFactionsCoords extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsCoords()
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
        Location at = me.getLocation();
        msenderFaction.msg("%s <i>shared their coordinates: x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <n>(<h>%s<n>)", msender.describeTo(msenderFaction, true), at.getBlockX(), at.getBlockY(), at.getBlockZ(), at.getWorld().getName());
    }

}
