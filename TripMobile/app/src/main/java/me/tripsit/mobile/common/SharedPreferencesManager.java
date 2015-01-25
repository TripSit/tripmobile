package me.tripsit.mobile.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import me.tripsit.mobile.R;

/**
 * Created by eddie on 25/01/15.
 */
public class SharedPreferencesManager {

    private static final int DEFAULT_FRESHNESS = 14;

    public static void saveCacheFreshness(Activity activity, int newValue) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.str_preferences_cache_freshness), newValue);
        editor.commit();
    }

    public static int getCacheFreshness(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(activity.getString(R.string.str_preferences_cache_freshness), DEFAULT_FRESHNESS);
    }
}
