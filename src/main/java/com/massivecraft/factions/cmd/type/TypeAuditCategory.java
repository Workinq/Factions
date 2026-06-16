package com.massivecraft.factions.cmd.type;

import com.massivecraft.factions.entity.object.AuditCategory;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;

public class TypeAuditCategory extends TypeEnum<AuditCategory>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static TypeAuditCategory i = new TypeAuditCategory();
	public static TypeAuditCategory get() { return i; }
	public TypeAuditCategory()
	{
		super(AuditCategory.class);
	}

}
