package com.massivecraft.factions.cmd.req;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementAbstract;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

public class ReqHasntVault extends RequirementAbstract
{
    // -------------------------------------------- //
    // SERIALIZABLE
    // -------------------------------------------- //

    private static final long serialVersionUID = 1L;

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static ReqHasntVault i = new ReqHasntVault();
    public static ReqHasntVault get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean apply(CommandSender sender, MassiveCommand command)
    {
        if (MUtil.isntSender(sender)) return false;

        MPlayer mplayer = MPlayer.get(sender);

        if (mplayer.getFaction().isSystemFaction()) return false;

        return !mplayer.getFaction().hasVault();
    }

    @Override
    public String createErrorMessage(CommandSender sender, MassiveCommand command)
    {
        return Txt.parse("<b>You must not have a vault set to %s.", getDesc(command));
    }

}
