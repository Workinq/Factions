package com.massivecraft.factions.cmd.discord;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsDiscordCheck extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDiscordCheck()
    {
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

        if (faction != msenderFaction && ! Perm.DISCORD_CHECK_OTHER.has(sender, true)) return;

        if ( ! MPerm.getPermDiscord().has(msender, faction, true)) return;

        String discord = faction.getDiscord();

        // Inform
        if (discord.equals(""))
        {
            msender.msg("%s <i>doesn't have a discord set.", faction.describeTo(msender));
            return;
        }
        msender.msg("%s's <i>discord is <h>%s<i>.", faction.describeTo(msender), discord);
    }

}
