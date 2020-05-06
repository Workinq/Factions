package com.massivecraft.factions.cmd.type;

import com.massivecraft.factions.Chat;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;

import java.util.Set;

public class TypeChat extends TypeEnum<Chat>
{

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeChat i = new TypeChat();
    public static TypeChat get() { return i; }
    public TypeChat() { super(Chat.class); }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getName()
    {
        return "chat";
    }

    @Override
    public String getNameInner(Chat value)
    {
        return value.getName();
    }

    @Override
    public Set<String> getNamesInner(Chat value)
    {
        return value.getNames();
    }

}
