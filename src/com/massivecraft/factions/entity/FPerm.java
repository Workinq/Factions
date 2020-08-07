package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.object.FactionPermission;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

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

    @Override @NotNull
    public FPerm load(@NotNull FPerm that)
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

        for (MPerm mperm : MPermColl.get().getAll())
        {
            factionPermissions.add(new FactionPermission(mperm.getName(), Material.ENCHANTED_BOOK));
        }
    }

}
