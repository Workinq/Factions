package com.massivecraft.factions.entity.migrator;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.store.migrator.MigratorFieldConvert;
import com.massivecraft.massivecore.store.migrator.MigratorFieldRename;
import com.massivecraft.massivecore.store.migrator.MigratorRoot;
import com.massivecraft.massivecore.xlib.gson.JsonArray;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonPrimitive;

public class MigratorFaction003ChestPages extends MigratorRoot
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static MigratorFaction003ChestPages i = new MigratorFaction003ChestPages();
	public static MigratorFaction003ChestPages get() { return i; }
	private MigratorFaction003ChestPages()
	{
		super(Faction.class);
		this.addInnerMigrator(MigratorFieldRename.get("inventorySerialized", "chestsSerialized"));
		this.addInnerMigrator(new StringToList());
	}

	// -------------------------------------------- //
	// INNER: SINGLE STRING -> ONE ELEMENT LIST
	// -------------------------------------------- //

	// Wraps the old single-chest base64 string into page one of the new chest list.
	public static class StringToList extends MigratorFieldConvert
	{
		private StringToList()
		{
			super("chestsSerialized");
		}

		@Override
		public Object migrateInner(JsonElement element)
		{
			JsonArray ret = new JsonArray();
			if (element != null && element.isJsonPrimitive())
			{
				String serialized = element.getAsString();
				if (!serialized.isEmpty()) ret.add(new JsonPrimitive(serialized));
			}
			return ret;
		}
	}

}
