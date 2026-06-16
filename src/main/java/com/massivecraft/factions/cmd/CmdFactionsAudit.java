package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.MassiveException;

import java.util.Collections;

public class CmdFactionsAudit extends FactionsCommand
{
	public CmdFactionsAuditList cmdFactionsAuditList = new CmdFactionsAuditList();
	public CmdFactionsAuditGui cmdFactionsAuditGui = new CmdFactionsAuditGui();

	public CmdFactionsAudit()
	{
		this.addAliases("log");
	}

	@Override
	public void perform() throws MassiveException
	{
		this.cmdFactionsAuditGui.execute(this.sender, Collections.emptyList());
	}
}
