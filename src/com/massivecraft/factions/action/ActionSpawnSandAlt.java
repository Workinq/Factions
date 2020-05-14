package com.massivecraft.factions.action;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.SandAlt;
import com.massivecraft.massivecore.chestgui.ChestActionAbstract;
import com.massivecraft.massivecore.ps.PS;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.citizensnpcs.trait.Gravity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class ActionSpawnSandAlt extends ChestActionAbstract
{

    private final Faction faction;
    private final Player player;
    private final Location location;
    private final int maxAlts;

    public ActionSpawnSandAlt(Faction faction, Player player, Location location, int maxAlts)
    {
        this.faction = faction;
        this.player = player;
        this.location = location;
        this.maxAlts = maxAlts;
    }

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Args
        MPlayer mplayer = MPlayer.get(player);

        if (BoardColl.get().getFactionAt(PS.valueOf(player)) != faction)
        {
            mplayer.msg("<b>You can only place sand alts in your own faction territory.");
            return true;
        }

        if (faction.getSandAlts().size() + 1 > maxAlts)
        {
            mplayer.msg("%s <n>cannot spawn more sand alts as you've reached the limit. Increase this limit using <k>/f upgrade<n>.", mplayer.describeTo(mplayer, true));
            return true;
        }

        SandAlt sandAlt = new SandAlt(this.getNpc(), faction.getId(), location);

        // Apply
        faction.addSandAlt(sandAlt);

        // Inform
        mplayer.msg("%s <i>placed a sand alt at x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <n>(<h>%s<n>)", mplayer.describeTo(mplayer, true), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());

        // Return
        return true;
    }

    private UUID getNpc()
    {
        // Args
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, MConf.get().sandAltName);

        // NPC Setup
        npc.data().set("removefromplayerlist", false);
        npc.setFlyable(true);
        npc.setProtected(true);
        npc.data().setPersistent("removefromplayerlist", false);
        npc.setProtected(true);
        npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, MConf.get().altSkin);

        // Traits - Gravity
        Gravity gravity = CitizensAPI.getTraitFactory().getTrait("gravity");
        gravity.gravitate(true);
        npc.addTrait(gravity);

        // Traits - Owner
        Owner owner = CitizensAPI.getTraitFactory().getTrait("owner");
        owner.setOwner(player.getName(), player.getUniqueId());
        npc.addTrait(owner);

        // Spawn
        npc.spawn(location);

        // Skin
        if (npc.isSpawned())
        {
            SkinnableEntity skinnable = npc.getEntity() instanceof SkinnableEntity ? (SkinnableEntity) npc.getEntity() : null;
            if (skinnable != null) skinnable.setSkinName(MConf.get().altSkin);
        }

        // Velocity
        npc.getEntity().setVelocity(npc.getEntity().getVelocity().add(new Vector(0.0, 0.42, 0.0)));

        // Return
        return npc.getUniqueId();
    }

}
