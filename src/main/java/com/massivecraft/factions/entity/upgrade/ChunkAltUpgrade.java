package com.massivecraft.factions.entity.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import org.bukkit.Material;

public class ChunkAltUpgrade extends AbstractUpgrade
{

    @Override
    public int getMaxLevel()
    {
        return MUpgrade.get().chunkAltUpgrade.getMaxLevel();
    }

    @Override
    public String getUpgradeName()
    {
        return MUpgrade.get().chunkAltUpgrade.getUpgradeName();
    }

    @Override
    public String[] getCurrentDescription()
    {
        return MUpgrade.get().chunkAltUpgrade.getCurrentDescription();
    }

    @Override
    public String[] getNextDescription()
    {
        return MUpgrade.get().chunkAltUpgrade.getNextDescription();
    }

    @Override
    public Material getUpgradeItem()
    {
        return MUpgrade.get().chunkAltUpgrade.getUpgradeItem();
    }

    @Override
    public Integer[] getCost()
    {
        return MUpgrade.get().chunkAltUpgrade.getCost();
    }

    @Override
    public void onUpgrade(Faction faction)
    {
    }

}
