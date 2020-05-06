package com.massivecraft.factions.entity.object;

import com.massivecraft.factions.entity.MPerm;
import org.bukkit.Material;

public class FactionPermission
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String mPerm;
    private final Material itemMaterial;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public FactionPermission(String mPerm, Material itemMaterial)
    {
        this.mPerm = mPerm;
        this.itemMaterial = itemMaterial;
    }

    public MPerm getMPerm()
    {
        return MPerm.get(this.mPerm);
    }

    public String getMPermString()
    {
        return this.mPerm;
    }

    public Material getItemMaterial()
    {
        return itemMaterial;
    }

}
