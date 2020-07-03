package com.massivecraft.factions.cmd.req;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementAbstract;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

public class ReqVaultIsntDamaged extends RequirementAbstract
{
    // -------------------------------------------- //
    // SERIALIZABLE
    // -------------------------------------------- //

    private static final long serialVersionUID = 1L;

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static ReqVaultIsntDamaged i = new ReqVaultIsntDamaged();
    public static ReqVaultIsntDamaged get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public boolean apply(CommandSender sender, MassiveCommand command)
    {
        if (MUtil.isntSender(sender)) return false;

        MPlayer mplayer = MPlayer.get(sender);

        if( ! mplayer.getFaction().hasVault())return false;

        return !mplayer.getFaction().getVault().isDamaged();
    }

    @Override
    public String createErrorMessage(CommandSender sender, MassiveCommand command)
    {
        return Txt.parse("<b>You must have a repaired your faction vault to %s.", getDesc(command));
    }

}