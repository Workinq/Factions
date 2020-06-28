package com.massivecraft.factions.cmd.vault;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.action.vault.ActionClickRepairVault;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class CmdFactionsVaultOpen extends FactionsCommand
{

    public CmdFactionsVaultOpen()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    @Override
    public void perform() throws MassiveException {
        // Args
        Faction faction = this.readArg(msenderFaction);

        // Perms
        if (faction != msenderFaction && ! Perm.VAULT_OPEN_ANY.has(sender, true)) return;

        // MPerm
        if ( ! MPerm.getPermVault().has(msender, faction, true)) return;

        if(faction.getVault() == null) {
            msender.msg("<b>You need to set your vault first using <white>/f vault set");
            return;
        }

        me.openInventory(getVaultGui(msenderFaction));
    }

    private Inventory getVaultGui(Faction faction) {
        Inventory inventory = Bukkit.createInventory(null, 27, Txt.parse("<gray>Faction Vault"));
        ChestGui chestGui = ChestGui.getCreative(inventory);

        // Chest Setup
        chestGui.setAutoclosing(true);
        chestGui.setAutoremoving(true);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        if(faction.getVault().getIfDamaged()) {
            if(faction.getVault().getCanRepair()) {
                chestGui.getInventory().setItem(10, new ItemBuilder(Material.STAINED_CLAY).name(Txt.parse("<i> Repair Vault")).addLore(Txt.parse("<gold><bold><u> Click to repair")).durability(1));
            } else {
                chestGui.getInventory().setItem(10, new ItemBuilder(Material.STAINED_CLAY).name(Txt.parse("<i> Repair Vault")).addLore(Txt.parse("<b> Repairable in %time%").replace("%time%",faction.getVault().getWhenCanRepairTime())).durability(14));
            }
        } else {
            chestGui.getInventory().setItem(10, new ItemBuilder(Material.STAINED_CLAY).name(Txt.parse("<i> Repair Vault")).addLore(Txt.parse("<grey> The vault is not damaged")).durability(5));
        }
        chestGui.setAction(10, new ActionClickRepairVault(msender,msenderFaction));
        InventoryUtil.fillInventory(chestGui.getInventory());

        return chestGui.getInventory();
    }

}
