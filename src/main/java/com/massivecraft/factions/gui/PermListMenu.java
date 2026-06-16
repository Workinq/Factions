package com.massivecraft.factions.gui;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.PagedGui;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PermListMenu extends PagedGui<MPerm>
{
    private final MPlayer mplayer;
    private final Faction faction;

    public PermListMenu(Player player, MPlayer mplayer)
    {
        super(player, 6, "<gray>Faction Permissions");
        this.mplayer = mplayer;
        this.faction = mplayer.getFaction();
    }

    @Override
    protected void build()
    {
        super.build();
        button((getRows() - 1) * 9 + 4, new ItemBuilder(Material.BARRIER).name(Txt.parse("<b>Close")), ctx -> ctx.getPlayer().closeInventory());
    }

    @Override
    protected List<MPerm> items()
    {
        List<MPerm> ret = new MassiveList<>();
        for (MPerm mperm : MPerm.getAll())
        {
            if ( ! mperm.isEditable()) continue;
            ret.add(mperm);
        }
        return ret;
    }

    @Override
    protected ItemStack icon(MPerm mperm)
    {
        List<String> lore = new MassiveList<>();
        lore.add(Txt.parse("<n>Click here to modify what roles"));
        lore.add(Txt.parse("<n>have this permission"));
        lore.add("");
        lore.add(Txt.parse("<i>Currently permitted:"));
        for (Rel rel : Rel.values())
        {
            if ( ! faction.isPermitted(mperm, rel)) continue;
            lore.add(Txt.parse("<g>+ <h>%s", rel.getName()));
        }
        return new ItemBuilder(Material.ENCHANTED_BOOK).name(Txt.parse("<k>%s", mperm.getName())).withLore(lore);
    }

    @Override
    protected void onPick(Player player, MPerm mperm)
    {
        new PermEditMenu(player, mplayer, mperm).open();
    }
}
