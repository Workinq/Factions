package com.massivecraft.factions.entity.object;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Location;

public class Vault extends EntityInternal<Vault> {

    public Vault(PS location, boolean damaged) {
        this.location = location;
        this.damaged = damaged;
        this.whenCanRepair = 0;
    }

    public long whenCanRepair;

    public String getWhenCanRepairTime() {
        int seconds = (int) ((whenCanRepair - System.currentTimeMillis()) / 1000L);
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);
        return minutes + "m " + seconds + "s";
    }

    public void setWhenCanRepair(long time) {
        whenCanRepair = time;
    }

    public boolean getCanRepair() {
        return whenCanRepair < System.currentTimeMillis();
    }

    public void damageVault() {
        this.setDamaged(true);
        final long time = System.currentTimeMillis() + (1000L * 1200);
        this.setWhenCanRepair(time);
        // todo: start koth
        this.changed();
    }

    public void repairVault() {
        this.setDamaged(false);
        this.setWhenCanRepair(0);
        // todo: stop koth
        createVault();
        this.changed();
    }

    public void createVault() {
        final Location min = this.location.asBukkitLocation().clone().add(-4, -2, -4);
        final Location max = this.location.asBukkitLocation().clone().add(3, 3, 3);
        //todo: generate vault
    }

    public boolean damaged;

    public boolean getIfDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public final PS location;

    public PS getLocation() {
        return location;
    }

    public boolean getHitBreakable(Location location) {
        final Location min = this.location.asBukkitLocation().clone().add(-4, -2, -4);
        final Location max = this.location.asBukkitLocation().clone().add(3, 3, 3);
        return location.getX() <= max.getX() && location.getX() >= min.getX() && location.getY() <= max.getY() && location.getY() >= min.getY() && location.getZ() <= max.getZ() && location.getZ() >= min.getZ();
    }

    public boolean getHitBottom(Location location) {
        final Location min = this.location.asBukkitLocation().clone().add(-4, -3, -4);
        final Location max = this.location.asBukkitLocation().clone().add(3, -3, 3);
        return location.getX() <= max.getX() && location.getX() >= min.getX() && location.getY() <= max.getY() && location.getY() >= min.getY() && location.getZ() <= max.getZ() && location.getZ() >= min.getZ();
    }

}
