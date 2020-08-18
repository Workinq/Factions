package com.massivecraft.factions.cmd;

import com.massivecraft.factions.action.ActionStartMission;
import com.massivecraft.factions.action.ActionViewMissions;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.mission.AbstractMission;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.TimeUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CmdFactionsMission extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsMission()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);

        // MPerm
        if ( ! MPerm.getPermMission().has(msender, faction, true)) return;

        // Open
        me.openInventory(this.getMissionGui(faction));
    }

    public Inventory getMissionGui(Faction faction)
    {
        // Args
        Inventory inventory = Bukkit.createInventory(null, MConf.get().missionGuiSize, Txt.parse(MConf.get().missionGuiName));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory, false);
        List<String> lore = new ArrayList<>();
        AbstractMission activeMission = faction.getActiveMission();

        if (activeMission == null)
        {
            if (System.currentTimeMillis() - faction.getMissionStart() < TimeUnit.MILLIS_PER_HOUR * MConf.get().missionDeadlineHours)
            {
                lore.add(Txt.parse("<b>Cannot start mission for:"));
                lore.add("");
                lore.add(Txt.parse("<n>%s", TimeUtil.formatTime((TimeUnit.MILLIS_PER_HOUR * MConf.get().missionDeadlineHours) - (System.currentTimeMillis() - faction.getMissionStart()), true)));
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
                        .replace("%time%", TimeUtil.formatTime((TimeUnit.MILLIS_PER_HOUR * MConf.get().missionDeadlineHours) - (System.currentTimeMillis() - faction.getMissionStart()), false))));
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

}
