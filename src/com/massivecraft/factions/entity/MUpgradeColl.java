package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MUpgradeColl extends Coll<MUpgrade>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MUpgradeColl i = new MUpgradeColl();
    public static MUpgradeColl get() { return i; }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    @Override
    public void onTick()
    {
        super.onTick();
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active)
    {
        super.setActive(active);

        if (!active) return;

        MUpgrade.i = this.get(MassiveCore.INSTANCE, true);
    }

}
