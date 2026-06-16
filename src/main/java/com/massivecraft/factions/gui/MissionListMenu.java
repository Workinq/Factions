package com.massivecraft.factions.gui;

import com.massivecraft.factions.entity.MMission;
import com.massivecraft.factions.entity.mission.Mission;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.PagedGui;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.List;

public class MissionListMenu extends PagedGui<Mission>
{
    public MissionListMenu(Player player)
    {
        super(player, 6, "<gray>All Missions");
    }

    @Override
    protected void build()
    {
        super.build();
        button((getRows() - 1) * 9 + 4, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
    }

    @Override
    protected List<Mission> items()
    {
        return MMission.get().getMissions();
    }

    @Override
    protected ItemStack icon(Mission mission)
    {
        NumberFormat rewardFormat = NumberFormat.getInstance();
        return new ItemBuilder(mission.getItemMaterial())
            .name(Txt.parse(mission.getRarity().getColor() + mission.getItemName()))
            .withLore(MUtil.list(
                Txt.parse("<n>Rarity: %s%s", mission.getRarity().getColor(), mission.getRarity().getDisplayName()),
                Txt.parse("<n>Challenge: <k>%s", mission.getDescription()),
                "",
                Txt.parse("<n>Reward: <k>%s", rewardFormat.format(mission.getReward()) + " Credits")))
            .flag(ItemFlag.HIDE_ATTRIBUTES);
    }

    @Override
    protected void onPick(Player player, Mission mission)
    {
        // Read-only reference list.
    }
}
