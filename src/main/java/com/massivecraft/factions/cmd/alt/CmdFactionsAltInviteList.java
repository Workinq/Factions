package com.massivecraft.factions.cmd.alt;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.Invitation;
import com.massivecraft.factions.entity.MPerm;
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

import java.util.*;

public class CmdFactionsAltInviteList extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsAltInviteList()
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

        if ( faction != msenderFaction && ! Perm.ALT_INVITELIST_OTHERS.has(sender, true)) return;

        // MPerm
        if ( ! MPerm.getPermAlt().has(msender, msenderFaction, true)) return;

        // Pager Create
        final List<Map.Entry<String, Invitation>> invitations = new MassiveList<>(faction.getInvitations().entrySet());

        // Sort
        invitations.removeIf(entry -> !entry.getValue().isAlt());

        Collections.sort(invitations, new Comparator<Map.Entry<String, Invitation>>()
        {
            @Override
            public int compare(Map.Entry<String, Invitation> i1, Map.Entry<String, Invitation> i2)
            {
                return ComparatorSmart.get().compare(i2.getValue().getCreationMillis(), i1.getValue().getCreationMillis());
            }
        });

        final long now = System.currentTimeMillis();

        final Pager<Map.Entry<String, Invitation>> pager = new Pager<>(this, "Invited Alts List", page, invitations, new Stringifier<Map.Entry<String, Invitation>>()
        {
            @Override
            public String toString(Map.Entry<String, Invitation> entry, int index)
            {
                String inviteeId = entry.getKey();
                String inviterId = entry.getValue().getInviterId();

                String inviteeDisplayName = MixinDisplayName.get().getDisplayName(inviteeId, sender);
                String inviterDisplayName = inviterId != null ? MixinDisplayName.get().getDisplayName(inviterId, sender) : Txt.parse("<silver>unknown");

                String ageDesc = "";
                if (entry.getValue().getCreationMillis() != null)
                {
                    long millis = now - entry.getValue().getCreationMillis();
                    LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
                    ageDesc = TimeDiffUtil.formatedMinimal(ageUnitcounts, "<i>");
                    ageDesc = " " + ageDesc + Txt.parse(" ago");
                }

                return Txt.parse("%s<i> was invited by %s<reset>%s<i>.", inviteeDisplayName, inviterDisplayName, ageDesc);
            }
        });

        // Pager Message
        pager.message();
    }

}
