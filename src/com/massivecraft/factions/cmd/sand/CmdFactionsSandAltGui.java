package com.massivecraft.factions.cmd.sand;

import com.massivecraft.factions.action.ActionDespawnSandAlt;
import com.massivecraft.factions.action.ActionEditSandAlt;
import com.massivecraft.factions.action.ActionSpawnSandAlt;
import com.massivecraft.factions.action.ActionUpdateSandAlts;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MUpgrade;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class CmdFactionsSandAltGui extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSandAltGui()
    {
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
        if ( ! MPerm.getPermSandalt().has(msender, msenderFaction, true)) return;

        me.openInventory(this.getSandAltGui());
    }

    private Inventory getSandAltGui()
    {
        // Args
        int maxAlts;

        if (msenderFaction.getLevel(MUpgrade.get().sandAltUpgrade.getUpgradeName()) == 0)
        {
            maxAlts = 5;
        }
        else
        {
            maxAlts = Integer.parseInt(MUpgrade.get().getUpgradeByName(MUpgrade.get().sandAltUpgrade.getUpgradeName()).getCurrentDescription()[msenderFaction.getLevel(MUpgrade.get().sandAltUpgrade.getUpgradeName()) - 1].split(" ")[0]);
        }

        Inventory inventory = Bukkit.createInventory(null, MConf.get().sandAltGuiSize, Txt.parse(MConf.get().sandAltGuiName));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory);
        int[] altSlots = new int[]{12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43};

        // Items
        chestGui.getInventory().setItem(10, new ItemBuilder(Material.SKULL_ITEM).name(Txt.parse("<k><bold>Spawn Alt")).durability(3).setLore(Txt.parse(MUtil.list("<n>Click here to spawn a sand alt at", "<n>where you are currently standing", "", "<n>It will cost your faction bank" , Txt.parse("<k>$%.1f <n>per sand placed", MConf.get().sandCost), "", Txt.parse("<n>Faction Limit: <k>%d", maxAlts), "<n>Upgrade limit in <k>/f upgrade"))));
        chestGui.setAction(10, new ActionSpawnSandAlt(msenderFaction, me, me.getLocation(), maxAlts));
        chestGui.getInventory().setItem(19, new ItemBuilder(Material.INK_SACK).name(Txt.parse("<g><bold>Start All")).setLore(Txt.parse(MUtil.list("<n>Click here to make all your active", "<n>sand alts start printing sand"))).durability(10));
        chestGui.setAction(19, new ActionUpdateSandAlts(msenderFaction, msender, false));
        chestGui.getInventory().setItem(28, new ItemBuilder(Material.INK_SACK).name(Txt.parse("<b><bold>Stop All")).setLore(Txt.parse(MUtil.list("<n>Click here to make all your active", "<n>sand alts stop printing sand"))).durability(1));
        chestGui.setAction(28, new ActionUpdateSandAlts(msenderFaction, msender, true));
        chestGui.getInventory().setItem(37, new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn All")).setLore(Txt.parse(MUtil.list("<n>Click here to despawn all of", "<n>your active sand alts"))));
        chestGui.setAction(37, new ActionDespawnSandAlt(msenderFaction, msender, null));

        // Sand Alts
        if ( ! msenderFaction.getSandAlts().isEmpty() )
        {
            int slot = 0;
            // Loop - Sand Alts
            for (SandAlt sandAlt : msenderFaction.getSandAlts())
            {
                // Args
                Location location = sandAlt.getLocation();

                // Inventory
                chestGui.getInventory().setItem(altSlots[slot], new ItemBuilder(Material.SKULL_ITEM).name(Txt.parse("<k><bold>SAND ALT")).durability(3).setLore(Txt.parse(MUtil.list("<n>Click to manage the sand alt at", Txt.parse("<i>x: <h>%,d <i>y: <h>%,d <i>z: <h>%,d <i>world: <h>%s", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName())))));
                chestGui.setAction(altSlots[slot], new ActionEditSandAlt(sandAlt, msenderFaction, msender));

                // Increment
                slot += 1;
            }
        }

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory(), altSlots);

        return chestGui.getInventory();
    }

}
