package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.Mission;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.TimeUtil;
import com.massivecraft.massivecore.chestgui.ClickContext;
import com.massivecraft.massivecore.chestgui.type.StandardGui;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MissionMenu extends StandardGui
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public MissionMenu(Player player, Faction faction)
    {
        super(player, 3, MConf.get().missionGuiName);
        this.faction = faction;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    protected void build()
    {
        fillBorder();

        button(11, new ItemBuilder(Material.BOOK).name(Txt.parse("<k><bold>All Missions"))
            .addLore(Txt.parse("<n>Click here to see a list of all faction missions"))
            .addLore(Txt.parse("<n>along with their requirements and rewards")), ctx -> new MissionListMenu(ctx.getPlayer()).open());

        button(13, buildActiveOrStartItem(), this::onStartClick);

        set(15, new ItemBuilder(Material.PAPER).name(Txt.parse("<k><bold>Information"))
            .addLore(Txt.parse("<n>Faction missions are tasks randomly assigned which"))
            .addLore(Txt.parse("<n>a faction must complete to be rewarded with credits."))
            .addLore("")
            .addLore(Txt.parse("<n>A faction can then use these credits to purchase"))
            .addLore(Txt.parse("<n>faction upgrades which can be viewed using <k>/f upgrade<n>."))
            .addLore("")
            .addLore(Txt.parse("<n>Factions with the most missions completed by the end"))
            .addLore(Txt.parse("<n>of each week will be rewarded with a buycraft voucher."))
            .addLore(Txt.parse("<n>View the top factions using <k>/f leaderboard mission<n>.")));

        button(22, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
    }

    @Override
    protected void onOpen()
    {
        // Keep the cooldown / progress / time-remaining current while open.
        scheduleRepeating(() -> setItem(13, buildActiveOrStartItem()), 20L);
    }

    // -------------------------------------------- //
    // SLOT 13
    // -------------------------------------------- //

    private boolean isOffCooldown()
    {
        return System.currentTimeMillis() - faction.getMissionStart() >= TimeUnit.MILLIS_PER_HOUR * MConf.get().missionDeadlineHours;
    }

    private ItemStack buildActiveOrStartItem()
    {
        List<String> lore = new ArrayList<>();
        Mission activeMission = faction.getActiveMission();

        if (activeMission == null)
        {
            if ( ! isOffCooldown())
            {
                lore.add(Txt.parse("<b>Cannot start mission for:"));
                lore.add("");
                lore.add(Txt.parse("<n>%s", TimeUtil.formatTime((TimeUnit.MILLIS_PER_HOUR * MConf.get().missionDeadlineHours) - (System.currentTimeMillis() - faction.getMissionStart()), true)));
                return new ItemBuilder(MConf.get().missionItemType).name(Txt.parse(MConf.get().missionItemName)).withLore(lore);
            }

            lore.add(Txt.parse("<n>Click to start a new mission"));
            return new ItemBuilder(MConf.get().missionItemType).name(Txt.parse(MConf.get().missionItemName)).withLore(lore);
        }

        for (String line : MConf.get().missionItemLore)
        {
            lore.add(Txt.parse(line
                .replace("%progress", String.format("%.1f", (faction.getMissionGoal() / activeMission.getRequirement()) * 100))
                .replace("%description%", activeMission.getDescription())
                .replace("%reward%", String.valueOf(activeMission.getReward()))
                .replace("%time%", TimeUtil.formatTime((TimeUnit.MILLIS_PER_HOUR * MConf.get().missionDeadlineHours) - (System.currentTimeMillis() - faction.getMissionStart()), false))));
        }
        return new ItemBuilder(activeMission.getItemMaterial()).amount(1).name(Txt.parse(activeMission.getRarity().getColor() + activeMission.getItemName())).withLore(lore);
    }

    private void onStartClick(ClickContext ctx)
    {
        if (faction.getActiveMission() != null) return;
        if ( ! isOffCooldown()) return;

        Mission winner = MMission.get().selectWeighted();
        if (winner == null) return;

        // Apply before the reel opens so closing it mid-roll cannot change the result.
        faction.setMissionStart(System.currentTimeMillis());
        faction.setMissionGoal(0);
        faction.setActiveMission(winner.getMissionName());

        new MissionReelGui(ctx.getPlayer(), faction, winner).open();
    }
}
