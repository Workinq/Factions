package com.massivecraft.factions.util;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.object.ChunkAlt;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.massivecore.ps.PS;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import net.citizensnpcs.trait.Gravity;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class AltUtil
{
    // -------------------------------------------- //
    // NPC
    // -------------------------------------------- //

    public static UUID spawnNpc(Player owner, Location location, String name)
    {
        // Args
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);

        // NPC Setup
        npc.data().set("removefromplayerlist", false);
        npc.setFlyable(true);
        npc.setProtected(true);
        npc.data().setPersistent("removefromplayerlist", false);

        // Skin
        SkinTrait trait = npc.getOrAddTrait(SkinTrait.class);
        trait.setSkinName(MConf.get().altSkin, true);

        // Gravity
        Gravity gravity = CitizensAPI.getTraitFactory().getTrait(Gravity.class);
        gravity.setHasGravity(true);
        npc.addTrait(gravity);

        // Owner
        Owner ownerTrait = CitizensAPI.getTraitFactory().getTrait(Owner.class);
        ownerTrait.setOwner(owner.getName(), owner.getUniqueId());
        npc.addTrait(ownerTrait);

        // Spawn
        npc.spawn(location);

        // Velocity
        if (npc.getEntity() != null)
        {
            npc.getEntity().setVelocity(npc.getEntity().getVelocity().add(new Vector(0.0, 0.42, 0.0)));
        }

        // Return
        return npc.getUniqueId();
    }

    public static void despawnNpc(UUID npcId)
    {
        NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(npcId);
        if (npc == null) return;
        npc.despawn(DespawnReason.PLUGIN);
        npc.destroy();
    }

    // -------------------------------------------- //
    // FORCE LOAD
    // -------------------------------------------- //

    // Force-load (loaded=true) or release (loaded=false) every chunk a chunk alt covers.
    // Releasing refreshes the ticket, so it's only dropped when no other alt still needs that chunk.
    public static void setLoaded(ChunkAlt alt, boolean loaded)
    {
        PS ps = alt.getPs();
        String world = ps.getWorld();
        int radius = MConf.get().chunkAltRadius;
        int ax = ps.getChunkX(true);
        int az = ps.getChunkZ(true);

        for (int dx = -radius; dx <= radius; dx++)
        {
            for (int dz = -radius; dz <= radius; dz++)
            {
                int cx = ax + dx;
                int cz = az + dz;
                if (loaded) setForceLoaded(world, cx, cz, true);
                else refresh(world, cx, cz);
            }
        }
    }

    // Drop the ticket on a sand alt's own chunk if nothing else needs it.
    public static void refreshSandAlt(SandAlt alt)
    {
        PS ps = alt.getPs();
        refresh(ps.getWorld(), ps.getChunkX(true), ps.getChunkZ(true));
    }

    public static void setForceLoaded(String world, int cx, int cz, boolean loaded)
    {
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) return;
        bukkitWorld.setChunkForceLoaded(cx, cz, loaded);
    }

    public static void refresh(String world, int cx, int cz)
    {
        setForceLoaded(world, cx, cz, isChunkNeeded(world, cx, cz));
    }

    // Does any active alt (sand or chunk) require this chunk to stay loaded?
    public static boolean isChunkNeeded(String world, int cx, int cz)
    {
        int radius = MConf.get().chunkAltRadius;
        for (Faction faction : FactionColl.get().getAll())
        {
            for (SandAlt alt : faction.getSandAlts())
            {
                if (alt.isPaused()) continue;
                PS ps = alt.getPs();
                if (world.equals(ps.getWorld()) && ps.getChunkX(true) == cx && ps.getChunkZ(true) == cz) return true;
            }
            for (ChunkAlt alt : faction.getChunkAlts())
            {
                if (alt.isPaused()) continue;
                PS ps = alt.getPs();
                if ( ! world.equals(ps.getWorld())) continue;
                if (Math.abs(cx - ps.getChunkX(true)) <= radius && Math.abs(cz - ps.getChunkZ(true)) <= radius) return true;
            }
        }
        return false;
    }

}
