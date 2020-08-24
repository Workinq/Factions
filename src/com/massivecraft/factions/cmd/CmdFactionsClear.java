package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsClear extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsClear()
    {
        // Aliases
        this.setAliases("clear");

        // Desc
        this.setDescPermission("factions.clear");

        // Parameters
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        Faction faction = this.readArg(msenderFaction);

        // Verify - Permission
        if (faction != msenderFaction && ! Perm.CLEAR_ANY.has(sender, true)) return;

        // Verify - Faction
        if (faction.isSystemFaction())
        {
            throw new MassiveException().setMsg("%s <b>must be in a faction to mark the walls as cleared.", msender.describeTo(msender, true));
        }

        // MPerm
        if ( ! MPerm.getPermClear().has(msender, faction, true) ) return;

        // Apply
        faction.setLastCheckedMillis(System.currentTimeMillis());
        faction.setAlarmEnabled(false);

        // Inform
        String messageFaction = Txt.parse("%s <i>marked the walls as cleared.", msender.describeTo(faction, true));
        faction.msg(messageFaction);
    }

}
