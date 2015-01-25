package me.tripsit.mobile.settings;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import me.tripsit.mobile.R;
import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.common.SharedPreferencesManager;

public class Settings extends Activity {

    private static final int MAX_CACHE_DURATION = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_settings, LayoutBuilder.buildParams()));
        setAppNameOnTextContent();
        setUpSeekBar();
    }

    private void setAppNameOnTextContent() {
        TextView cacheContent = (TextView) findViewById(R.id.txt_cache_content);
        cacheContent.setText(getString(R.string.str_cache_content, getString(R.string.app_name)));
    }

    private void setUpSeekBar() {
        int cacheFreshness = SharedPreferencesManager.getCacheFreshness(this);
        updateCacheFreshnessLabel(cacheFreshness);

        final Activity activity = this;

        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_cache);
        seekBar.setProgress(cacheFreshness);
        seekBar.setMax(MAX_CACHE_DURATION);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCacheFreshnessLabel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferencesManager.saveCacheFreshness(activity, seekBar.getProgress());
            }
        });
    }

    private void updateCacheFreshnessLabel(int newValue) {
        final TextView daysText = (TextView) findViewById(R.id.txt_cache_days);
        StringBuilder sb = new StringBuilder(Integer.toString(newValue));
        sb.append("\nday");
        if (newValue != 1) {
            sb.append('s');
        }
        daysText.setText(sb.toString());
    }
}
