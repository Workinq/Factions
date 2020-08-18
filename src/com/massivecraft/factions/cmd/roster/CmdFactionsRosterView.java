package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.comparator.ComparatorMPlayerRole;
import com.massivecraft.factions.engine.EngineSkull;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.ScrollerInventory;
import com.massivecraft.factions.util.TimeUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdFactionsRosterView extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterView()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);

        // MPerm
        if (!MPerm.getPermRoster().has(msender, faction, true)) return;

        // Open
        me.openInventory(this.getRosterGui(faction));
    }

    private Inventory getRosterGui(Faction faction)
    {
        // Args
        RosterScrollerInventory scrollerInventory = new RosterScrollerInventory();
        ChestGui chestGui = scrollerInventory.getBlankPage(Txt.parse("<gray>Faction Roster"), 54, me);
        scrollerInventory.fillSidesWithItem(chestGui.getInventory(), new ItemBuilder(MConf.get().fillerItemMaterial).name(Txt.parse(MConf.get().fillerItemName)).durability(MConf.get().fillerItemData));
        String rosterKicks = String.format("%,d", this.getRosterKicksRemaining(faction));

        // Loop
        List<MPlayer> mplayers = faction.getMPlayersWhereAlt(false);
        mplayers.sort(ComparatorMPlayerRole.get());

        for (MPlayer mplayer : mplayers)
        {
            Integer slot = scrollerInventory.getEmptyNonSideSlots(chestGui.getInventory()).stream().findFirst().orElse(null);
            if (slot == null)
            {
                chestGui = scrollerInventory.getBlankPage(Txt.parse("<gray>Faction Roster"), 54, me);
                scrollerInventory.fillSidesWithItem(chestGui.getInventory(), new ItemBuilder(MConf.get().fillerItemMaterial).name(Txt.parse(MConf.get().fillerItemName)).durability(MConf.get().fillerItemData));
                slot = scrollerInventory.getEmptyNonSideSlots(chestGui.getInventory()).stream().findFirst().orElse(null);
                if (slot == null) break;
            }

            List<String> lore = new ArrayList<>();
            lore.add(Txt.parse("<white>Last online: " + (mplayer.isOnline() ? "<g>now" : "<b>" + TimeUtil.formatTime(System.currentTimeMillis() - mplayer.getLastActivityMillis(), true) + " ago")));
            if (mplayer != msender)
            {
                lore.add(Txt.parse("<white>Join as: <n>" + mplayer.getRole().getDescPlayerOne()));
                lore.add(Txt.parse("<i>Click to kick from roster"));
                lore.add("");
                lore.add(Txt.parse("<n>Roster kicks remaining: <i>" + rosterKicks));
                lore.add("");
                lore.add(Txt.parse("<b>Note: <white>If this player is in the"));
                lore.add(Txt.parse("<white>faction, you must also have the kick"));
                lore.add(Txt.parse("<white>perm so they can be kicked as well"));
            }

            chestGui.getInventory().setItem(slot, new ItemBuilder(EngineSkull.get().getSkullItem(mplayer.getPlayer())).name(mplayer.describeTo(faction, true)).setLore(lore).durability(3));
        }

        return chestGui.getInventory();
    }

    private int getRosterKicksRemaining(Faction faction)
    {
        // Args
        MassiveList<Long> rosterKickTimes = new MassiveList<>(faction.getRosterKickTimes());
        boolean canKick = false;

        // Sort
        Collections.sort(rosterKickTimes);

        // Loop
        for (long time : rosterKickTimes)
        {
            long total = time + TimeUnit.MILLIS_PER_DAY;
            if (System.currentTimeMillis() > total)
            {
                // Apply
                canKick = true;

                // Remove
                faction.removeRosterKick(time);
            }
        }

        // Verify
        if (!canKick) return 0;

        // Return
        return MConf.get().rosterKickLimit - rosterKickTimes.size();
    }

    private static class RosterScrollerInventory extends ScrollerInventory
    {
        private final List<Integer> sideSlots = new ArrayList<>();

        @Override
        public void fillSidesWithItem(Inventory inventory, ItemStack item)
        {
            // Args
            int size = inventory.getSize(), rows = size / 9;

            if (rows >= 3)
            {
                for (int i = 0; i <= 8; ++i)
                {
                    inventory.setItem(i, item);
                    sideSlots.add(i);
                }
                for (int i = 8; i < inventory.getSize() - 9; i += 9)
                {
                    int lastSlot = i + 1;
                    inventory.setItem(i, item);
                    inventory.setItem(lastSlot, item);
                    sideSlots.add(i);
                    sideSlots.add(lastSlot);
                }
                for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++)
                {
                    inventory.setItem(i, item);
                    sideSlots.add(i);
                }
            }

            inventory.setItem(51, this.getNextPageButton());
            inventory.setItem(47, this.getPreviousPageButton());
        }

        @Override
        public List<Integer> getNonSideSlots(Inventory inventory)
        {
            List<Integer> availableSlots = new ArrayList<>();
            for (int i = 0; i < inventory.getSize(); i++)
            {
                if (!sideSlots.contains(i)) availableSlots.add(i);
            }
            return availableSlots;
        }

        @Override
        public List<Integer> getEmptyNonSideSlots(Inventory inventory)
        {
            List<Integer> availableSlots = new ArrayList<>();
            for (int i = 0; i < inventory.getSize(); i++)
            {
                if (!sideSlots.contains(i) && inventory.getContents()[i] == null) availableSlots.add(i);
            }
            return availableSlots;
        }
    }

}
