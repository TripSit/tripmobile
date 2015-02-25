package me.tripsit.mobile.common;

import me.tripsit.mobile.R;

/**
 * Created by eddie on 05/02/15.
 */
public enum Theme {
    DARK(1, R.style.DarkTheme, "CLI"),
    LIGHT(2, R.style.LightTheme, "Relaxed");

    private final int id;
    private final int rStyleId;
    private final String kiwiTheme;

    public static final Theme DEFAULT = DARK;

    private Theme(int id, int rStyleId, String kiwiTheme) {
        this.id = id;
        this.rStyleId = rStyleId;
        this.kiwiTheme = kiwiTheme;
    }

    public int getId() {
        return id;
    }

    public int getStyleId() {
        return rStyleId;
    }

    public String getKiwiTheme() {
        return kiwiTheme;
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
