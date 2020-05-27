package com.massivecraft.factions.engine;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EngineLogin extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineLogin i = new EngineLogin();
    public static EngineLogin get() { return i; }

    // -------------------------------------------- //
    // LOGIN
    // -------------------------------------------- //

    @EventHandler
    public void join(PlayerJoinEvent event)
    {
        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if ( ! mplayer.hasFaction() ) return;

        for (MPlayer mtarget : mplayer.getFaction().getMPlayersWhereOnline(true))
        {
            // Verify - Same player
            if (mplayer == mtarget) continue;

            // Verify - Toggled logins
            if ( ! mtarget.getLogins() ) continue;

            // Inform
            mtarget.msg(MConf.get().loginFormat, mplayer.describeTo(mtarget, true));
        }
    }

    // -------------------------------------------- //
    // QUIT
    // -------------------------------------------- //

    @EventHandler
    public void quit(PlayerQuitEvent event)
    {
        MPlayer mplayer = MPlayer.get(event.getPlayer());
        if ( ! mplayer.hasFaction() ) return;

        for (MPlayer mtarget : mplayer.getFaction().getMPlayersWhereOnline(true))
        {
            // Verify - Same player
            if (mplayer == mtarget) continue;

            // Verify - Toggled logins
            if ( ! mtarget.getLogins() ) continue;

            // Inform
            mtarget.msg(MConf.get().logoffFormat, mplayer.describeTo(mtarget, true));
        }
    }

}
