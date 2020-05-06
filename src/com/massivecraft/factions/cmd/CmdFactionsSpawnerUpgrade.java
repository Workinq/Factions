package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsSpawnerUpgrade;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.util.BlockIterator;

public class CmdFactionsSpawnerUpgrade extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSpawnerUpgrade()
    {
        this.addRequirements(RequirementIsPlayer.get());
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        BlockIterator iterator = new BlockIterator(me, 5);
        if (! iterator.hasNext())
        {
            msg("<b>You must be looking at a spawner in order to upgrade it.");
            return;
        }

        Block lastBlock = iterator.next(); // Get block player is looking at.
        while (iterator.hasNext())
        {
            lastBlock = iterator.next();
            if (lastBlock.getType() == Material.AIR) continue;
            break;
        }

        if (lastBlock == null || lastBlock.getType() != Material.MOB_SPAWNER) // No block was found.
        {
            msg("<b>You must be looking at a spawner in order to upgrade it.");
            return;
        }

        Faction at = BoardColl.get().getFactionAt(PS.valueOf(lastBlock));
        if ( ! MPerm.getPermSpawnerupgrade().has(msender, at, true)) return;

        BlockState blockState = lastBlock.getState();
        if (blockState == null)
        {
            msg("<b>You must be looking at a spawner in order to upgrade it.");
            return;
        }

        if (!(blockState instanceof CreatureSpawner))
        {
            msg("<b>You must be looking at a spawner in order to upgrade it.");
            return;
        }

        CreatureSpawner spawner = (CreatureSpawner) blockState;
        if (spawner.getSpawnedType() != EntityType.PIG_ZOMBIE)
        {
            msg("<b>You can only upgrade zombie pigman spawners.");
            return;
        }

        // Event
        EventFactionsSpawnerUpgrade event = new EventFactionsSpawnerUpgrade(sender, msenderFaction, spawner);
        event.run();
        if (event.isCancelled()) return;

        // Apply
        spawner.setSpawnedType(EntityType.SILVERFISH);
        spawner.update();

        // Inform
        msg("<i>Successfully upgraded the spawner to silverfish for <h>%s <i>.", Money.format(MConf.get().econCostSpawnerUpgrade));
    }

}
