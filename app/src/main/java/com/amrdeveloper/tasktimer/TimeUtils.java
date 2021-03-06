package com.amrdeveloper.tasktimer;

import java.util.Locale;

public class TimeUtils {

    public static String formatSecondsToTime(long totalSeconds) {
        short SECOND_IN_HOUR = 3600;
        short SECOND_IN_MINUTE = 60;
        long hours = 0;
        byte minutes = 0;

        if (totalSeconds >= SECOND_IN_HOUR) {
            long round = totalSeconds % SECOND_IN_HOUR;
            hours = (totalSeconds - round) / SECOND_IN_HOUR;
            totalSeconds = totalSeconds - (totalSeconds - round);
        }

        if (totalSeconds >= SECOND_IN_MINUTE) {
            long round = totalSeconds % SECOND_IN_MINUTE;
            minutes = (byte)((totalSeconds - round) / SECOND_IN_MINUTE);
            totalSeconds = totalSeconds - (totalSeconds - round);
        }

        return String.format(Locale.ENGLISH,"%d : %02d : %02d", hours, minutes, totalSeconds);
    }
}
