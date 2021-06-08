package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.event.EventFactionsWarpCreate;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsSetwarp extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSetwarp()
    {
        // Parameters
        this.addParameter(TypeString.get(), "name");
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
        String warp = this.readArgAt(0);
        String password = this.readArgAt(1, null);
        PS location = PS.valueOf(me);

        // Verify
        if ( ! MPerm.getPermSetwarp().has(msender, msenderFaction, true)) return;

        if (!msender.isOverriding() && !msenderFaction.isValidWarp(location))
        {
            throw new MassiveException().setMsg("<b>You can only be set faction warps in your own claimed territory.");
        }

        if (msenderFaction.getWarpLocations().size() >= MConf.get().amountOfWarps + msenderFaction.getLevel(MUpgrade.get().warpUpgrade.getUpgradeName()) * 2)
        {
            throw new MassiveException().setMsg("<b>You can not set any more faction warps as you've reached the limit.");
        }

        if (msenderFaction.warpExists(warp))
        {
            throw new MassiveException().setMsg("<b>A faction warp already exists with this name.");
        }

        // Event
        EventFactionsWarpCreate event = new EventFactionsWarpCreate(sender, msenderFaction, warp, password, location);
        event.run();
        if (event.isCancelled()) return;

        warp = event.getNewWarp().toLowerCase();
        password = event.getNewPassword();
        location = event.getNewLocation();

        // Apply
        msenderFaction.addWarp(warp, location, password);

        // Inform
        msenderFaction.msg("%s<i> created a new faction warp called <h>%s<i>.", msender.describeTo(msender, true), warp);
    }

}
