package com.massivecraft.factions.entity.migrator;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.store.migrator.MigratorFieldRename;
import com.massivecraft.massivecore.store.migrator.MigratorRoot;

public class MigratorMConf005AlarmSeconds extends MigratorRoot
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static MigratorMConf005AlarmSeconds i = new MigratorMConf005AlarmSeconds();
    public static MigratorMConf005AlarmSeconds get() { return i; }
    private MigratorMConf005AlarmSeconds()
    {
        super(MConf.class);
        this.addInnerMigrator(MigratorFieldRename.get("taskRingFactionAlarmMinutes", "taskRingFactionAlarmSeconds"));
    }

}
