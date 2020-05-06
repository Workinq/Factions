package com.massivecraft.factions;

import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveSet;

import java.util.Collections;
import java.util.Set;

public enum Chat implements Named
{

    FACTION("faction", "f"),
    ALLY("ally", "a"),
    TRUCE("truce", "t"),
    PUBLIC("public", "p"),

    ;

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public int getValue() { return this.ordinal(); }

    private final Set<String> names;
    public Set<String> getNames() { return this.names; }
    @Override public String getName() { return this.getNames().iterator().next(); }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    Chat(String... names)
    {
        this.names = Collections.unmodifiableSet(new MassiveSet<>(names));
    }

    public boolean isAtLeast(Chat chat)
    {
        return this.getValue() >= chat.getValue();
    }

    public boolean isAtMost(Chat chat)
    {
        return this.getValue() <= chat.getValue();
    }

    public boolean isLessThan(Chat chat)
    {
        return this.getValue() < chat.getValue();
    }

    public boolean isMoreThan(Chat chat)
    {
        return this.getValue() > chat.getValue();
    }

    public Chat getNext()
    {
        return this == PUBLIC ? TRUCE : (this == TRUCE ? ALLY : (this == ALLY ? FACTION : PUBLIC));
    }

}
