package com.massivecraft.factions.cmd.paypal;

import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdFactionsPaypalSet extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private Pattern pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdFactionsPaypalSet()
    {
        // Parameters
        this.addParameter(TypeString.get(), "paypal");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException
    {
        if ( ! MPerm.getPermPaypal().has(msender, msenderFaction, true)) return;

        // Args
        String paypal = this.readArg();

        // Verify
        Matcher matcher = pattern.matcher(paypal);
        if (!matcher.matches())
        {
            msender.msg("<b>Invalid email address entered.");
            return;
        }

        // Apply
        msenderFaction.setPaypal(paypal);

        // Inform
        msender.msg("%s <i>set the faction paypal to <a>%s<i>.", msender.describeTo(msender, true), paypal);
    }

}
