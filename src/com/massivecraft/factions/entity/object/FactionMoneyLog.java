package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.store.EntityInternal;

public class FactionMoneyLog extends EntityInternal<FactionMoneyLog> {
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String playerId;
    public String getPlayerId() {
        return playerId;
    }

    private final String type;
    public String getType() {
        return type;
    }

    private final double amount;

    public double getAmount() {
        return amount;
    }

    private final long creationMillis;

    public long getCreationMillis() {
        return creationMillis;
    }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionMoneyLog(String playerId,String type, double amount, long creationMillis) {
        this.playerId = playerId;
        this.amount = amount;
        this.type = type;
        this.creationMillis = creationMillis;
    }
}
