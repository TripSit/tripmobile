package me.tripsit.mobile.common;

import android.app.Activity;

import me.tripsit.mobile.R;

/**
 * Created by eddie on 05/02/15.
 */
public enum Theme {
    DARK(1, R.style.DarkTheme),
    LIGHT(2, R.style.LightTheme);

    private int id;
    private int rStyleId;

    public static final Theme DEFAULT = DARK;

    private Theme(int id, int rStyleId) {
        this.id = id;
        this.rStyleId = rStyleId;
    }

    public int getId() {
        return id;
    }

    public int getStyleId() {
        return rStyleId;
    }

    public static Theme forId(int id) {
        for (Theme theme : values()) {
            if (id == theme.id) {
                return theme;
            }
        }
        return DEFAULT;
    }
}
