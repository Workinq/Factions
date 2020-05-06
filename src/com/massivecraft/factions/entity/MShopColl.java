package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MShopColl extends Coll<MShop>
{

    private static final MShopColl i = new MShopColl();
    public static MShopColl get() { return i; }

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

        MShop.i = this.get(MassiveCore.INSTANCE, true);
    }

}
