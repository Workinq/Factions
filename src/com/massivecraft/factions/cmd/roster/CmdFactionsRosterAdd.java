package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.cmd.type.TypeRel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsRosterAdd extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterAdd()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
        this.addParameter(TypeRel.get(), "role", "recruit");
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        MPlayer mplayer = this.readArg();
        Rel rel = this.readArg(Rel.RECRUIT);
        Faction faction = this.readArg(msenderFaction);

        // Verify
        if ( ! MPerm.getPermRoster().has(msender, faction, true)) return;

        if (faction.isInRoster(mplayer))
        {
            msg("%s <i>is already in the faction roster.", mplayer.describeTo(msender));
            return;
        }

        // Size Check
        if (faction.getRoster().size() + 1 > MConf.get().rosterMemberLimit)
        {
            msg("<b>You can't add anymore people to the roster as you've reached the limit of %,d", MConf.get().rosterMemberLimit);
            return;
        }

        // Apply
        faction.addToRoster(mplayer, rel);

        // Inform
        msg("%s <i>added %s <i>to the faction roster with the role <h>%s<i>.", msender.describeTo(msender, true), mplayer.describeTo(msender), rel.getName());
        faction.msg("%s <i>was added to the faction roster with the role <h>%s<i>.", mplayer.describeTo(faction, true), rel.getName());
    }

}
