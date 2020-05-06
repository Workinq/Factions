package com.massivecraft.factions.cmd.shard;

import com.massivecraft.factions.cmd.FactionsCommand;

public class CmdFactionsShards extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsShardsBalance cmdMoneyBalance = new CmdFactionsShardsBalance();
	public CmdFactionsShardsShop cmdFactionsShardsShop = new CmdFactionsShardsShop();
	public CmdFactionsShardsAdd cmdFactionsShardsAdd = new CmdFactionsShardsAdd();

}
