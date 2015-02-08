package me.tripsit.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.tripsit.mobile.common.SharedPreferencesManager;
import me.tripsit.mobile.common.Theme;

/**
 * Created by eddie on 08/02/15.
 */
public abstract class TripMobileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(SharedPreferencesManager.getTheme(this).getStyleId());
    }

    protected void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
