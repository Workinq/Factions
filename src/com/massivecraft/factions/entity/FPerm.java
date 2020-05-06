package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.object.FactionPermission;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@EditorName("config")
public class FPerm extends Entity<FPerm>
{

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient FPerm i;
    public static FPerm get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE: ENTITY
    // -------------------------------------------- //

    @Override
    public FPerm load(FPerm that)
    {
        super.load(that);
        this.setupPermissions();
        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public String permissionGuiName = "<gray>Faction Permissions";
    public List<FactionPermission> factionPermissions = new ArrayList<>();

    public void setupPermissions()
    {
        if ( ! factionPermissions.isEmpty()) return;

        for (MPerm mPerm : MPermColl.get().getAll())
        {
            factionPermissions.add(new FactionPermission(mPerm.getName(), Material.ENCHANTED_BOOK));
        }
    }

}
