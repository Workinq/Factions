package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
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
        this.addParameter(TypeString.get(), "warp");
        this.addParameter(null, TypeString.get(), "password");
        this.addParameter(TypeFaction.get(), "faction", "you");

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
        String warp = this.readArg();
        warp = warp.toLowerCase();

        String password = this.readArg(null);
        Faction faction = this.readArg(msenderFaction);

        // Verify
        if ( ! MPerm.getPermWarp().has(msender, faction, true) ) return;

        if ( ! faction.verifyWarpIsValid(warp) ) return;

        if ( faction.warpHasPassword(warp) && ! faction.getWarpPassword(warp).equals(password) )
        {
            throw new MassiveException().addMsg("<b>The password you provided is incorrect.");
        }

        // Teleport
        PS location = faction.getWarpLocation(warp);
        DestinationSimple destination = new DestinationSimple(location, warp.toLowerCase());
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
