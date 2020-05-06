package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.LinkedHashSet;
import java.util.Set;

public class CmdFactionsSetLine extends CmdFactionsSetX
{

    /**
     * @author: grrocks
     * @source: https://github.com/grrocks/FactionsX/blob/master/src/com/massivecraft/factions/cmd/CmdFactionsSetLine.java
     */

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSetLine(boolean claim)
    {
        // Super
        super(claim);

        // Aliases
        this.addAliases("line");

        // Parameters
        this.addParameter(TypeInteger.get(), "amount");
        this.addParameter(null, TypeString.get(), "direction");

        // Requirements
        if (claim)
        {
            this.addParameter(TypeFaction.get(), "faction", "you");
            this.setFactionArgIndex(2);
        }
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    public Integer getRadius() throws MassiveException {
        int radius = this.readArgAt(0);

        // Radius Claim Min
        if (radius < 1)
        {
            msg("<b>If you specify an amount, it must be at least 1.");
            return null;
        }

        // Radius Claim Max
        if (radius > MConf.get().setLineMax && !msender.isOverriding())
        {
            msg("<b>The maximum amount allowed is <h>%s<b>.", MConf.get().setLineMax);
            return null;
        }

        return radius;
    }

    public BlockFace getDirection() throws MassiveException
    {
        String direction = this.readArgAt(1);
        BlockFace blockFace;

        if (direction == null)
        {
            blockFace = axis[Math.round(me.getLocation().getYaw() / 90f) & 0x3];
        }
        else if (direction.equalsIgnoreCase("north"))
        {
            blockFace = BlockFace.NORTH;
        }
        else if (direction.equalsIgnoreCase("east"))
        {
            blockFace = BlockFace.EAST;
        }
        else if (direction.equalsIgnoreCase("south"))
        {
            blockFace = BlockFace.SOUTH;
        }
        else if (direction.equalsIgnoreCase("west"))
        {
            blockFace = BlockFace.WEST;
        }
        else
        {
            msg("<h>%s <b>is not a valid direction.", direction);
            return null;
        }

        return blockFace;
    }

    @Override
    public Set<PS> getChunks() throws MassiveException
    {
        if (getDirection() == null || getRadius() == null) return null;

        final PS chunk = PS.valueOf(me.getLocation()).getChunk(true);
        final Set<PS> chunks = new LinkedHashSet<>();

        chunks.add(chunk);

        Location location = me.getLocation();

        for (int i = 0; i < getRadius() - 1; i++)
        {
            location = location.add(getDirection().getModX() * 16, 0, getDirection().getModZ() * 16);
            chunks.add(PS.valueOf(location).getChunk(true));
        }

        return chunks;
    }

}
