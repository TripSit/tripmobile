package me.tripsit.mobile.combinations;

import android.app.Activity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tripsit.mobile.R;
import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.common.ErrorHandlingActivity;
import me.tripsit.mobile.error.ErrorHandler;

public class Combinations extends ErrorHandlingActivity implements CombinationsCallback, ErrorHandler {

    private Map<String, Map<String, List<String>>> combinationsMap = new HashMap<String, Map<String, List<String>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_combinations, LayoutBuilder.buildParams()));
        new CombinationsAsyncTask(this, this).execute(this);
    }

    @Override
    public void updateCombinationsMap(Map<String, Map<String, List<String>>> combinations) {
        combinationsMap.putAll(combinations);
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
