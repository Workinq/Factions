package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
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
        // Aliases
        this.setAliases("delwarp");

        // Desc
        this.setDescPermission("factions.delwarp");

        // Parameters
        this.addParameter(TypeString.get(), "warp");
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        String warp = this.readArg();
        Faction faction = this.readArg(msenderFaction);

        // Verify
        if ( ! MPerm.getPermDelwarp().has(msender, faction, true) ) return;

        if ( ! faction.warpExists(warp) )
        {
            throw new MassiveException().setMsg("<b>There is no faction warp called <h>%s<b>.", warp);
        }

        // Event
        EventFactionsWarpDelete event = new EventFactionsWarpDelete(sender, faction, warp);
        event.run();
        if (event.isCancelled()) return;
        warp = event.getNewWarp();

        // Apply
        faction.deleteWarp(warp);

        // Inform
        faction.msg("%s<i> deleted the faction warp called <h>%s<i>.", msender.describeTo(faction, true), warp);
    }

}
