package com.massivecraft.factions.engine;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
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
        MPlayer me = MPlayer.get(event.getPlayer());
        Chat chat = me.getChat();
        Faction myFaction = me.getFaction();

        // Verify
        if (myFaction.isNone() && chat != Chat.PUBLIC) me.setChat(Chat.PUBLIC);

        // Inform
        String message;
        switch (chat)
        {
            case FACTION:
                if (myFaction.isMuted(me.getId()))
                {
                    me.msg("<b>You are currently muted in the faction");
                    event.setCancelled(true);
                    return;
                }
                message = String.format(MConf.get().chatFormat, me.describeTo(myFaction), msg);
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    MPlayer mplayer = MPlayer.get(player);
                    if (mplayer.isSpying())
                    {
                        mplayer.msg(MConf.get().spyChatFormat, me.describeTo(mplayer, true), Chat.FACTION.getName(), msg);
                    }
                    if (mplayer.getFaction() != myFaction) continue;
                    if (mplayer.isIgnoring(me)) continue; // Sad times :(
                    mplayer.msg(message);
                }
                Factions.get().log(" [Faction Chat] " + me.getName() + " sent a message: " + msg);
                event.setCancelled(true);
                break;
            case ALLY:
                message = String.format(MConf.get().chatFormat, Rel.ALLY.getColor() + me.getNameAndFactionName(), msg);
                for (MPlayer mPlayer : MPlayerColl.get().getAllOnline())
                {
                    if (mPlayer.isSpying())
                    {
                        mPlayer.msg(MConf.get().spyChatFormat, me.describeTo(mPlayer, true), Chat.ALLY.getName(), msg);
                    }
                    if (mPlayer.getFaction() != myFaction && mPlayer.getFaction().getRelationTo(myFaction) != Rel.ALLY) continue;
                    mPlayer.msg(message);
                }
                Factions.get().log(" [Ally Chat] " + me.getName() + " sent a message: " + msg);
                event.setCancelled(true);
                break;
            case TRUCE:
                if (myFaction.isMuted(me.getId()))
                {
                    me.msg("You are currently muted in the faction");
                    event.setCancelled(true);
                    return;
                }
                message = String.format(MConf.get().chatFormat, Rel.TRUCE.getColor() + me.getNameAndFactionName(), msg);
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    MPlayer mplayer = MPlayer.get(player);
                    if (mplayer.isSpying())
                    {
                        mplayer.msg(MConf.get().spyChatFormat, me.describeTo(mplayer, true), Chat.TRUCE.getName(), msg);
                    }
                    if (mplayer.getFaction() != myFaction && mplayer.getFaction().getRelationTo(myFaction) != Rel.TRUCE) continue;
                    mplayer.msg(message);
                }
                Factions.get().log(" [Truce Chat] " + me.getName() + " sent a message: " + msg);
                event.setCancelled(true);
                break;
            case PUBLIC:
            default:
                break;
        }
    }

}
