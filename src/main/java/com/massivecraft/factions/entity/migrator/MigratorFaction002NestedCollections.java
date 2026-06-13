package com.massivecraft.factions.entity.migrator;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.store.migrator.MigratorFieldConvert;
import com.massivecraft.massivecore.store.migrator.MigratorRoot;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

public class MigratorFaction002NestedCollections extends MigratorRoot
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static MigratorFaction002NestedCollections i = new MigratorFaction002NestedCollections();
	public static MigratorFaction002NestedCollections get() { return i; }
	private MigratorFaction002NestedCollections()
	{
		super(Faction.class);
		this.addInnerMigrator(new ArrayToMap("sandAlts", "npcId"));
		this.addInnerMigrator(new ArrayToMap("strikes", "id"));
		this.addInnerMigrator(new ArrayToMap("bannedMembers", "bannedId"));
		this.addInnerMigrator(new ArrayToMap("mutedMembers", "muteId"));
	}

	// -------------------------------------------- //
	// INNER: ARRAY -> ID KEYED MAP
	// -------------------------------------------- //

	// Converts a JSON array into an object keyed by each element's keyField,
	// matching the EntityInternalMap serialisation shape.
	public static class ArrayToMap extends MigratorFieldConvert
	{
		private final String keyField;

		public ArrayToMap(String fieldName, String keyField)
		{
			super(fieldName);
			this.keyField = keyField;
		}

		@Override
		public Object migrateInner(JsonElement element)
		{
			if (element == null || element.isJsonNull()) return new JsonObject();
			if (element.isJsonObject()) return element;
			if (!element.isJsonArray()) throw new IllegalArgumentException(element.toString());

			JsonObject ret = new JsonObject();
			for (JsonElement value : element.getAsJsonArray())
			{
				JsonObject object = value.getAsJsonObject();
				JsonElement key = object.get(this.keyField);
				if (key == null || key.isJsonNull()) continue;
				ret.add(key.getAsString(), object);
			}
			return ret;
		}
	}

}
