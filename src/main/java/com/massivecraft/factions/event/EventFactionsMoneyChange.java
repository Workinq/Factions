package com.massivecraft.factions.event;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.money.Money;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class EventFactionsMoneyChange extends EventFactionsAbstract
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

    private final Faction faction;
    public Faction getFaction() { return this.faction; }

    private double money;
    public double getMoney() { return this.money; }
    public void setMoney(double money) { this.money = money; }

    private final double oldMoney;
    public double getOldMoney() { return this.oldMoney; }

    private final Location spawnerLocation;
    public Location getSpawnerLocation() { return this.spawnerLocation; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public EventFactionsMoneyChange(Faction faction, double money, Location spawnerLocation)
    {
        super(false);
        this.faction = faction;
        this.money = money;
        this.spawnerLocation = spawnerLocation;
        this.oldMoney = Money.get(faction);
    }

}
