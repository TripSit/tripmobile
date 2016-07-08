package me.tripsit.mobile.combinations;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import me.tripsit.mobile.R;
import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.builders.LayoutBuilder;

public class Combinations extends TripMobileActivity implements CombinationsCallback {

    private enum CombinationSeverity {
        SAFE_SYNERGY("Safe & Synergy", R.string.safe_synergy_description, R.id.txt_safesynergy_header, R.id.txt_safesynergy_content),
        SAFE_NO_SYNERGY("Safe & No Synergy", R.string.safe_no_synergy_description, R.id.txt_safenosynergy_header, R.id.txt_safenosynergy_content),
        SAFE_DECREASE_SYNERGY("Safe & Decrease", R.string.safe_decrease_description, R.id.txt_safedecrease_header, R.id.txt_safedecrease_content),
        UNSAFE("Unsafe", R.string.unsafe_description, R.id.txt_unsafe_header, R.id.txt_unsafe_content),
        SEROTONIN_SYNDROME("Serotonin Syndrome", R.string.serotonin_syndrome_description, R.id.txt_serotoninsyndrome_header, R.id.txt_serotoninsyndrome_content),
        DEADLY("Deadly", R.string.deadly_description, R.id.txt_deadly_header, R.id.txt_deadly_content);

        private final String text;
        private final int descriptionId;
        private final int headerId;
        private final int contentId;

        private CombinationSeverity(String text, int description, int headerId, int contentId) {
            this.text = text;
            this.descriptionId = description;
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

        private int getDescriptionId() {
            return descriptionId;
        }
    }

    private String leftDrug = null;
    private String rightDrug = null;

    private Map<String, Map<String, Set<String>>> combinationsMap = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadCombinations();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_combinations;
    }

    @Override
    public void downloadCombinations() {
        setProgressBarIndeterminateVisibility(true);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_combinations);
        progressBar.setVisibility(View.VISIBLE);
        new CombinationsAsyncTask(this, this).execute(this);
    }

    @Override
    protected void onStart() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_combinations_1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                leftDrug = getSelectedDrugName(combinationsMap.keySet(), position);

                Map<String, Set<String>> interactions = combinationsMap.get(leftDrug);

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

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_combinations_2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (leftDrug != null) {
                    String drugName = getSelectedDrugName(getCombinationsSetExcludingLeftDrug(), position);
                    if (drugName != null && !getString(R.string.select_drug).equals(drugName)) {
                        updateViewWithSingleInteraction(combinationsMap.get(leftDrug), drugName);
                    } else {
                        updateViewWithInteractions(combinationsMap.get(leftDrug));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });


        super.onStart();
    }

    private String getSelectedDrugName(Set<String> potentialNames, int position) {
        for (String drug : potentialNames) {
            if (position == 0) {
                return drug;
            }
            position--;
        }
        return null;
    }

    private void updateSpinner2() {
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_combinations_2);
        if (getString(R.string.select_drug).equals(leftDrug)) {
            spinner2.setVisibility(View.GONE);
        } else {
            Set<String> combinations = getCombinationsSetExcludingLeftDrug();
            spinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                    combinations.toArray(new String[combinations.size()])));
            spinner2.setVisibility(View.VISIBLE);
        }
    }

    private Set<String> getCombinationsSetExcludingLeftDrug() {
        Set<String> combinations = new TreeSet<>(combinationsMap.keySet());
        if (leftDrug != null) {
            combinations.remove(leftDrug);
        }
        return combinations;
    }

    private void updateViewWithInteractions(Map<String, Set<String>> interactionsMap) {
        resetCombinationVisibilities();

        for (Map.Entry<String, Set<String>> entry : interactionsMap.entrySet()) {
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

    private void updateViewWithSingleInteraction(Map<String, Set<String>> interactionsMap, String drugName) {
        resetCombinationVisibilities();

        for (Map.Entry<String, Set<String>> entry : interactionsMap.entrySet()) {
            Set<String> set = entry.getValue();
            if (set != null) {
                for (String interactionDrug : set) {
                    if (interactionDrug.equals(drugName)) {
                        CombinationSeverity severity = CombinationSeverity.getSeverityWithText(entry.getKey());
                        TextView textView = (TextView) findViewById(R.id.txt_single_combination);
                        textView.setText(getString(severity.getDescriptionId(), leftDrug, drugName));
                        textView.setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }
        }

    }

    private String convertListToText(Set<String> values) {
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s).append("\n");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void resetCombinationVisibilities() {

        TextView textView = (TextView) findViewById(R.id.txt_single_combination);
        textView.setVisibility(View.GONE);

        for (CombinationSeverity severity : CombinationSeverity.values()) {
            TextView header = (TextView) findViewById(severity.getHeaderId());
            header.setVisibility(View.GONE);

            TextView content = (TextView) findViewById(severity.getContentId());
            content.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateCombinationsMap(Map<String, Map<String, Set<String>>> combinations) {
        setProgressBarIndeterminateVisibility(false);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_combinations);
        progressBar.setVisibility(View.GONE);
        combinationsMap.put(getString(R.string.select_drug), new TreeMap<String, Set<String>>());
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
