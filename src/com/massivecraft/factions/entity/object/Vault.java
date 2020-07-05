package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.Factions;
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

    public Vault(PS location, boolean damaged,Faction faction) {
        this.location = location;
        this.damaged = damaged;
        this.time = 0;
        this.factionId = faction.getId();
        this.createVault();
        this.changed();
    }

    private String factionId;

    private long time;

    public String getWhenCanRepairTime() {
        int seconds = (int) ((time - System.currentTimeMillis()) / 1000L);
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);
        return minutes + "m " + seconds + "s";
    }

    public void setTime(long time) {
        this.time = time;

        // Mark as changed
        this.changed();
    }

    public boolean canRepair() {
        return time < System.currentTimeMillis();
    }

    public void damageVault() {
        setDamaged(true);
        final long time = System.currentTimeMillis() + (1000L * 1200);
        setTime(time);
        startKoth();
        this.changed();
    }

    public void repairVault() {
        setDamaged(false);
        setTime(0);
        stopKoth();
        createVault();
        this.changed();
    }

    private void createVault() {
        final Location min = location.asBukkitLocation().clone().add(-4, -3, -4);
        final Location max = location.asBukkitLocation().clone().add(3, 3, 3);

        for (Player player : location.asBukkitLocation().getWorld().getPlayers()) {
            if (player.getLocation().distance(location.asBukkitLocation()) >= 10) continue;
            if (player.getLocation().getY() < min.getY() || player.getLocation().getY() > max.getY()) continue;
            if (player.getLocation().getX() < min.getX() || player.getLocation().getX() > max.getX()) continue;
            if (player.getLocation().getZ() < min.getZ() || player.getLocation().getZ() > max.getZ()) continue;
            player.teleport(location.asBukkitLocation().clone().add(-0.5, -3, -5.5));
        }

        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                for (int y = -3; y < 4; y++) {
                    if(y == -3) {
                        location.asBukkitLocation().clone().add(x,y,z).getBlock().setType(Material.BEDROCK);
                        continue;
                    }
                    location.asBukkitLocation().clone().add(x, y, z).getBlock().setType(Material.IRON_BLOCK);
                }
            }
        }
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                for (int y = -2; y < 3; y++) {
                    location.asBukkitLocation().clone().add(x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }

        // Mark as changed
        this.changed();
    }

    public void deleteVault() {
        // Loop
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                for (int y = -3; y < 4; y++) {
                    location.asBukkitLocation().clone().add(x, y, z).getBlock().setType(Material.AIR);
                }
            }
        }

        // Stop
        this.stopKoth();

        // Mark as changed
        this.changed();
    }

    private boolean damaged;

    public boolean isDamaged() {
        return damaged;
    }

    private void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public final PS location;

    public PS getLocation() {
        return location;
    }

    public boolean getHitVault(Location location) {
        final Location min = this.location.asBukkitLocation().clone().add(-4, -3, -4);
        final Location max = this.location.asBukkitLocation().clone().add(3, 3, 3);
        return location.getX() <= max.getX() && location.getX() >= min.getX() && location.getY() <= max.getY() && location.getY() >= min.getY() && location.getZ() <= max.getZ() && location.getZ() >= min.getZ();
    }

    private transient Faction currentCapper;
    private transient List<Integer> loops;
    private transient boolean contested;

    public void startKoth() {
        if(loops == null) {
            loops = new ArrayList<>();
        }
        checkLoop();
        captureLoop();
    }

    private void stopKoth() {
        if(loops == null)return;
        if (loops.isEmpty()) return;
        for(int task : loops) {
            Bukkit.getScheduler().cancelTask(task);
        }
        loops.clear();
    }

    private void checkLoop() {
        currentCapper = null;
        final BukkitTask task = Bukkit.getScheduler().runTaskTimer(Factions.get(), () ->
        {
            // Args
            List<Player> playersInRegion = this.playersInRegion();
            boolean stillCapturing = false;

            // Loop - Players
            for (Player player : playersInRegion) {
                if (currentCapper != null) {
                    if (currentCapper != MPlayer.get(player).getFaction()) continue;
                    stillCapturing = true;
                    break;
                }
            }

            // Verify - Still Capturing
            if (!stillCapturing) {
                if (currentCapper != null) {
                    currentCapper.msg("<b>You have stopped stealing money from the faction %s", Faction.get(factionId).describeTo(currentCapper));
                }
                currentCapper = null;
            }

            // Verify - Players in Region
            if (playersInRegion.size() == 0) return;

            // Loop - Players
            boolean stillContested = false;
            for (Player player : playersInRegion) {
                final Faction faction = MPlayer.get(player).getFaction();
                if (faction.isNone()) continue;

                final Faction from = Faction.get(factionId);
                if (faction == from) {
                    contested = true;
                    stillContested = true;
                    continue;
                }
                if (currentCapper == null) {
                    faction.msg("<b>You have started to steal money from the faction %s", from.describeTo(faction));
                    currentCapper = faction;
                    continue;
                }
            }
            if(contested && stillContested)return;
            contested = false;
        }, 0, 10L);
        loops.add(task.getTaskId());
    }

    private void captureLoop() {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Factions.get(), () -> {

            // Verify
            if (currentCapper == null) return;

            // Args
            final Faction from = Faction.get(factionId);
            if(from == null)return;
            double amount = Money.get(from) / 10;
            if (amount < 10000) {
                currentCapper.msg("%s <b> does not have any significant money left to steal.", from.describeTo(currentCapper));
                return;
            }
            if(contested) {
                currentCapper.msg("<g>You failed to steal money from %s due to the vault being contested.",from.getName());
                return;
            }

            // Move
            Econ.transferMoney(from, currentCapper, null, amount, false);

            // Inform
            from.msg("%s <b>has stolen <h>$%.1f <b>from your vault.", currentCapper.describeTo(from, true), amount);
            currentCapper.msg("<g>You <i>have stolen <h>$%.1f <i>from %s", amount, from.describeTo(currentCapper));

            // Log
            Factions.get().log(Txt.parse("%s <b>has stolen $%.1f from %s", currentCapper.getName(), amount, from.getName()));
        }, 0, 60 * 20L);
        loops.add(task.getTaskId());
    }

    private List<Player> playersInRegion() {
        // Args
        List<Player> players = new ArrayList<>();
        World world = location.asBukkitWorld();

        // Verify
        if (world.getPlayers() == null) return players;

        for (Player player : world.getPlayers()) {
            if (!this.getHitVault(player.getLocation())) continue;
            players.add(player);
        }

        // Return
        return players;
    }

}
