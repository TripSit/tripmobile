package me.tripsit.mobile.combinations;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
        SAFE_SYNERGY("Safe & Synergy", "%s and %s %s a safe combination, which provides synergistic effects. Be sure to lower the doses of each, as they will potentiate each other accordingly.", R.id.txt_safesynergy_header, R.id.txt_safesynergy_content),
        SAFE_NO_SYNERGY("Safe & No Synergy", "%s and %s %s a safe combination, which doesn't provide any notable potentiated or decreased effects.", R.id.txt_safenosynergy_header, R.id.txt_safenosynergy_content),
        SAFE_DECREASE_SYNERGY("Safe & Decrease", "%s and %s %s a safe combination, however one or more of the drugs will have decreased effects when they are combined.", R.id.txt_safedecrease_header, R.id.txt_safedecrease_content),
        UNSAFE("Unsafe", "%s and %s %s an unsafe combination and should be taken with extreme caution, or ideally avoided entirely.", R.id.txt_unsafe_header, R.id.txt_unsafe_content),
        SEROTONIN_SYNDROME("Serotonin Syndrome", "%s and %s should not be combined as they increase the risks of serotonin syndrome. Make sure that one of the drugs has completely left your system before taking the other.", R.id.txt_serotoninsyndrome_header, R.id.txt_serotoninsyndrome_content),
        DEADLY("Deadly", "%s and %s %s a potentially deadly combination, and should be avoided even in low dosages", R.id.txt_deadly_header, R.id.txt_deadly_content);

        private final String text;
        private final String singleCombinationText;
        private final int headerId;
        private final int contentId;

        private CombinationSeverity(String text, String singleCombinationText, int headerId, int contentId) {
            this.text = text;
            this.singleCombinationText = singleCombinationText;
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

        private String getSingleCombinationText() {
            return singleCombinationText;
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
                leftDrug = getSelectedDrugName(combinationsMap.keySet(), position);

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

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_combinations_2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (leftDrug != null) {
                    String drugName = getSelectedDrugName(getCombinationsSetExcludingLeftDrug(), position);
                    if (drugName != null && !EMPTY_SELECTION.equals(drugName)) {
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
        if (EMPTY_SELECTION.equals(leftDrug)) {
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

    private void updateViewWithSingleInteraction(Map<String, List<String>> interactionsMap, String drugName) {
        resetCombinationVisibilities();

        for (Map.Entry<String, List<String>> entry : interactionsMap.entrySet()) {
            List<String> list = entry.getValue();
            if (list != null) {
                for (String interactionDrug : list) {
                    if (interactionDrug.equals(drugName)) {
                        CombinationSeverity severity = CombinationSeverity.getSeverityWithText(entry.getKey());
                        TextView textView = (TextView) findViewById(R.id.txt_single_combination);
                        textView.setText(getSingleCombinationText(leftDrug, drugName, severity));
                        textView.setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }
        }

    }

    private String getSingleCombinationText(String drug1, String drug2, CombinationSeverity severity) {
        return String.format(severity.getSingleCombinationText(), drug1, drug2, drug2.endsWith("s") || drug1.endsWith("s") ? "are" : "is");
    }

    private String convertListToText(List<String> values) {
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
