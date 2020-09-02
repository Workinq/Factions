package com.massivecraft.factions.action;

import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionRosterKick extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final MPlayer mplayer;
    private final MPlayer mtarget;
    private final Faction faction;
    private final int rosterKicks;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionRosterKick(MPlayer mplayer, MPlayer mtarget, Faction faction, int rosterKicks)
    {
        this.mplayer = mplayer;
        this.mtarget = mtarget;
        this.faction = faction;
        this.rosterKicks = rosterKicks;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // MPerm
        if ( ! MPerm.getPermRoster().has(mplayer, faction, true)) return false;

        // Verify
        if (rosterKicks <= 0)
        {
            mplayer.msg("%s <i>has reached the maximum number of roster kicks in 24 hours.", faction.describeTo(mplayer, true));
            return false;
        }

        if ( ! faction.isInRoster(mtarget) )
        {
            mplayer.msg("%s <i>is not in the faction roster.", mtarget.describeTo(mplayer));
            return false;
        }

        if (mtarget.getRole().isMoreThan(mplayer.getRole()) && ! mplayer.isOverriding())
        {
            mplayer.msg("<b>You can't kick people of higher rank than yourself.");
            return false;
        }

        if (mtarget.getRole() == mplayer.getRole() && ! mplayer.isOverriding())
        {
            mplayer.msg("<b>You can't kick people of the same rank as yourself.");
            return false;
        }

        // Apply
        faction.addRosterKick();
        faction.removeFromRoster(mtarget);
        CmdFactions.get().cmdFactionsKick.execute(mplayer.getSender(), MUtil.list(mtarget.getName()));

        // Inform
        mplayer.msg("%s <i>removed %s <i>from the faction roster.", mplayer.describeTo(mplayer, true), mtarget.describeTo(mplayer));
        faction.msg("%s <i>removed %s <i>from the faction roster.", mplayer.describeTo(faction, true), mtarget.describeTo(faction));

        // Open
        player.openInventory(CmdFactions.get().cmdFactionsRoster.cmdFactionsRosterView.getRosterGui(player, faction));

        // Return
        return true;
    }

}
