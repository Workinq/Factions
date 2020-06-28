package com.massivecraft.factions.action.vault;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ActionClickRepairVault extends ChestActionAbstract {
    Faction faction;
    MPlayer mplayer;

    public ActionClickRepairVault(MPlayer mplayer,Faction faction) {
        this.faction = faction;
        this.mplayer = mplayer;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player) {
        if(faction.getVault() == null)return true;
        if(!faction.getVault().getIfDamaged()) {
            mplayer.msg("<i>Your faction vault is not damaged.");
            return true;
        }
        if(faction.getVault().getCanRepair()) {
            faction.getVault().repairVault();
            faction.msg("%s <i>has repaired the faction vault", mplayer.describeTo(faction, true));
        } else {
            mplayer.msg("<i>You can repair your vault in <b>" + faction.getVault().getWhenCanRepairTime());
        }
        return false;
    }
}
