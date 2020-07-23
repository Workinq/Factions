package com.massivecraft.factions.predicate;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.predicate.Predicate;

public class PredicateMPlayerAlt implements Predicate<MPlayer>
{

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final boolean alt;
    public boolean isAlt() { return this.alt; }

    // -------------------------------------------- //
    // INSTANCE AND CONSTRUCT
    // -------------------------------------------- //

    public static PredicateMPlayerAlt get(boolean alt) { return new PredicateMPlayerAlt(alt); }
    public PredicateMPlayerAlt(boolean alt)
    {
        this.alt = alt;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean apply(MPlayer mplayer)
    {
        if (mplayer == null) return false;
        return mplayer.isAlt();
    }

}
