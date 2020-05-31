package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class FPermColl extends Coll<FPerm>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final FPermColl i = new FPermColl();
    public static FPermColl get() { return i; }

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

        FPerm.i = this.get(MassiveCore.INSTANCE, true);
    }

}
