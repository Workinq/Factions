package com.massivecraft.factions.event;

import com.massivecraft.factions.ClaimType;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EventFactionsChunksChange extends EventFactionsAbstractSender
{	
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final Set<PS> chunks;
	public Set<PS> getChunks() { return this.chunks; }
	
	private final Faction newFaction;
	public Faction getNewFaction() { return this.newFaction; }
	
	private final Map<PS, Faction> oldChunkFaction;
	public Map<PS, Faction> getOldChunkFaction() { return this.oldChunkFaction; }
	
	private final Map<Faction, Set<PS>> oldFactionChunks;
	public Map<Faction, Set<PS>> getOldFactionChunks() { return this.oldFactionChunks; }
	
	private final Map<PS, EventFactionsChunkChangeType> chunkType;
	public Map<PS, EventFactionsChunkChangeType> getChunkType() { return this.chunkType; }
	
	private final Map<EventFactionsChunkChangeType, Set<PS>> typeChunks;
	public Map<EventFactionsChunkChangeType, Set<PS>> getTypeChunks() { return this.typeChunks; }

	private final ClaimType claimType;
	public ClaimType getClaimType() { return this.claimType; }
	public boolean isRaidClaim() { return this.claimType == ClaimType.RAID; }

	private final Long expiryMillis;
	public Long getExpiryMillis() { return this.expiryMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventFactionsChunksChange(CommandSender sender, Set<PS> chunks, Faction newFaction) {
		this(sender, chunks, newFaction, ClaimType.NORMAL, null);
	}

	public EventFactionsChunksChange(CommandSender sender, Set<PS> chunks, Faction newFaction, ClaimType claimType, Long expiryMillis) {
		super(sender);
		this.claimType = (claimType == null ? ClaimType.NORMAL : claimType);
		this.expiryMillis = expiryMillis;
		chunks = PS.getDistinctChunks(chunks);
		this.chunks = Collections.unmodifiableSet(chunks);
		this.newFaction = newFaction;
		this.oldChunkFaction = Collections.unmodifiableMap(BoardColl.getChunkFaction(chunks));
		this.oldFactionChunks = Collections.unmodifiableMap(MUtil.reverseIndex(this.oldChunkFaction));
		
		MPlayer msender = this.getMPlayer();
		Faction self = null;
		if (msender != null) self = msender.getFaction();
		Map<PS, EventFactionsChunkChangeType> currentChunkType = new MassiveMap<>();
		for (Entry<PS, Faction> entry : this.oldChunkFaction.entrySet())
		{
			PS chunk = entry.getKey();
			Faction from = entry.getValue();
			currentChunkType.put(chunk, EventFactionsChunkChangeType.get(from, newFaction, self));
		}
		
		this.chunkType = Collections.unmodifiableMap(currentChunkType);
		this.typeChunks = Collections.unmodifiableMap(MUtil.reverseIndex(this.chunkType));
	}
	
}
