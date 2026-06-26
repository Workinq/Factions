package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsRosterKick extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterKick()
    {
        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
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
        Faction faction = this.readArg(msenderFaction);

        // Verify
        if ( ! MPerm.getPermRoster().has(msender, faction, true)) return;

        if ( ! faction.isInRoster(mplayer)) throw new MassiveException().setMsg("%s <i>is not in the faction roster.", mplayer.describeTo(msender));

        // Apply
        faction.removeFromRoster(mplayer);

        // Inform
        msg("%s <i>removed %s <i>from the faction roster.", msender.describeTo(msender, true), mplayer.describeTo(msender));
    }

}
