package com.massivecraft.factions.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static String formatPlayTime(long playTime)
    {
        String output = "";
        long days = TimeUnit.MILLISECONDS.toDays(playTime);
        long millis = playTime - TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (days > 1L)
        {
            output = output + days + " days ";
        }
        else if (days == 1L)
        {
            output = output + days + " day ";
        }
        if (hours > 1L)
        {
            output = output + hours + " hours ";
        }
        else if (hours == 1L)
        {
            output = output + hours + " hour ";
        }
        if (minutes > 1L)
        {
            output = output + minutes + " minutes ";
        }
        else if (minutes == 1L)
        {
            output = output + minutes + " minute ";
        }
        if (seconds > 1L)
        {
            output = output + seconds + " seconds ";
        }
        else if (seconds == 1L)
        {
            output = output + seconds + " second ";
        }
        return output.isEmpty() ? "0 seconds " : output;
    }

    public static String formatTime(long timePeriod, boolean showSeconds)
    {
        // Args - Output
        StringBuilder builder = new StringBuilder();

        // Args - Time
        long days = TimeUnit.MILLISECONDS.toDays(timePeriod);
        long millis = timePeriod - TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        if (days > 1L) // Days
        {
            builder.append(days).append(" days ");
        }
        else if (days == 1L)
        {
            builder.append(days).append(" day ");
        }

        if (hours > 1L) // Hours
        {
            builder.append(hours).append(" hours ");
        }
        else if (hours == 1L)
        {
            builder.append(hours).append(" hour ");
        }

        if (minutes > 1L) // Minutes
        {
            builder.append(minutes).append(" minutes ");
        }
        else if (minutes == 1L)
        {
            builder.append(minutes).append(" minute ");
        }

        // Verify - Seconds
        if (showSeconds)
        {
            if (seconds > 1L) // Seconds
            {
                builder.append(seconds).append(" seconds ");
            }
            else if (seconds == 1L)
            {
                builder.append(seconds).append(" second ");
            }
        }

        // Return
        return builder.length() == 0 ? "just now" : builder.toString().trim();
    }

    public static String formatTime(int seconds)
    {
        int days = seconds / 86400;
        int hours = seconds % 86400 / 3600;
        int minutes = seconds % 86400 % 3600 / 60;
        StringBuilder sb = new StringBuilder();
        if (days != 0)
        {
            if (days > 1)
            {
                sb.append(days).append(" days ");
            }
            else if (days == 1)
            {
                sb.append("1 day ");
            }
        }
        if (hours != 0)
        {
            if (hours > 1)
            {
                sb.append(hours).append(" hours ");
            }
            else if (hours == 1)
            {
                sb.append("1 hour ");
            }
        }
        if (minutes != 0)
        {
            if (minutes > 1)
            {
                sb.append(minutes).append(" minutes ");
            }
            else if (minutes == 1)
            {
                sb.append("1 minute ");
            }
        }
        return sb.toString().isEmpty() ? "just now " : sb.toString();
    }

}
