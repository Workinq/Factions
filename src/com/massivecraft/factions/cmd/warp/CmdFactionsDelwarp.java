package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeWarp;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.object.FactionWarp;
import com.massivecraft.factions.event.EventFactionsWarpDelete;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsDelwarp extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDelwarp()
    {
        // Parameters
        this.addParameter(TypeWarp.get(), "warp");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        FactionWarp warp = this.readArgAt(0);

        // Verify
        if ( ! MPerm.getPermDelwarp().has(msender, msenderFaction, true)) return;

        // Event
        EventFactionsWarpDelete event = new EventFactionsWarpDelete(sender, msenderFaction, warp);
        event.run();
        if (event.isCancelled()) return;

        // Apply
        msenderFaction.deleteWarp(event.getNewWarp());

        // Inform
        msenderFaction.msg("%s<i> deleted the faction warp called <a>%s<i>.", msender.describeTo(msenderFaction, true), warp.getName());
    }

}
