package me.tripsit.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.tripsit.mobile.common.SharedPreferencesManager;

/**
 * Created by eddie on 08/02/15.
 */
public abstract class TripMobileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(SharedPreferencesManager.getTheme(this).getStyleId());
        setContentView(getLayoutId());
    }

    protected void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public abstract int getLayoutId();
}
