package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.conf.ConfItem;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

@EditorName("config")
public class MShop extends Entity<MShop>
{

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MShop i;
    public static MShop get() { return i; }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public MassiveSet<ConfItem> items = new MassiveSet<>(
            new ConfItem(1000, MUtil.list("eco give %player% 1000"), Material.PAPER, "<b><bold>$1,000", 0, MUtil.list("<n>Click here to exchange 1,000", "<n>tokens into $1,000.")),
            new ConfItem(5000, MUtil.list("eco give %player% 5000"), Material.PAPER, "<b><bold>$5,000", 0, MUtil.list("<n>Click here to exchange 5,000", "<n>tokens into $5,000."))
    );

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public MShop load(MShop that)
    {
        super.load(that);
        return this;
    }

}
