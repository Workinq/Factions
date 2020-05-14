package com.massivecraft.factions.action;

import com.massivecraft.factions.engine.EngineSand;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.factions.util.InventoryUtil;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ActionEditSandAlt extends ChestActionAbstract
{

    private final SandAlt sandAlt;
    private final Faction faction;
    private final MPlayer mplayer;

    public ActionEditSandAlt(SandAlt sandAlt, Faction faction, MPlayer mplayer)
    {
        this.sandAlt = sandAlt;
        this.faction = faction;
        this.mplayer = mplayer;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        player.openInventory(EngineSand.get().getEditGui(sandAlt, faction, mplayer));
        return true;
    }

}
