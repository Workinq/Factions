package com.massivecraft.factions.cmd.warp;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.object.FactionWarp;
import com.massivecraft.factions.util.TimeUtil;
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

        final List<Mson> warps = new ArrayList<>();
        for (FactionWarp warp : faction.getWarps())
        {
            long timeRemaining = System.currentTimeMillis() - warp.getCreationMillis();
            String timeSince = Txt.parse("%s", TimeUtil.formatTime(timeRemaining, true));

            Mson mson = mson(Txt.parse("<h>%s <i>set by %s <a>%s ago", warp.getName() + Txt.parse((warp.hasPassword() ? " <n>(<g>protected<n>)" : "")), warp.getCreator().describeTo(msender), timeSince));
            mson = mson.command("/f warp " + warp.getName());
            mson = mson.tooltip(Txt.parse("<a>Click to warp to %s", warp.getName()));
            warps.add(mson);
        }

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
