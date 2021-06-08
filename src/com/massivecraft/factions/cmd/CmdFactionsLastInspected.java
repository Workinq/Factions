package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.integration.coreprotect.IntegrationCoreProtect;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.pager.Msonifier;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CmdFactionsLastInspected extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsLastInspected()
    {
        // Aliases
        this.addAliases("li");

        // Requirements
        this.addRequirements(ReqHasFaction.get());
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! IntegrationCoreProtect.get().isActive())
        {
            throw new MassiveException().setMsg("<b>Inspecting faction land is currently disabled.");
        }

        if ( ! MPerm.getPermInspect().has(msender, msenderFaction, true)) return;

        String data = msender.getLastInspected();

        if (data == null)
        {
            throw new MassiveException().addMsg("<b>You haven't inspected any blocks recently.");
        }

        if ( ! data.contains("\n") )
        {
            throw new MassiveException().addMsg("<b>No data was found for that block.");
        }

        List<Mson> inspectData = new ArrayList<>();
        String[] blockData = data.split("\n");
        for (String blockDatum : blockData)
        {
            String info = ChatColor.stripColor(blockDatum);

            if (info.contains("older data by typing") || info.contains("-----") || info.contains("CoreProtect")) continue;

            // Format: TIME - ACTION (e.g. 0.01/h ago - Kieraaaan placed cobblestone.)
            String[] dataSplit = info.split("-");
            String time = dataSplit[0];
            String actionString = dataSplit[1].replace(".", "");
            String[] actionSplit = actionString.split(" ");
            String player = ChatColor.stripColor(actionSplit[1]);
            String action = ChatColor.stripColor(actionSplit[2]);
            String material = ChatColor.stripColor(actionSplit[3]);

            info = Txt.parse("<a>%s <i>%s <a>%s <n>%s", player, action, material, time);
            inspectData.add(Mson.mson(info));
        }
        final Pager<Mson> pager = new Pager<>(CmdFactions.get().cmdFactionsInspect, "Inspect Log", 1, inspectData, (Msonifier<Mson>) (item, index) -> inspectData.get(index));
        pager.setSender(msender.getSender());

        // Send pager
        pager.message();
    }

}
