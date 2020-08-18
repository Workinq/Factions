package com.massivecraft.factions.entity;

import com.massivecraft.factions.entity.conf.ConfMission;
import com.massivecraft.factions.entity.mission.*;
import com.massivecraft.factions.entity.mission.AbstractMission;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.text.NumberFormat;
import java.util.ArrayList;
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

    public ConfMission sugarcaneMission = new ConfMission("Sugarcane", "Harvest Sugarcane", "Harvest 25,000 Sugarcane", 25000.0D, 5000, Material.SUGAR_CANE, 0);
    public ConfMission blazeMission = new ConfMission("Blaze", "Kill Blazes", "Kill 2,500 Blazes", 2500.0D, 5000, Material.BLAZE_ROD, 0);
    public ConfMission trenchMission = new ConfMission("Trench", "Mine", "Mine 20,000 Blocks", 20000.0D, 5000, Material.DIAMOND_PICKAXE, 0);
    public ConfMission expMission = new ConfMission("EXP", "Earn EXP", "Earn 100 EXP Levels", 29315.0D, 5000, Material.EXP_BOTTLE, 0);
    public ConfMission travelMission = new ConfMission("Travel", "Travel", "Take 60,000 Steps", 60000.0D, 5000, Material.DIAMOND_BOOTS, 0);
    public ConfMission witchMission = new ConfMission("Witch", "Kill Witches", "Kill 500 Witches", 500.0D, 500, Material.POTION, 0);
    public transient List<AbstractMission> missions = MUtil.list(new MissionBlaze(), new MissionSugarcane(), new MissionTrench(), new MissionEXP(), new MissionTravel(), new MissionWitch());

    // -------------------------------------------- //
    // FIELD: missions
    // -------------------------------------------- //

    public List<AbstractMission> getMissions()
    {
        return new ArrayList<>(missions);
    }

    public AbstractMission getMissionByName(String string)
    {
        for (AbstractMission mission : missions)
        {
            if (mission.getMissionName().equalsIgnoreCase(string))
            {
                return mission;
            }
        }
        return null;
    }

    public void incrementProgress(AbstractMission mission, MPlayer mplayer) { this.incrementProgress(mission, mplayer, 1); }
    public void incrementProgress(AbstractMission mission, MPlayer mplayer, Integer amount) { this.incrementProgress(mission, mplayer.getFaction(), amount); }
    public void incrementProgress(AbstractMission mission, Faction faction) { this.incrementProgress(mission, faction, 1); }
    public void incrementProgress(AbstractMission mission, Faction faction, Integer amount)
    {
        // Verify
        if (faction.getActiveMission() == null) return;
        if (!faction.getActiveMission().getMissionName().equalsIgnoreCase(mission.getMissionName())) return;

        // Args
        Integer missionComplete = faction.getMissionGoal();
        if ((double) (missionComplete + amount) >= mission.getRequirement() - (double) amount)
        {
            Integer credits = mission.getReward();
            faction.addCredits(credits);
            faction.setActiveMission(null);
            faction.setMissionGoal(0);
            NumberFormat creditsFormat = NumberFormat.getInstance();
            creditsFormat.setGroupingUsed(true);
            faction.msg("%s <g>has received <h>%s <g>credits for completing the <h>%s <g>mission.", faction.describeTo(faction), creditsFormat.format(credits), mission.getMissionName());
        }
        else
        {
            faction.setMissionGoal(missionComplete + amount);
        }
    }

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
