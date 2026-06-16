package com.massivecraft.factions.gui;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.type.StandardGui;
import com.massivecraft.massivecore.mixin.MixinCommand;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PermEditMenu extends StandardGui
{
    private final MPlayer mplayer;
    private final Faction faction;
    private final MPerm mperm;

    public PermEditMenu(Player player, MPlayer mplayer, MPerm mperm)
    {
        super(player, 3, Txt.parse("<gray>Editing %s", mperm.getName()));
        this.mplayer = mplayer;
        this.faction = mplayer.getFaction();
        this.mperm = mperm;
    }

    @Override
    protected void build()
    {
        fillBorder();

        int slot = 9;
        for (Rel rel : Rel.values())
        {
            boolean status = faction.isPermitted(mperm, rel);
            ItemBuilder icon = new ItemBuilder(status ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                .name((status ? ChatColor.GREEN : ChatColor.RED) + rel.getName());

            if (rel == Rel.LEADER)
            {
                set(slot, icon.withLore(Txt.parse(MUtil.list("<n>You cannot modify permissions", "<n>for <g>your faction leader<n>."))));
            }
            else if (rel.isMoreThan(mplayer.getRole()))
            {
                set(slot, icon.withLore(Txt.parse(MUtil.list("<b>You can't modify permissions for", "<b>roles higher than yours."))));
            }
            else if (status)
            {
                button(slot, icon.withLore(Txt.parse(MUtil.list("<n>Click to <b>deny <n>access to this permission"))), ctx -> toggle(rel, false));
            }
            else
            {
                button(slot, icon.withLore(Txt.parse(MUtil.list("<n>Click to <g>allow <n>access to this permission"))), ctx -> toggle(rel, true));
            }

            slot++;
        }

        button(22, new ItemBuilder(Material.ARROW).name(Txt.parse("<i>Back")), ctx -> new PermListMenu(ctx.getPlayer(), mplayer).open());
    }

    private void toggle(Rel rel, boolean newStatus)
    {
        // Verify
        if (rel.isMoreThan(mplayer.getRole()))
        {
            mplayer.msg("<b>You can't modify permissions for roles higher than yours.");
            return;
        }

        // Execute
        MixinCommand.get().dispatchCommand(getPlayer(), "f perm set " + mperm.getName() + " " + rel.getName() + " " + (newStatus ? "yes" : "no"));

        // Rebuild so the toggle reflects the new state.
        clearContent();
        build();
        render();
    }
}
