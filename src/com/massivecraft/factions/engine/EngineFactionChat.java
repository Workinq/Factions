package com.massivecraft.factions.engine;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EngineFactionChat extends Engine
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static EngineFactionChat i = new EngineFactionChat();
    public static EngineFactionChat get() { return i; }

    // -------------------------------------------- //
    // FACTION CHAT
    // -------------------------------------------- //

    @EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
    public void onPlayerEarlyChat(AsyncPlayerChatEvent event)
    {
        // Args
        String msg = event.getMessage();
        MPlayer mplayer = MPlayer.get(event.getPlayer());
        Faction faction = mplayer.getFaction();
        Chat chat = mplayer.getChat();

        // Verify
        if (faction.isNone() && chat != Chat.PUBLIC) mplayer.setChat(Chat.PUBLIC);

        // Inform
        String message;
        switch (chat)
        {
            case FACTION:
                if (faction.isMuted(mplayer.getId()))
                {
                    mplayer.msg("<b>You are currently muted in the faction");
                    event.setCancelled(true);
                    return;
                }
                message = String.format(MConf.get().chatFormat, mplayer.describeTo(faction), msg);
                for (Player target : Bukkit.getOnlinePlayers())
                {
                    MPlayer mtarget = MPlayer.get(target);
                    if (mtarget.isSpying())
                    {
                        mtarget.msg(MConf.get().spyChatFormat, mplayer.describeTo(mtarget, true), Chat.FACTION.getName(), msg);
                    }
                    if (mtarget.getFaction() != faction) continue;
                    if (mtarget.isIgnoring(mplayer)) continue; // Sad times :(
                    mtarget.msg(message);
                }
                Factions.get().log(" [Faction Chat] " + mplayer.getName() + " sent a message: " + msg);
                event.setCancelled(true);
                break;
            case ALLY:
                message = String.format(MConf.get().chatFormat, Rel.ALLY.getColor() + mplayer.getNameAndFactionName(), msg);
                for (Player target : Bukkit.getOnlinePlayers())
                {
                    MPlayer mtarget = MPlayer.get(target);
                    if (mtarget.isSpying())
                    {
                        mtarget.msg(MConf.get().spyChatFormat, mplayer.describeTo(mtarget, true), Chat.ALLY.getName(), msg);
                    }
                    if (mtarget.getFaction() != faction && mtarget.getFaction().getRelationTo(faction) != Rel.ALLY) continue;
                    mtarget.msg(message);
                }
                Factions.get().log(" [Ally Chat] " + mplayer.getName() + " sent a message: " + msg);
                event.setCancelled(true);
                break;
            case TRUCE:
                message = String.format(MConf.get().chatFormat, Rel.TRUCE.getColor() + mplayer.getNameAndFactionName(), msg);
                for (Player target : Bukkit.getOnlinePlayers())
                {
                    MPlayer mtarget = MPlayer.get(target);
                    if (mtarget.isSpying())
                    {
                        mtarget.msg(MConf.get().spyChatFormat, mplayer.describeTo(mtarget, true), Chat.TRUCE.getName(), msg);
                    }
                    if (mtarget.getFaction() != faction && mtarget.getFaction().getRelationTo(faction) != Rel.TRUCE) continue;
                    mtarget.msg(message);
                }
                Factions.get().log(" [Truce Chat] " + mplayer.getName() + " sent a message: " + msg);
                event.setCancelled(true);
                break;
            case PUBLIC:
            default:
                break;
        }
    }

}
