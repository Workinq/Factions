package com.massivecraft.factions.cmd.chunk;

import com.massivecraft.factions.action.chunkalt.ActionChunkaltDespawn;
import com.massivecraft.factions.action.chunkalt.ActionChunkaltEdit;
import com.massivecraft.factions.action.chunkalt.ActionChunkaltSpawn;
import com.massivecraft.factions.action.chunkalt.ActionChunkaltUpdate;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.*;
import com.massivecraft.factions.entity.object.ChunkAlt;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CmdFactionsChunkAltGui extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsChunkAltGui()
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
        if ( ! MPerm.getPermChunkalt().has(msender, faction, true)) return;

        // Open
        me.openInventory(this.getChunkAltGui(me, msender, faction));
    }

    public Inventory getChunkAltGui(Player player, MPlayer mplayer, Faction faction)
    {
        // Args
        int maxAlts;

        // Verify
        if (faction.getLevel(MUpgrade.get().chunkAltUpgrade.getUpgradeName()) == 0)
        {
            maxAlts = 5;
        }
        else
        {
            maxAlts = Integer.parseInt(MUpgrade.get().getUpgradeByName(MUpgrade.get().chunkAltUpgrade.getUpgradeName()).getCurrentDescription()[faction.getLevel(MUpgrade.get().chunkAltUpgrade.getUpgradeName()) - 1].split(" ")[0]);
        }

        Inventory inventory = Bukkit.createInventory(null, MConf.get().chunkAltGuiSize, Txt.parse(MConf.get().chunkAltGuiName));
        ChestGui chestGui = InventoryUtil.getChestGui(inventory, false);
        int[] altSlots = new int[]{12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43};

        // Items
        chestGui.getInventory().setItem(10, new ItemBuilder(Material.PLAYER_HEAD).name(Txt.parse("<k><bold>Spawn Alt")).withLore(Txt.parse(MUtil.list("<n>Click here to spawn a chunk alt at", "<n>where you are currently standing", "", "<n>It will cost your faction bank", Txt.parse("<k>$%,.0f <n>to spawn", MConf.get().chunkAltSpawnCost), "", Txt.parse("<n>Faction Limit: <k>%d", maxAlts), "<n>Upgrade limit in <k>/f upgrade"))));
        chestGui.setAction(10, new ActionChunkaltSpawn(faction, player, player.getLocation(), maxAlts));
        chestGui.getInventory().setItem(19, new ItemBuilder(Material.LIME_DYE).name(Txt.parse("<g><bold>Start All")).withLore(Txt.parse(MUtil.list("<n>Click here to make all your", "<n>chunk alts keep their chunks loaded"))));
        chestGui.setAction(19, new ActionChunkaltUpdate(faction, mplayer, false));
        chestGui.getInventory().setItem(28, new ItemBuilder(Material.RED_DYE).name(Txt.parse("<b><bold>Stop All")).withLore(Txt.parse(MUtil.list("<n>Click here to make all your", "<n>chunk alts stop keeping chunks loaded"))));
        chestGui.setAction(28, new ActionChunkaltUpdate(faction, mplayer, true));
        chestGui.getInventory().setItem(37, new ItemBuilder(Material.BARRIER).name(Txt.parse("<red><bold>Despawn All")).withLore(Txt.parse(MUtil.list("<n>Click here to despawn all of", "<n>your active chunk alts"))));
        chestGui.setAction(37, new ActionChunkaltDespawn(faction, mplayer, null, false));

        // Chunk Alts
        if ( ! faction.getChunkAlts().isEmpty() )
        {
            int slot = 0;
            // Loop - Chunk Alts
            for (ChunkAlt chunkAlt : faction.getChunkAlts())
            {
                // Args
                Location location = chunkAlt.getLocation();

                // Inventory
                chestGui.getInventory().setItem(altSlots[slot], new ItemBuilder(Material.PLAYER_HEAD).name(Txt.parse("<k><bold>CHUNK ALT")).withLore(Txt.parse(MUtil.list("<n>Click to manage the chunk alt at", Txt.parse("<i>x: <h>%,d <i>y: <h>%,d <i>z: <h>%,d <i>world: <h>%s", location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName())))));
                chestGui.setAction(altSlots[slot], new ActionChunkaltEdit(chunkAlt, faction, mplayer));

                // Increment
                slot += 1;
            }
        }

        // Fill
        InventoryUtil.fillInventory(chestGui.getInventory(), altSlots);

        return chestGui.getInventory();
    }

}
