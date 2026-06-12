package com.massivecraft.factions.action.sandalt;

import com.massivecraft.factions.cmd.CmdFactions;
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
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class ActionSandaltSpawn extends ChestActionAbstract
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final Faction faction;
    private final Player player;
    private final Location location;
    private final int maxAlts;

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public ActionSandaltSpawn(Faction faction, Player player, Location location, int maxAlts)
    {
        this.faction = faction;
        this.player = player;
        this.location = location;
        this.maxAlts = maxAlts;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean onClick(InventoryClickEvent event, Player player)
    {
        // Args
        MPlayer mplayer = MPlayer.get(player);

        // Verify - Faction
        if (BoardColl.get().getFactionAt(PS.valueOf(player)) != faction)
        {
            mplayer.msg("<b>You can only place sand alts in your own faction territory.");
            return true;
        }

        // Verify - Maximum alts
        if (faction.getSandAlts().size() + 1 > maxAlts)
        {
            mplayer.msg("%s <n>cannot spawn more sand alts as you've reached the limit. Increase this limit using <k>/f upgrade<n>.", mplayer.describeTo(mplayer, true));
            return true;
        }

        // Verify - Solid block
        if ( ! location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
        {
            mplayer.msg("<b>You must spawn sandalts above a solid block.");
            return true;
        }

        // Verify - Y location
        if (player.getLocation().getBlockY() > 254)
        {
            mplayer.msg("<b>You must be standing at Y:254 or below to spawn a sand alt.");
            return true;
        }

        // Args
        SandAlt sandAlt = new SandAlt(this.getNpc(), faction.getId(), location);

        // Apply
        faction.addSandAlt(sandAlt);

        // Inform
        mplayer.msg("%s <i>placed a sand alt at x:<h>%,d <i>y:<h>%,d <i>z:<h>%,d <n>(<h>%s<n>)", mplayer.describeTo(mplayer, true), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());

        // Open
        player.openInventory(CmdFactions.get().cmdFactionsSandAlt.cmdFactionsSandAltGui.getSandAltGui(player, mplayer, faction));

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
