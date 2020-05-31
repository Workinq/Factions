package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("config")
public class MOption extends Entity<MOption>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MOption i;
    public static MOption get() { return i; }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private boolean grace = false;
    public boolean isGrace() { return grace; }

    public void setGrace(boolean grace)
    {
        // Detect Nochange
        if (this.grace == grace) return;

        // Apply
        this.grace = grace;

        // Mark as changed
        this.changed();
    }

    private boolean flight = false;
    public boolean isFlight() { return flight; }

    public void setFlight(boolean flight)
    {
        // Detect Nochange
        if (this.flight == flight) return;

        // Apply
        this.flight = flight;

        // Mark as changed
        this.changed();
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public MOption load(MOption that)
    {
        super.load(that);
        this.setGrace(that.grace);
        this.setFlight(that.flight);
        return this;
    }

}
