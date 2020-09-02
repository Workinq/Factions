package com.massivecraft.factions.adapter;

import com.massivecraft.factions.Chat;
import com.massivecraft.factions.cmd.type.TypeChat;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;

import java.lang.reflect.Type;

public class ChatAdapter implements JsonDeserializer<Chat>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static ChatAdapter i = new ChatAdapter();
    public static ChatAdapter get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Chat deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        try
        {
            return TypeChat.get().read(json.getAsString());
        }
        catch (MassiveException e)
        {
            return null;
        }
    }

}
