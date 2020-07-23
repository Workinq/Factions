package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.money.MoneyMixinVault;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import java.util.Comparator;
import java.util.List;

public class CmdFactionsBaltop extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsBaltop()
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
        int page = this.readArg();
        Faction faction = this.readArg(msenderFaction);

        if (faction != msenderFaction && ! Perm.BALTOP_ANY.has(sender, true)) return;

        // Pager Create
        final List<MPlayer> mplayers = new MassiveList<>(faction.getMPlayers());
        mplayers.sort(new Comparator<MPlayer>()
        {
            @Override
            public int compare(MPlayer p1, MPlayer p2)
            {
                double p1Balance = MoneyMixinVault.get().get(p1.getName());
                double p2Balance = MoneyMixinVault.get().get(p2.getName());
                return ComparatorSmart.get().compare(p2Balance, p1Balance);
            }
        });

        final Pager<MPlayer> pager = new Pager<>(this, "Faction Baltop", page, mplayers, new Stringifier<MPlayer>()
        {
            @Override
            public String toString(MPlayer mplayer, int index)
            {
                return Txt.parse("<n>%,d. %s <white>- <h>%s", index + 1, mplayer.describeTo(msender, true), MoneyMixinVault.get().format(MoneyMixinVault.get().get(mplayer.getName()), true));
            }
        });

        // Pager Message
        pager.message();
    }

}
