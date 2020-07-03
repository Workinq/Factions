package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Vault extends EntityInternal<Vault> {

    public Vault(PS location, boolean damaged) {
        this.location = location;
        this.damaged = damaged;
        this.whenCanRepair = 0;
        createVault();
        this.changed();
    }

    private long whenCanRepair;

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
        startKoth();
        this.changed();
    }

    public void repairVault() {
        this.setDamaged(false);
        this.setWhenCanRepair(0);
        stopKoth();
        createVault();
        this.changed();
    }

    private void createVault() {
        final Location min = this.location.asBukkitLocation().clone().add(-4, -3, -4);
        final Location max = this.location.asBukkitLocation().clone().add(3, 3, 3);
        for (Player player : this.location.asBukkitLocation().getWorld().getPlayers()) {
            if (player.getLocation().distance(this.location.asBukkitLocation()) >= 10) continue;
            if (player.getLocation().getY() < min.getY() || player.getLocation().getY() > max.getY()) continue;
            if (player.getLocation().getX() < min.getX() || player.getLocation().getX() > max.getX()) continue;
            if (player.getLocation().getZ() < min.getZ() || player.getLocation().getZ() > max.getZ()) continue;
            player.teleport(this.location.asBukkitLocation().clone().add(-0.5, -3, -5.5));
        }
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                for (int y = -3; y < 4; y++) {
                    this.location.asBukkitLocation().clone().add(x, y, z).getBlock().setType(Material.IRON_BLOCK);
                }
            }
        }
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                for (int y = -2; y < 3; y++) {
                    this.location.asBukkitLocation().clone().add(x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }
        this.changed();
    }

    public void deleteVault() {
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                for (int y = -3; y < 4; y++) {
                    this.location.asBukkitLocation().clone().add(x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }
        this.stopKoth();
        this.changed();
    }

    private boolean damaged;

    public boolean getIfDamaged() {
        return damaged;
    }

    private void setDamaged(boolean damaged) {
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

    private transient Faction currentCapper;

    private transient List<BukkitTask> loops;

    public void startKoth() {
        checkLoop();
        captureLoop();
    }

    private void stopKoth() {
        if(loops == null) { loops = new ArrayList<>(); }
        if(loops.isEmpty())return;
        loops.forEach(BukkitTask::cancel);
        loops.clear();
    }

    private void checkLoop() {
        currentCapper = null;
        if(loops == null) { loops = new ArrayList<>(); }
        final BukkitTask task = Bukkit.getScheduler().runTaskTimer(Factions.get(), () -> {
            boolean stillCapturing = false;
            for (Player player : playersInRegion()) {
                if (currentCapper != null) {
                    if (currentCapper != MPlayer.get(player).getFaction()) continue;
                    stillCapturing = true;
                    break;
                }
            }
            if (!stillCapturing) {
                if(currentCapper != null) {
                    final Faction from = BoardColl.get().getFactionAt(this.location);
                    currentCapper.msg("<b>You have stopped stealing money from the faction %s", from.getName());
                    currentCapper = null;
                }
            }
            if (playersInRegion().size() == 0) return;
            for (Player player : playersInRegion()) {
                if (currentCapper == null) {
                    final Faction faction = MPlayer.get(player).getFaction();
                    if (faction.isNone()) {
                        MPlayer.get(player).msg("<b>You must be in a faction to steal money from this factions vault.");
                        continue;
                    }
                    final Faction from = BoardColl.get().getFactionAt(this.location);
                    if(faction == from)continue;
                    faction.msg("<b>You have started to steal money from the faction %s",from.getName());
                    this.currentCapper = faction;
                    return;
                }
            }
        }, 0, 10L);
        loops.add(task);
    }

    private void captureLoop() {
        if(loops == null) { loops = new ArrayList<>(); }
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Factions.get(), () -> {
            if (currentCapper == null) return;
            final Faction from = BoardColl.get().getFactionAt(this.location);
            double amount = Money.get(from)/10;
            Money.move(from,currentCapper,null,amount);
            from.msg("%s <b>has stolen $%.1f from your vault.", currentCapper.describeTo(from, true), amount);
            currentCapper.msg("<b>You have stolen $%.1f from %s",amount,from.describeTo(currentCapper));
            Factions.get().log(Txt.parse("%s <b>has stolen $%.1f from %s",currentCapper.getName(),amount,from.getName()));
        }, 0, 60 * 20L);
        loops.add(task);
    }

    private List<Player> playersInRegion() {
        List<Player> players = new ArrayList<>();
        World world = this.location.asBukkitWorld();
        if (world.getPlayers() == null) return players;
        for (Player player : world.getPlayers()) {
            if (!getHitBreakable(player.getLocation())) continue;
            players.add(player);
        }
        return players;
    }

}
