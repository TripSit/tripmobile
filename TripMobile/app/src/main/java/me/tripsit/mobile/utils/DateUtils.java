package me.tripsit.mobile.utils;

import java.util.Date;

/**
 * Created by eddie on 28/01/15.
 */
public class DateUtils {

    public static final int MILLIS_TO_DAYS = 1000 * 60 * 60 * 24; // Millis * seconds * minutes * hours = days

    public static long getDifferenceInDays(Date mostRecent, Date leastRecent) {
        return (mostRecent.getTime() - leastRecent.getTime()) / MILLIS_TO_DAYS;
    }
}
