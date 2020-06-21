package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsWarpDelete;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsDelwarp extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDelwarp()
    {
        // Parameters
        this.addParameter(TypeString.get(), "warp");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        String warp = this.readArg();

        // Verify
        if ( ! MPerm.getPermDelwarp().has(msender, msenderFaction, true) ) return;

        // Event
        EventFactionsWarpDelete event = new EventFactionsWarpDelete(sender, msenderFaction, warp);
        event.run();
        if (event.isCancelled()) return;
        warp = event.getNewWarp();

        // Apply
        msenderFaction.deleteWarp(warp);

        // Inform
        msenderFaction.msg("%s<i> deleted the faction warp called <h>%s<i>.", msender.describeTo(msenderFaction, true), warp);
    }

}
