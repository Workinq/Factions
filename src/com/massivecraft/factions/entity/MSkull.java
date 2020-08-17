package com.massivecraft.factions.entity;

import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

@EditorName("config")
public class MSkull extends Entity<MSkull>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MSkull i;
    public static MSkull get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public MSkull load(MSkull that)
    {
        super.load(that);
        this.setTextureCache(that.textureCache);
        return this;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    // This will store textures for a player's skin so when executing the command
    // /f roster view the head textures will appear instantly.
    public MassiveMapDef<UUID, String> textureCache = new MassiveMapDef<>();

    // -------------------------------------------- //
    // FIELD: textureCache
    // -------------------------------------------- //

    public void setTextureCache(MassiveMapDef<UUID, String> textureCache)
    {
        // Apply
        this.textureCache = textureCache;

        // Mark as changed
        this.changed();
    }

    public void addSkullTexture(UUID uuid, String hash)
    {
        // Put
        textureCache.put(uuid, hash);

        // Mark as changed
        this.changed();
    }
    public void addSkullTexture(Player player, String hash) { this.addSkullTexture(player.getUniqueId(), hash); }

    public String getSkullTexture(UUID uuid) { return textureCache.getOrDefault(uuid, null); }
    public String getSkullTexture(Player player) { return this.getSkullTexture(player.getUniqueId()); }

}
