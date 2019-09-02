package com.amrdeveloper.tasktimer;

import java.util.Locale;

public class TimeUtils {

    public static String formatSecondsToTime(long totalSeconds) {
        short SECOND_IN_HOUR = 3600;
        short SECOND_IN_MINUTE = 60;
        long hours = 0;
        short minutes = 0;

        if (totalSeconds >= SECOND_IN_HOUR) {
            long round = totalSeconds % SECOND_IN_HOUR;
            hours = (totalSeconds - round) / SECOND_IN_HOUR;
            totalSeconds = totalSeconds - (totalSeconds - round);
        }

        if (totalSeconds >= SECOND_IN_MINUTE) {
            long round = totalSeconds % SECOND_IN_MINUTE;
            minutes = (short)((totalSeconds - round) / SECOND_IN_MINUTE);
            totalSeconds = totalSeconds - (totalSeconds - round);
        }

        return String.format(Locale.ENGLISH,"%d : %d : %d", hours, minutes, totalSeconds);
    }
}
