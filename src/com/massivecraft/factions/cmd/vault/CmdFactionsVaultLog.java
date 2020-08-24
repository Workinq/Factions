package com.massivecraft.factions.cmd.vault;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.object.ChestAction;
import com.massivecraft.factions.util.TimeUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.pager.Msonifier;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.util.Txt;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdFactionsVaultLog extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsVaultLog()
    {
        // Aliases
        this.addAliases("log");

        // Desc
        this.setDescPermission("factions.vault.log");

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

        if ( ! MPerm.getPermVault().has(msender, faction, true)) return;

        final List<Mson> chestActions = new ArrayList<>();
        for (ChestAction chestAction : faction.getChestActions())
        {
            String date = DateFormatUtils.format(chestAction.getTimestamp(), "dd MMM yyyy HH:mm:ss");
            long timeRemaining = System.currentTimeMillis() - chestAction.getTimestamp();
            String timeSince = Txt.parse("<a>%s", TimeUtil.formatTime(timeRemaining, true));

            Mson mson = mson(Txt.parse("%s <i>%s <h>%sx %s <i>%s ago", MPlayer.get(chestAction.getPlayerId()).describeTo(msender, true), chestAction.getItem().getAmount() < 0 ? "took" : "put", Math.abs(chestAction.getItem().getAmount()), Txt.getItemName(chestAction.getItem()), timeSince));
            mson = mson.tooltip(Txt.parse("<k>%s", date));

            chestActions.add(mson);
        }

        Collections.reverse(chestActions);

        // Pager create
        final Pager<Mson> pager = new Pager<>(this, "Faction Chest Log", page, chestActions, new Msonifier<Mson>()
        {
            @Override
            public Mson toMson(Mson item, int index)
            {
                return chestActions.get(index);
            }
        });

        // Pager message
        pager.message();
    }

}
