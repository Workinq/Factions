package com.massivecraft.factions.entity.upgrade;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MUpgrade;
import org.bukkit.Material;

public class SandAltUpgrade extends AbstractUpgrade
{

    @Override
    public int getMaxLevel()
    {
        return MUpgrade.get().sandAltUpgrade.getMaxLevel();
    }

    @Override
    public String getUpgradeName()
    {
        return MUpgrade.get().sandAltUpgrade.getUpgradeName();
    }

    @Override
    public String[] getCurrentDescription()
    {
        return MUpgrade.get().sandAltUpgrade.getCurrentDescription();
    }

    @Override
    public String[] getNextDescription()
    {
        return MUpgrade.get().sandAltUpgrade.getNextDescription();
    }

    @Override
    public Material getUpgradeItem()
    {
        return MUpgrade.get().sandAltUpgrade.getUpgradeItem();
    }

    @Override
    public Integer[] getCost()
    {
        return MUpgrade.get().sandAltUpgrade.getCost();
    }

    @Override
    public void onUpgrade(Faction faction)
    {
    }

}
