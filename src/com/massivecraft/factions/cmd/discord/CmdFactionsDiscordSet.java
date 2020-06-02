package com.massivecraft.factions.cmd.discord;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsDiscordSet extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    // private final Pattern pattern = Pattern.compile("(https?://)?(www\\.)?(discord\\.(gg|io|me|li)|discordapp\\.com/invite)/.+[a-z]");

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsDiscordSet()
    {
        // Parameters
        this.addParameter(TypeString.get(), "discord");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MPerm.getPermDiscord().has(msender, msenderFaction, true)) return;

        // Args
        String discord = this.readArg();

        // Verify
        /*Matcher matcher = pattern.matcher(discord);
        if (!matcher.matches())
        {
            msender.msg("<b>Invalid discord link, make sure you're inputting the full link.");
            return;
        }*/

        // Apply
        msenderFaction.setDiscord(discord);

        // Inform
        msenderFaction.msg("%s <i>set the faction discord to <a>%s<i>.", msender.describeTo(msenderFaction, true), discord);
    }

}
