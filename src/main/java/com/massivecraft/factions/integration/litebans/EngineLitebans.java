package com.massivecraft.factions.integration.litebans;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.Engine;
import litebans.api.Entry;
import litebans.api.Events;

public class EngineLitebans extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineLitebans i = new EngineLitebans ();
    public static EngineLitebans get() { return i; }
    public EngineLitebans() { this.registerLitebans(); }

    // -------------------------------------------- //
    // LISTENER
    // -------------------------------------------- //

    private void registerLitebans()
    {
        Events.get().register(new Events.Listener()
        {
            @Override
            public void entryAdded(Entry entry)
            {
                // ... and we remove player data when banned ...
                if (!MConf.get().removePlayerWhenBanned) return;

                // ... and we remove player data when PERMANENTLY banned ...
                if (MConf.get().onlyRemovePlayersWhenPermanentlyBanned && !entry.isPermanent()) return;

                // If a player (not an IP-only ban) was banned from the server ...
                String uuid = entry.getUuid();
                if (uuid == null) return;

                // ... get rid of their stored info.
                MPlayer mplayer = MPlayerColl.get().get(uuid, false);
                if (mplayer == null) return;

                if (mplayer.getRole() == Rel.LEADER)
                {
                    mplayer.getFaction().promoteNewLeader();
                }

                mplayer.leave();
                mplayer.detach();
            }
        });
    }

}
