package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsChestSee extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsChestSee()
    {
        // Parameters
        this.addParameter(TypeFaction.get(), "faction");
        this.addParameter(Parameter.getPage());

        // Requirements
        this.addRequirements(RequirementIsPlayer.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        Faction faction = this.readArg();
        int page = this.readArg();
        if (page < 1) page = 1;

        if (page > faction.getChestCount())
        {
            throw new MassiveException().setMsg("<h>%s<b> only has <h>%d<b> chest%s.", faction.getName(), faction.getChestCount(), faction.getChestCount() == 1 ? "" : "s");
        }

        // Apply
        me.openInventory(faction.getChest(page));

        // Inform
        msg("<i>Viewing %s<i>'s chest <h>#%d<i>.", faction.describeTo(msender), page);
    }

}
