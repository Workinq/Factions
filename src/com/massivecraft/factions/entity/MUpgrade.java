package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.upgrade.ConfUpgrade;
import com.massivecraft.factions.entity.upgrade.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@EditorName("config")
public class MUpgrade extends Entity<MUpgrade>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MUpgrade i;
    public static MUpgrade get() { return i; }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public ConfUpgrade factionChestUpgrade = new ConfUpgrade(Txt.parse("Faction Chest"), Material.CHEST, 3, new String[]{"36 slots in /f chest", "45 slots in /f chest", "54 slots in /f chest"}, new String[]{"36 slots in /f chest", "45 slots in /f chest", "54 slots in /f chest"}, new Integer[]{1000000, 2000000, 3000000});
    public ConfUpgrade tntUpgrade = new ConfUpgrade(Txt.parse("TNT Storage"), Material.TNT, 10, new String[]{"50,000 tnt in /f tnt", "100,000 tnt in /f tnt", "150,000 tnt in /f tnt", "200,000 tnt in /f tnt", "250,000 tnt in /f tnt", "500,000 tnt in /f tnt", "750,000 tnt in /f tnt", "1,000,000 tnt in /f tnt", "1,500,000 tnt in /f tnt", "2,000,000 tnt in /f tnt"}, new String[]{"50,000 tnt in /f tnt", "100,000 tnt in /f tnt", "150,000 tnt in /f tnt", "200,000 tnt in /f tnt", "250,000 tnt in /f tnt", "500,000 tnt in /f tnt", "750,000 tnt in /f tnt", "1,000,000 tnt in /f tnt", "1,500,000 tnt in /f tnt", "2,000,000 tnt in /f tnt"}, new Integer[]{1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000});
    public ConfUpgrade warpUpgrade = new ConfUpgrade(Txt.parse("More Warps"), Material.ENDER_PEARL, 5, new String[]{(MConf.get().amountOfWarps + 2) + " faction warps", (MConf.get().amountOfWarps + 4) + " faction warps", (MConf.get().amountOfWarps + 6) + " faction warps", (MConf.get().amountOfWarps + 8) + " faction warps", (MConf.get().amountOfWarps + 10) + " faction warps"}, new String[]{(MConf.get().amountOfWarps + 2) + " faction warps", (MConf.get().amountOfWarps + 4) + " faction warps", (MConf.get().amountOfWarps + 6) + " faction warps", (MConf.get().amountOfWarps + 8) + " faction warps", (MConf.get().amountOfWarps + 10) + " faction warps"}, new Integer[]{1000000, 2000000, 3000000, 4000000, 5000000});
    public ConfUpgrade spawnerRate = new ConfUpgrade(Txt.parse("Spawner Rate"), Material.MOB_SPAWNER, 5, new String[]{"+2% spawner rate boost", "+4% spawner rate boost", "+6% spawner rate boost", "+8% spawner rate boost", "+10% spawner rate boost"}, new String[]{"2% spawner rate boost", "6% spawner rate boost", "12% spawner rate boost", "20% spawner rate boost", "+30% spawner rate boost"}, new Integer[]{1000000, 2000000, 3000000, 4000000, 5000000});
    public ConfUpgrade cropGrowth = new ConfUpgrade(Txt.parse("Crop Growth"), Material.CACTUS, 5, new String[]{"+2% crop growth rate", "+4% crop growth rate", "+6% crop growth rate", "+8% crop growth rate", "+10% crop growth rate"}, new String[]{"2% crop growth rate", "6% crop growth rate", "12% crop growth rate", "20% crop growth rate", "30% crop growth rate"}, new Integer[]{1000000, 2000000, 3000000, 4000000, 5000000});
    public ConfUpgrade powerboostUpgrade = new ConfUpgrade(Txt.parse("Faction Power"), Material.DIAMOND_SWORD, 5, new String[]{"100 faction power", "200 faction power", "300 faction power", "400 faction power", "500 faction power"}, new String[]{"100 faction power", "200 faction power", "300 faction power", "400 faction power", "500 faction power"}, new Integer[]{1000000, 2000000, 3000000, 4000000, 5000000});
    public ConfUpgrade sandAltUpgrade = new ConfUpgrade(Txt.parse("Sand Alts"), Material.SAND, 3, new String[]{"10 sand alts in /f sandalt", "15 sand alts in /f sandalt", "20 sand alts in /f sandalt"}, new String[]{"10 sand alts in /f sandalt", "15 sand alts in /f sandalt", "20 sand alts in /f sandalt"}, new Integer[]{500000, 1000000, 2000000});
    public transient List<AbstractUpgrade> upgrades = MUtil.list(new SpawnerRateUpgrade(), new CropGrowthUpgrade(), new FactionChestUpgrade(), new TNTStorageUpgrade(), new WarpUpgrade(), new PowerboostUpgrade(), new SandAltUpgrade());

    // -------------------------------------------- //
    // FIELD: upgrades
    // -------------------------------------------- //

    public List<AbstractUpgrade> getUpgrades()
    {
        return new ArrayList<>(upgrades);
    }

    public AbstractUpgrade getUpgradeByName(String string)
    {
        for (AbstractUpgrade upgrade : upgrades)
        {
            if (upgrade.getUpgradeName().equalsIgnoreCase(string))
            {
                return upgrade;
            }
        }
        return null;
    }

    public void increaseUpgrade(Faction faction, AbstractUpgrade upgrade)
    {
        if (faction.getLevel(upgrade.getUpgradeName()) < upgrade.getMaxLevel())
        {
            faction.increaseLevel(upgrade.getUpgradeName());
            upgrade.onUpgrade(faction);
        }
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public MUpgrade load(MUpgrade that)
    {
        super.load(that);
        return this;
    }

}
