package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeWarp;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.object.FactionWarp;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mixin.MixinTeleport;
import com.massivecraft.massivecore.mixin.TeleporterException;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.teleport.DestinationSimple;

public class CmdFactionsWarp extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsWarp()
    {
        // Parameters
        this.addParameter(TypeWarp.get(), "warp");
        this.addParameter(null, TypeString.get(), "password");

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
        FactionWarp warp = this.readArgAt(0);
        String password = this.readArgAt(1, null);

        // Verify
        if ( ! MPerm.getPermWarp().has(msender, msenderFaction, true)) return;

        if ( ! msenderFaction.verifyWarpIsValid(warp)) return;

        if (warp.hasPassword() && !warp.getPassword().equals(password))
        {
            msender.msg("<b>The password you provided is incorrect.");
            return;
        }

        // Teleport
        PS location = warp.getLocation();
        DestinationSimple destination = new DestinationSimple(location, warp.getName().toLowerCase());
        try
        {
            MixinTeleport.get().teleport(me, destination, MConf.get().warpWarmup);
        }
        catch (TeleporterException e)
        {
            msender.msg("<b>%s", e.getMessage());
        }
    }

}
