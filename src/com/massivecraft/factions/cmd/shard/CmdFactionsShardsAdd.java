package com.massivecraft.factions.cmd.shard;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdFactionsShardsAdd extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsShardsAddPlayer cmdFactionsShardsAddPlayer = new CmdFactionsShardsAddPlayer();
    public CmdFactionsShardsAddFaction cmdFactionsShardsAddFaction = new CmdFactionsShardsAddFaction();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsShardsAdd()
    {
        // Child
        this.addChild(this.cmdFactionsShardsAddPlayer);
        this.addChild(this.cmdFactionsShardsAddFaction);

        // Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.SHARDS_ADD));
    }

}
