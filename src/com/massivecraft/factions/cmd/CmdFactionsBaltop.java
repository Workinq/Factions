package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.comparator.ComparatorSmart;
import com.massivecraft.massivecore.money.MoneyMixinVault;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

import java.util.ArrayList;
import java.util.Collections;
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

        // Requirements
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        int page = this.readArg();

        if ( ! MPerm.getPermBaltop().has(msender, msenderFaction, true)) return;

        // Pager Create
        final List<MPlayer> mPlayers = new MassiveList<>(msenderFaction.getMPlayers());

        Collections.sort(mPlayers, new Comparator<MPlayer>()
        {
            @Override
            public int compare(MPlayer p1, MPlayer p2)
            {
                double p1Balance = MoneyMixinVault.get().get(p1.getName());
                double p2Balance = MoneyMixinVault.get().get(p2.getName());
                return ComparatorSmart.get().compare(p2Balance, p1Balance);
            }
        });

        final Pager<MPlayer> pager = new Pager<>(this, "Faction Baltop", page, mPlayers, new Stringifier<MPlayer>()
        {
            @Override
            public String toString(MPlayer mPlayer, int index)
            {
                return Txt.parse("<n>%,d. %s <white>- <h>%s", index + 1, mPlayer.describeTo(msender, true), MoneyMixinVault.get().format(MoneyMixinVault.get().get(mPlayer.getName()), true));
            }
        });

        // Pager Message
        pager.message();
    }

}
