package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.pager.Msonifier;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.util.Txt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdFactionsWarpList extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsWarpList()
    {
        // Aliases
        this.addAliases("warps");

        // Parameters
        this.addParameter(Parameter.getPage());
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        int page = this.readArg();
        Faction faction = this.readArg(msenderFaction);

        if (faction != msenderFaction && ! Perm.WARPLIST_OTHER.has(sender, true)) return;

        if ( ! MPerm.getPermWarp().has(msender, faction, true) ) return;

        List<Mson> warps = new ArrayList<>();
        for (String warp : faction.getWarpNames())
        {
            Mson mson = mson(Txt.parse("<h>%s %s <n>- click to teleport", warp, Txt.parse((faction.warpHasPassword(warp) ? "<n>(<g>protected<n>)" : "<n>(<b>unprotected<n>)"))));
            mson = mson.tooltip("<a>Click to warp to %s", warp);
            warps.add(mson);
        }

        // Reverse
        Collections.reverse(warps);

        // Pager create
        final Pager<Mson> pager = new Pager<>(this, "Faction Warps", page, warps, new Msonifier<Mson>()
        {
            @Override
            public Mson toMson(Mson item, int index)
            {
                return warps.get(index);
            }
        });

        // Pager message
        pager.message();
    }

}
