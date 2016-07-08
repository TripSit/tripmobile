package me.tripsit.mobile.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import me.tripsit.mobile.Menu;
import me.tripsit.mobile.R;
import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.common.SharedPreferencesManager;
import me.tripsit.mobile.common.Theme;

public class Settings extends TripMobileActivity {

    private static final String[] CHAT_CHANNELS = new String[] {
      "home","drugs","sanctuary","psychonaut","dreaming","opiates","stims", "psychopharm","news","gaming","compsci","music"
    };
    private static final int MAX_CACHE_DURATION = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppNameOnTextContent();
        setUpSeekBar();
        setChatChannels(CHAT_CHANNELS);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    /**
     * Override onBackPressed so that we can "return" to the main menu but using a new theme if the theme setting was changed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
        finish();
    }

    public void saveChatChannel(View view) {
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_channel);
        String channel = textView.getText().toString();
        Toast toast;
        if (channel.matches("[a-zA-Z0-9]+")) {
            SharedPreferencesManager.saveChatChannel(this, channel);
            toast = Toast.makeText(getApplicationContext(), getString(R.string.chat_channel_toast, channel), Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(getApplicationContext(), getString(R.string.invalid_channel, channel), Toast.LENGTH_SHORT);
        }
        toast.show();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        textView.clearFocus();
    }

    public void setThemeLight(View view) {
        if (SharedPreferencesManager.getTheme(this) != Theme.LIGHT) {
            SharedPreferencesManager.saveTheme(this, Theme.LIGHT);
            restartActivity();
        }
    }

    public void setThemeDark(View view) {
        if (SharedPreferencesManager.getTheme(this) != Theme.DARK) {
            SharedPreferencesManager.saveTheme(this, Theme.DARK);
            restartActivity();
        }
    }

    private void setChatChannels(String[] channels) {
        Arrays.sort(channels);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, channels);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_channel);
        textView.setAdapter(adapter);
        textView.setText(SharedPreferencesManager.getChatChannel(this));
        textView.clearFocus();
    }

    private void setAppNameOnTextContent() {
        TextView cacheContent = (TextView) findViewById(R.id.txt_cache_content);
        cacheContent.setText(getString(R.string.cache_freshness_description, getString(R.string.app_name)));
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
        sb.append("\n")
        .append(getResources().getQuantityString(R.plurals.days, newValue));

        daysText.setText(sb.toString());
    }
}
