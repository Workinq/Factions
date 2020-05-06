package com.massivecraft.factions.cmd.strike;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.FactionStrike;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class CmdFactionsStrikeList extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsStrikeList()
    {
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

        if ( faction != msenderFaction && ! Perm.STRIKE_CHECK_OTHER.has(sender, true)) return;

        // Pager Create
        final List<FactionStrike> strikes = new MassiveList<>(faction.getStrikes());

        // Sort
        Collections.sort(strikes, new Comparator<FactionStrike>()
        {
            @Override
            public int compare(FactionStrike s1, FactionStrike s2)
            {
                return ComparatorSmart.get().compare(s2.getCreationMillis(), s1.getCreationMillis());
            }
        });

        final long now = System.currentTimeMillis();

        final Pager<FactionStrike> pager = new Pager<>(this, "Invited Players List", page, strikes, new Stringifier<FactionStrike>()
        {
            @Override
            public String toString(FactionStrike factionStrike, int index)
            {
                String strikerId = factionStrike.getIssuedBy();

                String issuerName = strikerId != null ? MixinDisplayName.get().getDisplayName(strikerId, sender) : Txt.parse("<silver>unknown");

                String strikeId = factionStrike.getStrikeId();
                String reason = factionStrike.getMessage();
                String strikeDesc = "";
                long millis = now - factionStrike.getCreationMillis();
                LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
                strikeDesc = TimeDiffUtil.formatedMinimal(ageUnitcounts, "<i>");
                strikeDesc = " " + strikeDesc + Txt.parse(" ago");

                return Txt.parse("<n>[<gray>%s<n>] <i>Issued<reset>%s <i>by %s <i>for <h>%s<i>.", strikeId, strikeDesc, issuerName, reason);
            }
        });

        // Pager Message
        pager.message();
        msg("%s <i>has a total of <h>%d <i>point" + (faction.getStrikes().size() > 1 ? "s" : "") + ".", faction.describeTo(msender), faction.getStrikePoints());
    }

}
