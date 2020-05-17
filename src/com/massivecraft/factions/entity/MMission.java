package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.conf.ConfMission;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

@EditorName("config")
public class MMission extends Entity<MMission>
{

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MMission i;
    public static MMission get() { return i; }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public List<ConfMission> challenges = MUtil.list(
            new ConfMission("Sugarcane", "Harvest Sugarcane", "Harvest 25,000 Sugarcane", 25000.0D, 5000, Material.SUGAR_CANE, 0),
            new ConfMission("Blaze", "Kill Blazes", "Kill 2,500 Blazes", 2500.0D, 5000, Material.BLAZE_ROD, 0),
            new ConfMission("Trench", "Mine", "Mine 20,000 Blocks", 20000.0D, 5000, Material.DIAMOND_PICKAXE, 0),
            new ConfMission("EXP", "Earn EXP", "Earn 100 EXP Levels", 29315.0D, 5000, Material.EXP_BOTTLE, 0),
            new ConfMission("Shards", "Earn Shards", "Earn 10,000 Shards", 10000.0D, 5000, Material.NETHER_STAR, 0),
            new ConfMission("Travel", "Travel", "Take 60,000 Steps", 60000.0D, 5000, Material.DIAMOND_BOOTS, 0),
            new ConfMission("Witch", "Kill Witches", "Kill 500 Witches", 500.0D, 500, Material.POTION, 0)
    );

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public MMission load(MMission that)
    {
        super.load(that);
        return this;
    }

}
