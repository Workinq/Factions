package com.massivecraft.factions.cmd;

import com.massivecraft.factions.action.ActionSpawnSandAlt;
import com.massivecraft.factions.action.ActionUpdateSandAlts;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.upgrade.UpgradesManager;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class CmdFactionsSandAlt extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSandAlt()
    {
        // Aliases
        this.addAliases("sandBot");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {

    }

    private Inventory getSandAltGui()
    {
        // Args
        int maxAlts = Integer.parseInt(UpgradesManager.get().getUpgradeByName(MUpgrade.get().sandAltUpgrade.getUpgradeName()).getCurrentDescription()[msenderFaction.getLevel(MUpgrade.get().sandAltUpgrade.getUpgradeName()) - 1].split(" ")[0]);
        Inventory inventory = Bukkit.createInventory(null, MConf.get().sandAltGuiSize, Txt.parse(MConf.get().sandAltGuiName));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        // Items
        chestGui.getInventory().setItem(10, new ItemBuilder(Material.SKULL_ITEM).name(Txt.parse("<k><bold>Spawn Alt")).setLore(Txt.parse(MUtil.list("<n>Click here to spawn a sand alt at", "<n>where you are currently standing", "", "<n>It will cost your faction bank" , "<k>$%,d <n>per sand placed", "", Txt.parse("<n>Faction Limit: <k>%d", maxAlts), "<n>Upgrade limit in <k>/f upgrade"))));
        chestGui.setAction(10, new ActionSpawnSandAlt(msenderFaction, msender, PS.valueOf(me)));
        chestGui.getInventory().setItem(19, new ItemBuilder(Material.INK_SACK).name(Txt.parse("<g><bold>Start All")).setLore(Txt.parse(MUtil.list("<n>Click here to make all your active", "<n>sand alts start printing sand"))).durability(10));
        chestGui.setAction(19, new ActionUpdateSandAlts(msenderFaction, msender, false));
        chestGui.getInventory().setItem(28, new ItemBuilder(Material.INK_SACK).name(Txt.parse("<b><bold>Stop All")).setLore(Txt.parse(MUtil.list("<n>Click here to make all your active", "<n>sand alts stop printing sand"))).durability(1));
        chestGui.setAction(28, new ActionUpdateSandAlts(msenderFaction, msender, true));
        chestGui.getInventory().setItem(37, new ItemBuilder(Material.BARRIER).name("<red><bold>Despawn All").setLore(Txt.parse(MUtil.list("<n>Click here to despawn all of", "<n>your active sand alts"))));
        chestGui.setAction(37, new ChestAction()
        {
            @Override
            public boolean onClick(InventoryClickEvent event)
            {
                msenderFaction.despawnAllSandAlts();
                msg("%s <i>despawned all of %s's <i>active sand alts.", msender.describeTo(msender, true), msenderFaction.describeTo(msender));
                return true;
            }
        });

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory(), new int[]{
                12, 13, 14, 15, 16,
                21, 22, 23, 24, 25,
                30, 31, 32, 33, 34,
                38, 39, 40, 41, 42
        });

        return chestGui.getInventory();
    }

}
