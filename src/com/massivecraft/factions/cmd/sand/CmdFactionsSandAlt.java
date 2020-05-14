package com.massivecraft.factions.cmd.sand;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.massivecore.MassiveException;

import java.util.ArrayList;

public class CmdFactionsSandAlt extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsSandAltGui cmdFactionsSandAltGui = new CmdFactionsSandAltGui();
    public CmdFactionsSandAltKillAll cmdFactionsSandAltKillAll = new CmdFactionsSandAltKillAll();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsSandAlt()
    {
        // Aliases
        this.addAliases("sandBot");
    }

    @Override
    public void perform() throws MassiveException
    {
        cmdFactionsSandAltGui.execute(sender, new ArrayList<>());
    }

}
