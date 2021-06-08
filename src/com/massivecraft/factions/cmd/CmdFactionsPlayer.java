package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Progressbar;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerCleanInactivityToleranceMillis;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class CmdFactionsPlayer extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPlayer()
	{
		// Parameters
		this.addParameter(TypeMPlayer.get(), "player", "you");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Args
		MPlayer mplayer = this.readArg(msender);

		// INFO: Title
		message(Txt.titleize("Player " + mplayer.describeTo(msender)));

		// INFO: Power (as progress bar)
		double progressbarQuota = 0;
		double playerPowerMax = mplayer.getPowerMax();
		if (playerPowerMax != 0)
		{
			progressbarQuota = mplayer.getPower() / playerPowerMax;
		}

		int progressbarWidth = (int) Math.round(mplayer.getPowerMax() / mplayer.getPowerMaxUniversal() * 100);
		this.msg("<a>Power: <v>%s", Progressbar.HEALTHBAR_CLASSIC.withQuota(progressbarQuota).withWidth(progressbarWidth).render());
				
		// INFO: Power (as digits)
		this.msg("<a>Power: <v>%.2f / %.2f", mplayer.getPower(), mplayer.getPowerMax());
		
		// INFO: Power Boost
		if (mplayer.hasPowerBoost())
		{
			double powerBoost = mplayer.getPowerBoost();
			String powerBoostType = (powerBoost > 0 ? "bonus" : "penalty");
			this.msg("<a>Power Boost: <v>%f <i>(a manually granted %s)", powerBoost, powerBoostType);
		}
		
		// INFO: Power per Hour
		// If the player is not at maximum we wan't to display how much time left.
		
		String stringTillMax = "";
		double powerTillMax = mplayer.getPowerMax() - mplayer.getPower();
		if (powerTillMax > 0)
		{
			long millisTillMax = (long) (powerTillMax * TimeUnit.MILLIS_PER_HOUR / mplayer.getPowerPerHour());
			LinkedHashMap<TimeUnit, Long> unitcountsTillMax = TimeDiffUtil.unitcounts(millisTillMax, TimeUnit.getAllButMillis());
			unitcountsTillMax = TimeDiffUtil.limit(unitcountsTillMax, 2);
			String unitcountsTillMaxFormated = TimeDiffUtil.formatedVerboose(unitcountsTillMax, "<i>");
			stringTillMax = Txt.parse(" <i>(%s <i>left till max)", unitcountsTillMaxFormated);
		}
		
		msg("<a>Power per Hour: <v>%.2f%s", mplayer.getPowerPerHour(), stringTillMax);
		
		// INFO: Power per Death
		msg("<a>Power per Death: <v>%.2f", mplayer.getPowerPerDeath());
		
		// Display automatic kick / remove info if the system is in use
		if (MConf.get().cleanInactivityToleranceMillis <= 0) return;
		
		EventMassiveCorePlayerCleanInactivityToleranceMillis event = new EventMassiveCorePlayerCleanInactivityToleranceMillis(mplayer.getLastActivityMillis(), mplayer);
		event.run();
		msg("<i>Automatic removal after %s <i>of inactivity:", format(event.getToleranceMillis()));
		for (Entry<String, Long> causeMillis : event.getToleranceCauseMillis().entrySet())
		{
			String cause = causeMillis.getKey();
			long millis = causeMillis.getValue();
			msg("<a>%s<a>: <v>%s", cause, format(millis));
		}
	}
	
	// -------------------------------------------- //
	// TIME FORMAT
	// -------------------------------------------- //
	
	public static String format(long millis)
	{
		LinkedHashMap<TimeUnit, Long> unitcounts = TimeDiffUtil.unitcounts(millis, TimeUnit.getAllBut(TimeUnit.MILLISECOND, TimeUnit.WEEK, TimeUnit.MONTH));
		return TimeDiffUtil.formatedVerboose(unitcounts);
	}
	
}
