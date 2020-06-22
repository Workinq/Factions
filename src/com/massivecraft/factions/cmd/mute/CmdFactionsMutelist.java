package com.massivecraft.factions.cmd.mute;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.object.FactionMute;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class CmdFactionsMutelist extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsMutelist()
    {
        // Aliases
        this.addAliases("mutes");

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

        if ( faction != msenderFaction && ! Perm.MUTELIST_OTHER.has(sender, true)) return;

        // MPerm
        if ( ! MPerm.getPermMute().has(msender, msenderFaction, true)) return;

        // Pager Create
        final List<FactionMute> mutedMembers = new MassiveList<>(faction.getMutedMembers());
        Collections.sort(mutedMembers, (i1, i2) -> ComparatorSmart.get().compare(i2.getCreationMillis(), i1.getCreationMillis()));
        final long now = System.currentTimeMillis();

        final Pager<FactionMute> pager = new Pager<>(this, "Muted Members", page, mutedMembers, new Stringifier<FactionMute>()
        {
            @Override
            public String toString(FactionMute factionMute, int index)
            {
                String mutedId = factionMute.getMutedId();
                String muterId = factionMute.getMuterId();

                String mutedDisplayName = MixinDisplayName.get().getDisplayName(mutedId, sender);
                String muterDisplayName = muterId != null ? MixinDisplayName.get().getDisplayName(muterId, sender) : Txt.parse("<silver>unknown");

                String ageDesc = "";
                long millis = now - factionMute.getCreationMillis();
                LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
                ageDesc = TimeDiffUtil.formatedMinimal(ageUnitcounts, "<i>");
                ageDesc = " " + ageDesc + Txt.parse(" ago");

                return Txt.parse("%s<i> was muted by %s<reset>%s<i>.", mutedDisplayName, muterDisplayName, ageDesc);
            }
        });

        // Pager Message
        pager.message();
    }

}
