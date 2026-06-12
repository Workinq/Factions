package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

@EditorName("config")
public class MOption extends Entity<MOption>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MOption i;
    public static MOption get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public MOption load(MOption that)
    {
        super.load(that);
        this.setGrace(that.grace);
        this.setFlight(that.flight);
        this.setShield(that.shield);
        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    // This will store if grace mode is enabled or not.
    // If this is null or false then players won't be able to set shields
    // or set base regions, etc.
    // Null means false
    private Boolean grace = Boolean.TRUE;

    // This will store whether or not faction flight is enabled on the server.
    // Null means false
    private Boolean flight = Boolean.TRUE;

    // This will store the status of shields on the server.
    // If this is null or false then shields will be ineffective.
    // Null means false
    private Boolean shield = Boolean.TRUE;

    // This will store whether or not rosters are enabled or not.
    // If this is true then players must be in the roster to join the faction.
    // Null means false
    private Boolean roster = Boolean.TRUE;

    // -------------------------------------------- //
    // FIELD: grace
    // -------------------------------------------- //

    public boolean isGrace()
    {
        if (this.grace == null) return false;
        return this.grace;
    }

    public void setGrace(Boolean grace)
    {
        // Clean input
        Boolean target = grace;
        if (MUtil.equals(target, false)) target = null;

        // Detect Nochange
        if (MUtil.equals(this.grace, target)) return;

        // Apply
        this.grace = target;

        // Mark as changed
        this.changed();
    }

    // -------------------------------------------- //
    // FIELD: flight
    // -------------------------------------------- //

    public boolean isFlight()
    {
        if (this.flight == null) return false;
        return this.flight;
    }

    public void setFlight(Boolean flight)
    {
        // Clean input
        Boolean target = flight;
        if (MUtil.equals(target, false)) target = null;

        // Detect Nochange
        if (MUtil.equals(this.flight, target)) return;

        // Apply
        this.flight = target;

        // Mark as changed
        this.changed();
    }

    // -------------------------------------------- //
    // FIELD: shield
    // -------------------------------------------- //

    public boolean isShield()
    {
        if (this.shield == null) return false;
        return this.shield;
    }

    public void setShield(Boolean shield)
    {
        // Clean input
        Boolean target = shield;
        if (MUtil.equals(target, false)) target = null;

        // Detect Nochange
        if (MUtil.equals(this.shield, target)) return;

        // Apply
        this.shield = target;

        // Mark as changed
        this.changed();
    }

    // -------------------------------------------- //
    // FIELD: roster
    // -------------------------------------------- //

    public boolean isRoster()
    {
        if (this.roster == null) return false;
        return this.roster;
    }

    public void setRoster(Boolean roster)
    {
        // Clean input
        Boolean target = roster;
        if (MUtil.equals(target, false)) target = null;

        // Detect Nochange
        if (MUtil.equals(this.roster, target)) return;

        // Apply
        this.roster = target;

        // Mark as changed
        this.changed();
    }

}
