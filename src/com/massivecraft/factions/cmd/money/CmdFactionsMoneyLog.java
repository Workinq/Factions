package com.massivecraft.factions.cmd.money;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqBankCommandsEnabled;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.object.FactionMute;
import com.massivecraft.factions.entity.object.FactionMoneyLog;
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

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;

public class CmdFactionsMoneyLog extends FactionsCommand {

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsMoneyLog() {
        this.addAliases("logs");

        // Parameters
        this.addParameter(Parameter.getPage());
        this.addParameter(TypeFaction.get(), "faction", "you");

        // Requirements
        this.addRequirements(ReqBankCommandsEnabled.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        int page = this.readArg();
        Faction faction = this.readArg(msenderFaction);
        if (faction != msenderFaction && ! Perm.MONEY_LOG_ANY.has(sender, true)) return;

        // Pager Create
        final List<FactionMoneyLog> moneyLogs = new MassiveList<>(faction.getMoneyLog());

        moneyLogs.sort((i1, i2) -> ComparatorSmart.get().compare(i2.getCreationMillis(), i1.getCreationMillis()));

        final long now = System.currentTimeMillis();

        final Pager<FactionMoneyLog> pager = new Pager<>(this, "Money transactions", page, moneyLogs, (Stringifier<FactionMoneyLog>) (moneyLog, index) -> {
            final String playerId = moneyLog.getPlayerId();
            final double amount = moneyLog.getAmount();
            final String type = moneyLog.getType();

            final String playerDisplayName = MixinDisplayName.get().getDisplayName(playerId, sender);

            String ageDesc;
            long millis = now - moneyLog.getCreationMillis();
            LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
            ageDesc = TimeDiffUtil.formatedMinimal(ageUnitcounts, "<i>");
            ageDesc = " " + ageDesc + Txt.parse(" ago");

            final DecimalFormat formatter = new DecimalFormat("#,###");
            return Txt.parse("%s<i> %s <white>$%s<reset>%s<i>.", playerDisplayName,type,formatter.format(amount), ageDesc);
        });

        // Pager Message
        pager.message();
    }
}
