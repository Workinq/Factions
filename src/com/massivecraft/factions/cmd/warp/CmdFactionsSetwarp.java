package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.entity.object.FactionWarp;
import com.massivecraft.factions.event.EventFactionsWarpCreate;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;

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
        String name = this.readArgAt(0);
        String password = this.readArgAt(1, null);
        PS newWarp = PS.valueOf(me);

        // Verify
        if ( ! MPerm.getPermSetwarp().has(msender, msenderFaction, true)) return;

        if (!msender.isOverriding() && !msenderFaction.isValidWarp(newWarp))
        {
            msender.msg("<b>You can only be set faction warps in your own claimed territory.");
            return;
        }

        if (msenderFaction.getWarps().size() >= MConf.get().amountOfWarps + msenderFaction.getLevel(MUpgrade.get().warpUpgrade.getUpgradeName()) * 2)
        {
            msg("<b>You can not set any more faction warps as you've reached the limit.");
            return;
        }

        if (msenderFaction.warpExists(name))
        {
            msg("<b>A faction warp already exists with this name.");
            return;
        }

        // Event
        EventFactionsWarpCreate event = new EventFactionsWarpCreate(sender, msenderFaction, new FactionWarp(name, me.getUniqueId(), newWarp, password));
        event.run();
        if (event.isCancelled()) return;

        // Apply
        msenderFaction.addWarp(event.getNewWarp());

        // Inform
        msenderFaction.msg("%s<i> created a new faction warp called <a>%s<i>.", msender.describeTo(msender, true), name);
    }

}
