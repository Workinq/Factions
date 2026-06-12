package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsTnt extends FactionsCommand
{
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdFactionsTntBalance cmdFactionsTntBalance = new CmdFactionsTntBalance();
    public CmdFactionsTntDeposit cmdFactionsTntDeposit = new CmdFactionsTntDeposit();
    public CmdFactionsTntSet cmdFactionsTntSet = new CmdFactionsTntSet();
    public CmdFactionsTntWithdraw cmdFactionsTntWithdraw = new CmdFactionsTntWithdraw();
    public CmdFactionsTntFill cmdFactionsTntFill = new CmdFactionsTntFill();
    public CmdFactionsTntUnfill cmdFactionsTntUnfill = new CmdFactionsTntUnfill();

}
