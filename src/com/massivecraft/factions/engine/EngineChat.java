package com.massivecraft.factions.engine;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.chat.ChatFormatter;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerToRecipientChat;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

public class EngineChat extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineChat i = new EngineChat();
	public static EngineChat get() { return i; }
	public EngineChat()
	{
		this.setPlugin(Factions.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActiveInner(boolean active)
	{
		if ( ! active) return;
		
		if (MConf.get().chatSetFormat)
		{
			Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, MConf.get().chatSetFormatAt, new SetFormatEventExecutor(), Factions.get(), true);
		}
		
		if (MConf.get().chatParseTags)
		{
			Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, MConf.get().chatParseTagsAt, new ParseTagsEventExecutor(), Factions.get(), true);
		}
		
		if (MConf.get().chatParseTags)
		{
			Bukkit.getPluginManager().registerEvent(EventMassiveCorePlayerToRecipientChat.class, this, EventPriority.NORMAL, new ParseRelcolorEventExecutor(), Factions.get(), true);
		}
	}
	
	// -------------------------------------------- //
	// SET FORMAT
	// -------------------------------------------- //
	
	private class SetFormatEventExecutor implements EventExecutor
	{
		@Override
		public void execute(Listener listener, Event event) throws EventException
		{
			try
			{
				if (!(event instanceof AsyncPlayerChatEvent)) return;
				setFormat((AsyncPlayerChatEvent)event);
			}
			catch (Throwable t)
			{
				throw new EventException(t);
			}
		}
	}
	
	public static void setFormat(AsyncPlayerChatEvent event)
	{	
		event.setFormat(MConf.get().chatSetFormatTo);
	}
	
	// -------------------------------------------- //
	// PARSE TAGS
	// -------------------------------------------- //

	private class ParseTagsEventExecutor implements EventExecutor
	{
		@Override
		public void execute(Listener listener, Event event) throws EventException
		{
			try
			{
				if (!(event instanceof AsyncPlayerChatEvent)) return;
				parseTags((AsyncPlayerChatEvent)event);
			}
			catch (Throwable t)
			{
				throw new EventException(t);
			}
		}
	}

	public static void parseTags(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		String format = event.getFormat();
		format = ChatFormatter.format(format, player, null);
		event.setFormat(format);
	}
	
	// -------------------------------------------- //
	// PARSE RELCOLOR
	// -------------------------------------------- //
	
	private class ParseRelcolorEventExecutor implements EventExecutor
	{
		@Override
		public void execute(Listener listener, Event event) throws EventException
		{
			try
			{
				if (!(event instanceof EventMassiveCorePlayerToRecipientChat)) return;
				parseRelcolor((EventMassiveCorePlayerToRecipientChat)event);
			}
			catch (Throwable t)
			{
				throw new EventException(t);
			}
		}
	}

	public static void parseRelcolor(EventMassiveCorePlayerToRecipientChat event)
	{
		String format = event.getFormat();
		format = ChatFormatter.format(format, event.getSender(), event.getRecipient());
		event.setFormat(format);
	}

	// -------------------------------------------- //
	// FACTION CHAT
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
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
				message = String.format(MConf.get().chatFormat, me.describeTo(myFaction), msg);
				for (MPlayer mPlayer : MPlayerColl.get().getAllOnline())
				{
					if (mPlayer.isSpying())
					{
						mPlayer.msg(MConf.get().spyChatFormat, me.describeTo(mPlayer, true), Chat.FACTION.getName(), msg);
					}
					if (mPlayer.getFaction() != myFaction) continue;
					// Sad times :(
					if (mPlayer.isIgnoring(me)) continue;
					mPlayer.msg(message);
				}
				Factions.get().log(" [Faction Chat]" + me.getName() + " sent a message: " + msg);
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
				Factions.get().log(" [Ally Chat]" + me.getName() + " sent a message: " + msg);
				event.setCancelled(true);
				break;
			case TRUCE:
				message = String.format(MConf.get().chatFormat, Rel.TRUCE.getColor() + me.getNameAndFactionName(), msg);
				for (MPlayer mPlayer : MPlayerColl.get().getAllOnline())
				{
					if (mPlayer.isSpying())
					{
						mPlayer.msg(MConf.get().spyChatFormat, me.describeTo(mPlayer, true), Chat.TRUCE.getName(), msg);
					}
					if (mPlayer.getFaction() != myFaction && mPlayer.getFaction().getRelationTo(myFaction) != Rel.TRUCE) continue;
					mPlayer.msg(message);
				}
				Factions.get().log(" [Truce Chat]" + me.getName() + " sent a message: " + msg);
				event.setCancelled(true);
				break;
			case PUBLIC:
			default:
				break;
		}
	}

}
