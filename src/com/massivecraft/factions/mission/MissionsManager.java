package com.massivecraft.factions.mission;

import com.massivecraft.factions.action.ActionStartMission;
import com.massivecraft.factions.action.ActionViewMissions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.conf.ConfMission;
import com.massivecraft.factions.mission.missions.*;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.TimeUtil;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MissionsManager {

    private static final MissionsManager i = new MissionsManager();
    private final List<Mission> missions = new ArrayList<>();

    public static MissionsManager get()
    {
        return i;
    }

    public ConfMission getConfMissionByName(String missionName)
    {
        return MMission.get().challenges.stream().filter((confChallenge) -> confChallenge.getName().equalsIgnoreCase(missionName)).findFirst().orElse(null);
    }

    public Inventory getMissionGui(Faction faction)
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, MConf.get().missionGuiSize, Txt.parse(MConf.get().missionGuiName));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        // Arg Setup
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        List<String> lore = new ArrayList<>();
        Mission activeMission = faction.getActiveMission();

        if (activeMission == null)
        {
            if (System.currentTimeMillis() - faction.getMissionStart() < 86400000L)
            {
                lore.add(Txt.parse("<b>Cannot start mission for:"));
                lore.add("");
                lore.add(Txt.parse("<n>%s", TimeUtil.formatTime(86400000L - (System.currentTimeMillis() - faction.getMissionStart()), true)));
                chestGui.getInventory().setItem(MConf.get().missionItemSlot, new ItemBuilder(MConf.get().missionItemType).name(Txt.parse(MConf.get().missionItemName)).data(MConf.get().missionItemData).setLore(lore));
            }
            else
            {
                lore.add(Txt.parse("<n>Click to start a new mission"));
                chestGui.getInventory().setItem(MConf.get().missionItemSlot, new ItemBuilder(MConf.get().missionItemType).name(Txt.parse(MConf.get().missionItemName)).data(MConf.get().missionItemData));
                chestGui.setAction(MConf.get().missionItemSlot, new ActionStartMission(faction));
            }
        }
        else
        {
            for (String line : MConf.get().missionItemLore)
            {
                lore.add(Txt.parse(line
                        .replace("%progress", String.format("%.1f", (faction.getMissionGoal() / activeMission.getRequirement()) * 100))
                        .replace("%description%", activeMission.getDescription())
                        .replace("%reward%", String.valueOf(activeMission.getReward()))
                        .replace("%time%", TimeUtil.formatTime(86400000L - (System.currentTimeMillis() - faction.getMissionStart()), false))));
            }
            chestGui.getInventory().setItem(MConf.get().missionItemSlot, new ItemBuilder(activeMission.getItemMaterial()).durability(activeMission.getItemData()).amount(1).name(Txt.parse("<k>" + activeMission.getItemName())).setLore(lore));
        }
        chestGui.getInventory().setItem(11, new ItemBuilder(Material.BOOK).name(Txt.parse("<k><bold>All Missions"))
                .addLore(Txt.parse("<n>Click here to see a list of all faction missions"))
                .addLore(Txt.parse("<n>along with their requirements and rewards")));
        chestGui.setAction(11, new ActionViewMissions());
        chestGui.getInventory().setItem(15, new ItemBuilder(Material.PAPER).name(Txt.parse("<k><bold>Information"))
                .addLore(Txt.parse("<n>Faction missions are tasks randomly assigned which"))
                .addLore(Txt.parse("<n>a faction must complete to be rewarded with credits."))
                .addLore("")
                .addLore(Txt.parse("<n>A faction can then use these credits to purchase"))
                .addLore(Txt.parse("<n>faction upgrades which can be viewed using <k>/f upgrade<n>."))
                .addLore("")
                .addLore(Txt.parse("<n>Factions with the most missions completed by the end"))
                .addLore(Txt.parse("<n>of each week will be rewarded with a buycraft voucher."))
                .addLore(Txt.parse("<n>View the top factions using <k>/f leaderboard mission<n>.")));
        InventoryUtil.fillInventory(chestGui.getInventory());
        return chestGui.getInventory();
    }

    public void incrementProgress(Mission mission, MPlayer mplayer)
    {
        if (mplayer.isConsole()) return;

        this.incrementProgress(mission, mplayer, 1);
    }

    public void incrementProgress(Mission mission, MPlayer mplayer, Integer amount)
    {
        if (mplayer.isConsole()) return;

        this.incrementProgress(mission, mplayer.getFaction(), amount);
    }

    public void incrementProgress(Mission mission, Faction faction)
    {
        this.incrementProgress(mission, faction, 1);
    }

    public void incrementProgress(Mission mission, Faction faction, Integer amount)
    {
        if (faction.getActiveMission() == null) return;
        if (!faction.getActiveMission().getName().equalsIgnoreCase(mission.getName())) return;

        Integer missionComplete = faction.getMissionGoal();
        if ((double) (missionComplete + amount) >= mission.getRequirement() - (double) amount)
        {
            Integer credits = mission.getReward();
            faction.addCredits(credits);
            faction.setActiveMission(null);
            faction.setMissionGoal(0);
            NumberFormat creditsFormat = NumberFormat.getInstance();
            creditsFormat.setGroupingUsed(true);
            faction.msg("%s <g>has received <h>%s <g>credits for completing the <h>%s <g>mission.", faction.describeTo(faction), creditsFormat.format(credits), mission.getName());
        }
        else
        {
            faction.setMissionGoal(missionComplete + amount);
        }
    }

    public Mission getMissionByName(String missionName)
    {
        return missions.stream().filter((mission) -> mission.getName().equalsIgnoreCase(missionName)).findFirst().orElse(null);
    }

    public List<Mission> getMissions()
    {
        return missions;
    }

    public void load()
    {
        missions.add(new MissionBlaze());
        missions.add(new MissionSugarcane());
        missions.add(new MissionTrench());
        // missions.add(new MissionShards());
        missions.add(new MissionEXP());
        missions.add(new MissionTravel());
        missions.add(new MissionWitch());
    }

}
