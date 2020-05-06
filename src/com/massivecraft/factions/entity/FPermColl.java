package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class FPermColl extends Coll<FPerm>
{

    private static final FPermColl i = new FPermColl();
    public static FPermColl get() { return i; }

    @Override
    public void onTick()
    {
        super.onTick();
    }

    @Override
    public void setActive(boolean active)
    {
        super.setActive(active);

        if (!active) return;

        FPerm.i = this.get(MassiveCore.INSTANCE, true);
    }

}
