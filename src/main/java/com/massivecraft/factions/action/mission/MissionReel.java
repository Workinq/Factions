package com.massivecraft.factions.action.mission;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.Mission;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

// Cosmetic case-opening reel. The winner is already chosen and applied before this runs;
// the animation only scrolls the mission icons and "lands" on that winner.
public class MissionReel extends BukkitRunnable
{
    // -------------------------------------------- //
    // CONFIG
    // -------------------------------------------- //

    private static final int WINDOW = 7;          // visible reel slots (10..16)
    private static final int CENTER_OFFSET = 3;   // center slot 13 = window index 3
    private static final int TOTAL_ADVANCES = 30;  // how many scroll steps before landing
    private static final int DECELERATE_LAST = 10; // start slowing down over the final N steps

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Player player;
    private final Faction faction;
    private final Mission winner;

    private final List<Mission> pool;
    private final int size;
    private final int winnerIndex;

    private Inventory inventory;

    private int offset = 0;
    private int ticks = 0;
    private int advances = 0;
    private int gap = 1;
    private int nextAdvanceAt = 0;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public MissionReel(Player player, Faction faction, Mission winner)
    {
        this.player = player;
        this.faction = faction;
        this.winner = winner;
        this.pool = MMission.get().getMissions();
        this.size = this.pool.size();

        int index = 0;
        for (int i = 0; i < this.pool.size(); i++)
        {
            if (this.pool.get(i).getMissionName().equalsIgnoreCase(winner.getMissionName())) { index = i; break; }
        }
        this.winnerIndex = index;
    }

    // -------------------------------------------- //
    // START
    // -------------------------------------------- //

    public void start()
    {
        // Defer a tick so we can safely open a fresh inventory after the click event settles.
        Bukkit.getScheduler().runTask(Factions.get(), () ->
        {
            Inventory inv = Bukkit.createInventory(null, 27, Txt.parse("<gray>Rolling Mission..."));
            ChestGui chestGui = InventoryUtil.getChestGui(inv);
            this.inventory = chestGui.getInventory();

            this.decorate();
            this.renderWindow();
            this.player.openInventory(this.inventory);

            this.runTaskTimer(Factions.get(), 2L, 1L);
        });
    }

    // -------------------------------------------- //
    // ANIMATION
    // -------------------------------------------- //

    @Override
    public void run()
    {
        // Stop if the player navigated away or logged off.
        if ( ! player.isOnline() || ! inventory.getViewers().contains(player)) { this.cancel(); return; }

        ticks++;
        if (ticks < nextAdvanceAt) return;

        advances++;

        if (advances >= TOTAL_ADVANCES)
        {
            // Land: place the winner in the center slot.
            offset = ((winnerIndex - CENTER_OFFSET) % size + size) % size;
            renderWindow();
            highlightWinner();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            faction.msg("<g>The mission <i>%s <g>is now active. You have <i>%d hours <g>to complete it.", winner.getMissionName(), MConf.get().missionDeadlineHours);
            this.cancel();
            return;
        }

        offset = (offset + 1) % size;
        renderWindow();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f + Math.min(advances, 20) * 0.03f);

        // Decelerate over the final stretch for a "settling" feel.
        if (advances > TOTAL_ADVANCES - DECELERATE_LAST) gap++;
        nextAdvanceAt = ticks + gap;
    }

    // -------------------------------------------- //
    // RENDERING
    // -------------------------------------------- //

    private void decorate()
    {
        inventory.setItem(4, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(Txt.parse("<g><bold>v")));
        inventory.setItem(22, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(Txt.parse("<g><bold>^")));
        InventoryUtil.fillInventory(inventory, new int[]{4, 22, 10, 11, 12, 13, 14, 15, 16});
    }

    private void renderWindow()
    {
        for (int i = 0; i < WINDOW; i++)
        {
            Mission mission = pool.get((offset + i) % size);
            inventory.setItem(10 + i, icon(mission, false));
        }
    }

    private void highlightWinner()
    {
        inventory.setItem(10 + CENTER_OFFSET, icon(winner, true));
    }

    private ItemStack icon(Mission mission, boolean landed)
    {
        List<String> lore = new ArrayList<>();
        lore.add(Txt.parse("<n>Rarity: %s%s", mission.getRarity().getColor(), mission.getRarity().getDisplayName()));
        lore.add(Txt.parse("<n>Challenge: <k>%s", mission.getDescription()));
        lore.add(Txt.parse("<n>Reward: <k>%s Credits", NumberFormat.getInstance().format(mission.getReward())));
        if (landed)
        {
            lore.add("");
            lore.add(Txt.parse("<g><bold>Mission started!"));
        }
        return new ItemBuilder(mission.getItemMaterial())
                .name(Txt.parse(mission.getRarity().getColor() + mission.getMissionName()))
                .withLore(lore)
                .flag(ItemFlag.HIDE_ATTRIBUTES);
    }

}
