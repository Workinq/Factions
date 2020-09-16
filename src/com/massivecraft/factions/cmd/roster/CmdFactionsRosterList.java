package com.massivecraft.factions.cmd.roster;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CmdFactionsRosterList extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsRosterList()
    {
        // Aliases
        this.addAliases("list");

        // Desc
        this.setDescPermission("factions.roster.list");

        // Parameters
        this.addParameter(Parameter.getPage());
        this.addParameter(TypeFaction.get(), "faction", "you");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        // Args
        int page = this.readArg();
        Faction faction = this.readArg(msenderFaction);

        // Verify
        if (faction != msenderFaction && ! Perm.ROSTER_LIST_ANY.has(sender, true)) return;

        // MPerm
        if ( ! MPerm.getPermRoster().has(msender, faction, true)) return;

        // Pager Create
        final List<Map.Entry<String, Rel>> roster = new MassiveList<>(faction.getRoster().entrySet());

        // Sort
        roster.sort((role1, role2) -> ComparatorSmart.get().compare(role2.getValue().getValue(), role1.getValue().getValue()));

        final Pager<Map.Entry<String, Rel>> pager = new Pager<>(this, "Faction Roster", page, roster, new Stringifier<Map.Entry<String, Rel>>()
        {
            @Override
            public String toString(Map.Entry<String, Rel> entry, int index)
            {
                String playerId = entry.getKey();
                Rel role = entry.getValue();
                String playerDisplayName = MixinDisplayName.get().getDisplayName(playerId, sender);

                return Txt.parse("%s<i> with the role <h>%s<i>.", playerDisplayName, role.getName());
            }
        });

        // Pager Message
        pager.message();
    }

}
