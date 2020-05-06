package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MMissionColl extends Coll<MMission>
{

    private static final MMissionColl i = new MMissionColl();
    public static MMissionColl get() { return i; }

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

        MMission.i = this.get(MassiveCore.INSTANCE, true);
    }

}
