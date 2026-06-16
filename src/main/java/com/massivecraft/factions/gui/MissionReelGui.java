package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.Mission;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.Gui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MissionReelGui extends Gui
{
    // -------------------------------------------- //
    // CONFIG
    // -------------------------------------------- //

    private static final int WINDOW = 7;
    private static final int CENTER_OFFSET = 3;
    private static final int TOTAL_ADVANCES = 30;
    private static final int DECELERATE_LAST = 10;

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final Mission winner;

    private final List<Mission> pool;
    private final int size;
    private final int winnerIndex;

    private int offset = 0;
    private int ticks = 0;
    private int advances = 0;
    private int gap = 1;
    private int nextAdvanceAt = 0;
    private boolean finished = false;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public MissionReelGui(Player player, Faction faction, Mission winner)
    {
        super(player, 3, "<gray>Rolling Mission...");
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
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        set(4, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(Txt.parse("<g><bold>v")));
        set(22, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(Txt.parse("<g><bold>^")));
        fillBorder();
        for (int i = 0; i < WINDOW; i++)
        {
            set(10 + i, icon(pool.get((offset + i) % size), false));
        }
    }

    @Override
    protected void onOpen()
    {
        scheduleRepeating(this::tick, 1L);
    }

    // -------------------------------------------- //
    // ANIMATION
    // -------------------------------------------- //

    private void tick()
    {
        if ( ! getPlayer().isOnline() || ! this.isOpen()) return;
        if (finished) return;

        Player player = getPlayer();

        ticks++;
        if (ticks < nextAdvanceAt) return;

        advances++;

        if (advances >= TOTAL_ADVANCES)
        {
            // Land on the winner and leave it displayed until the player closes.
            finished = true;
            offset = ((winnerIndex - CENTER_OFFSET) % size + size) % size;
            renderWindow();
            highlightWinner();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            faction.msg("<g>The mission <i>%s <g>is now active. You have <i>%d hours <g>to complete it.", winner.getMissionName(), MConf.get().missionDeadlineHours);
            return;
        }

        offset = (offset + 1) % size;
        renderWindow();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f + Math.min(advances, 20) * 0.03f);

        if (advances > TOTAL_ADVANCES - DECELERATE_LAST) gap++;
        nextAdvanceAt = ticks + gap;
    }

    // -------------------------------------------- //
    // RENDERING
    // -------------------------------------------- //

    private void renderWindow()
    {
        for (int i = 0; i < WINDOW; i++)
        {
            Mission mission = pool.get((offset + i) % size);
            setItem(10 + i, icon(mission, false));
        }
    }

    private void highlightWinner()
    {
        setItem(10 + CENTER_OFFSET, icon(winner, true));
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
