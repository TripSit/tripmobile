package me.tripsit.mobile.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.tripsit.mobile.R;
import me.tripsit.mobile.utils.DateUtils;

/**
 * Created by eddie on 25/01/15.
 */
public class SharedPreferencesManager {

    /*
     * TODO: This class needs some good unit test coverage
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final int DEFAULT_FRESHNESS = 14;
    private static final String DEFAULT_CHANNEL = "home";

    public static void saveTheme(Activity activity, Theme theme) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.shared_preferences_theme), theme.getId());
        editor.commit();
    }

    public static Theme getTheme(Activity activity) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        int id = sharedPref.getInt(activity.getString(R.string.shared_preferences_theme), Theme.DEFAULT.getId());
        return Theme.forId(id);
    }

    public static void saveChatChannel(Activity activity, String channel) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.shared_preferences_chat_channel), channel);
        editor.commit();
    }

    public static String getChatChannel(Activity activity) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        return sharedPref.getString(activity.getString(R.string.shared_preferences_chat_channel), DEFAULT_CHANNEL);
    }

    public static void saveCacheFreshness(Activity activity, int newValue) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.shared_preferences_cache_freshness), newValue);
        editor.commit();
    }

    public static int getCacheFreshness(Activity activity) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        return sharedPref.getInt(activity.getString(R.string.shared_preferences_cache_freshness), DEFAULT_FRESHNESS);
    }

    public static String getCachedURLResponse(String url, Activity activity) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        return sharedPref.getString(getResponseKey(url, activity), null);
    }

    public static boolean isCachedDataValid(String url, Activity activity) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        String date = sharedPref.getString(getDateKey(url, activity), null);
        int cacheFreshness = getCacheFreshness(activity);
        return date != null && isDateWithinNumDays(date, cacheFreshness);
    }

    public static void saveURLResponse(String url, String response, Activity activity) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getResponseKey(url, activity), response);
        editor.putString(getDateKey(url, activity), DATE_FORMAT.format(new Date()));
        editor.commit();
    }

    public static void invalidateURLResponse(String url, Activity activity) {
        SharedPreferences sharedPref = getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getResponseKey(url, activity));
        editor.remove(getDateKey(url, activity));
        editor.commit();
    }

    private static boolean isDateWithinNumDays(String date, int numDays) {
        boolean isWithinNumDays = false;
        try {
            Date now = new Date();
            Date lastUpdated = DATE_FORMAT.parse(date);
            long difference = DateUtils.getDifferenceInDays(now, lastUpdated);
            if (difference < numDays) {
                isWithinNumDays = true;
            }
        } catch (ParseException e) {
            //TODO: log this somewhere
            e.printStackTrace();
        }
        return isWithinNumDays;
    }

    private static String getDateKey(String url, Activity activity) {
        return activity.getString(R.string.shared_preferences_date) + url;
    }

    private static String getResponseKey(String url, Activity activity) {
        return activity.getString(R.string.shared_preferences_url) + url;
    }

    private static SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getSharedPreferences(activity.getString(R.string.shared_preferences), Context.MODE_PRIVATE);
    }
}
