package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.CmdFactions;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.TypeWorld;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class CmdFactionsSetAt extends CmdFactionsSetXSimple
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSetAt(boolean claim)
    {
        // Super
        super(claim);

        // Aliases
        this.addAliases("at");

        // Parameters
        this.addParameter(TypeInteger.get(), "x", "you");
        this.addParameter(TypeInteger.get(), "z", "you");
        this.addParameter(TypeWorld.get(), "world", "you");
        this.addParameter(TypeBooleanYes.get(), "map", "false");

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
        Perm perm = claim ? Perm.CLAIM_ONE : Perm.UNCLAIM_ONE;
        this.addRequirements(RequirementHasPerm.get(perm));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public Set<PS> getChunks() throws MassiveException
    {
        int chunkX = this.readArg(me.getLocation().getChunk().getX());
        int chunkZ = this.readArg(me.getLocation().getChunk().getZ());
        int abX = Math.abs(chunkX);
        int abZ = Math.abs(chunkZ);
        int acAbX = Math.abs(me.getLocation().getChunk().getX());
        int acAbZ = Math.abs(me.getLocation().getChunk().getZ());
        int difX = Math.abs(acAbX - abX);
        int difZ = Math.abs(acAbZ - abZ);
        World world = this.readArg(me.getLocation().getWorld());
        boolean sendMap = this.readArg(false);

        if (this.isClaim() && me.getLocation().getWorld() != world)
        {
            msg("<b>You can not claim land in another world.");
            return Collections.emptySet();
        }
        if (!this.isClaim() || (difX <= MConf.get().maximumClaimDistance && difZ <= MConf.get().maximumClaimDistance))
        {
            PS chunk = PS.valueOf(chunkX, chunkZ).withWorld(world.getName()).getChunk(true);
            if (sendMap)
            {
                new BukkitRunnable()
                {
                    CommandSender sender = CmdFactionsSetAt.this.sender;

                    @Override
                    public void run()
                    {
                        CmdFactions.get().cmdFactionsMap.execute(sender, new ArrayList<>());
                    }
                }.runTaskLater(Factions.get(), 1L);
            }
            return Collections.singleton(chunk);
        }
        else
        {
            msg("<b>You can only claim land up to %s chunks away.", MConf.get().maximumClaimDistance);
            return Collections.emptySet();
        }
    }
}
