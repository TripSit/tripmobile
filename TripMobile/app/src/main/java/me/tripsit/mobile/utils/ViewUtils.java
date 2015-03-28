package me.tripsit.mobile.utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by eddie on 28/03/15.
 */
public class ViewUtils {

    private ViewUtils() {
        // Static methods only
    }

    public static void hideViewsWithId(Activity activity, int... ids) {
        updatedViews(activity, View.GONE, ids);
    }

    public static void showViewsWithId(Activity activity, int... ids) {
        updatedViews(activity, View.VISIBLE, ids);
    }

    private static void updatedViews(Activity activity, int visibility, int... ids) {
        for (int id : ids) {
            View view = activity.findViewById(id);
            if (view != null) {
                view.setVisibility(visibility);
            }
        }
    }
}
