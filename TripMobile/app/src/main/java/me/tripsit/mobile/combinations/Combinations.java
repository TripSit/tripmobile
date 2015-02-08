package me.tripsit.mobile.combinations;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import me.tripsit.mobile.R;
import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.common.ErrorHandlingActivity;
import me.tripsit.mobile.error.ErrorHandler;

public class Combinations extends ErrorHandlingActivity implements CombinationsCallback, ErrorHandler {

    private enum CombinationSeverity {
        SAFE_SYNERGY("Safe & Synergy", R.id.txt_safesynergy_header, R.id.txt_safesynergy_content),
        SAFE_NO_SYNERGY("Safe & No Synergy", R.id.txt_safenosynergy_header, R.id.txt_safenosynergy_content),
        SAFE_DECREASE_SYNERGY("Safe & Decrease", R.id.txt_safedecrease_header, R.id.txt_safedecrease_content),
        UNSAFE("Unsafe", R.id.txt_unsafe_header, R.id.txt_unsafe_content),
        SEROTONIN_SYNDROME("Serotonin Syndrome", R.id.txt_serotoninsyndrome_header, R.id.txt_serotoninsyndrome_content),
        DEADLY("Deadly", R.id.txt_deadly_header, R.id.txt_deadly_content);

        private String text;
        private int headerId;
        private int contentId;

        private CombinationSeverity(String text, int headerId, int contentId) {
            this.text = text;
            this.headerId = headerId;
            this.contentId = contentId;
        }

        private static CombinationSeverity getSeverityWithText(String text) {
            for (CombinationSeverity severity : CombinationSeverity.values()) {
                if (severity.text.equalsIgnoreCase(text)) {
                    return severity;
                }
            }
            return null;
        }

        private int getHeaderId() {
            return headerId;
        }

        private int getContentId() {
            return contentId;
        }
    }

    private static final String EMPTY_SELECTION = "";
    private String leftDrug = null;
    private String rightDrug = null;

    private Map<String, Map<String, List<String>>> combinationsMap = new TreeMap<String, Map<String, List<String>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_combinations, LayoutBuilder.buildParams()));
        new CombinationsAsyncTask(this, this).execute(this);
    }

    @Override
    protected void onStart() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_combinations_1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                leftDrug = null;
                for (String drug : combinationsMap.keySet()) {
                    if (position == 0) {
                        leftDrug = drug;
                        break;
                    }
                    position--;
                }

                Map<String, List<String>> interactions = combinationsMap.get(leftDrug);

                if (interactions != null) {
                    updateViewWithInteractions(interactions);
                }

                updateSpinner2();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }

        });
        super.onStart();
    }

    private void updateSpinner2() {
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_combinations_2);
        if (EMPTY_SELECTION.equals(leftDrug)) {
            spinner2.setVisibility(View.GONE);
        } else {
            Set<String> combinations = new TreeSet<>(combinationsMap.keySet());
            if (leftDrug != null) {
                combinations.remove(leftDrug);
            }
            spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                    combinations.toArray(new String[combinations.size()])));
            spinner2.setVisibility(View.VISIBLE);
        }
    }

    private void updateViewWithInteractions(Map<String, List<String>> interactionsMap) {
        resetCombinationVisibilities();

        for (Map.Entry<String, List<String>> entry : interactionsMap.entrySet()) {
            CombinationSeverity severity = CombinationSeverity.getSeverityWithText(entry.getKey());
            if (severity != null) {
                TextView header = (TextView) findViewById(severity.getHeaderId());
                header.setVisibility(View.VISIBLE);

                TextView content = (TextView) findViewById(severity.getContentId());
                content.setText(convertListToText(entry.getValue()));
                content.setVisibility(View.VISIBLE);
            }

        }
    }

    private String convertListToText(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s).append("\n");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void resetCombinationVisibilities() {
        for (CombinationSeverity severity : CombinationSeverity.values()) {
            TextView header = (TextView) findViewById(severity.getHeaderId());
            header.setVisibility(View.GONE);

            TextView content = (TextView) findViewById(severity.getContentId());
            content.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateCombinationsMap(Map<String, Map<String, List<String>>> combinations) {
        combinationsMap.put(EMPTY_SELECTION, new TreeMap<String, List<String>>());
        combinationsMap.putAll(combinations);
        refreshSpinner();
    }

    private void refreshSpinner() {
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_combinations_1);
        spinner1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                combinationsMap.keySet().toArray(new String[combinationsMap.size()])));
        updateSpinner2();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
