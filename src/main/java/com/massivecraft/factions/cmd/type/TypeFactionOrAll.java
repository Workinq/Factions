package com.massivecraft.factions.cmd.type;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Set;

/**
 * Reads a {@link Faction}, or {@code null} for the tokens "all"/"*"/"global"/"any" (meaning the
 * whole-server scope in the admin audit command). Delegates everything else to {@link TypeFaction}.
 */
public class TypeFactionOrAll extends TypeAbstract<Faction>
{
	private static TypeFactionOrAll i = new TypeFactionOrAll();
	public static TypeFactionOrAll get() { return i; }
	public TypeFactionOrAll() { super(Faction.class); }

	public static final Set<String> ALL_TOKENS = MUtil.set("all", "*", "global", "any");

	@Override
	public Faction read(String str, CommandSender sender) throws MassiveException
	{
		if (str != null && ALL_TOKENS.contains(str.toLowerCase())) return null; // null = global scope
		return TypeFaction.get().read(str, sender);
	}

	@Override
	public String getNameInner(Faction value)
	{
		return value == null ? "all" : TypeFaction.get().getNameInner(value);
	}

	@Override
	public String getVisualInner(Faction value, CommandSender sender)
	{
		return value == null ? "all" : TypeFaction.get().getVisualInner(value, sender);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Collection<String> ret = TypeFaction.get().getTabList(sender, arg);
		ret.add("all");
		return ret;
	}
}
