package me.tripsit.mobile.factsheets;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import me.tripsit.mobile.R;
import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.utils.CollectionUtils;

/**
 * The factsheets activity is used to retrieve data about particular drugs from the tripbot API
 * @author Eddie Curtis
 */
public class Factsheets extends TripMobileActivity implements FactsheetsCallback {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_factsheets, LayoutBuilder.buildParams()));
		searchDrugNames();
		setDrugNameSearchListeners((AutoCompleteTextView) findViewById(R.id.drugNameSearch));
	}
	
	private void setDrugNameSearchListeners(final AutoCompleteTextView findViewById) {
		findViewById.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				findViewById.setText("");
			}
		});
		findViewById.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                searchDrug((String)parent.getItemAtPosition(position));
			}
		});
	}
	
	public void clickSearch(View v) {
		AutoCompleteTextView searchBox = (AutoCompleteTextView) findViewById(R.id.drugNameSearch);
		Editable drugName = searchBox.getText();
        searchDrug(drugName.toString());
	}

    @Override
    public void searchDrug(String drugName) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.drugNameSearch);
        textView.clearFocus();
        new DrugInfoAsyncTask(this, this, drugName).execute(this);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void onDrugListComplete(Collection<String> drugNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                drugNames.toArray(new String[drugNames.size()]));
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.drugNameSearch);
        textView.setAdapter(adapter);
    }

    @Override
    public void onDrugSearchComplete(Drug drug) {
        if (drug != null) {
            TextView disclaimer = (TextView) findViewById(R.id.txt_drugDisclaimer);
            disclaimer.setVisibility(View.GONE);
            updateDrugView(drug);
        }
    }

    @Override
    public void searchDrugNames() {
        new DrugNamesAsyncTask(this, this).execute(this);
    }

    private void updateDrugView(Drug drug) {
        TextView drugName = (TextView) findViewById(R.id.txt_drugName);
        String updatedDrugName = manipulateDrugName(drug.getName(), drug.getAliases());
        drugName.setText(updatedDrugName);

        ExpandableListView infoList = (ExpandableListView) findViewById(R.id.exlist_drugInfo);
        LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();

        // If we didn't already put the aliases in the name
        if (updatedDrugName.length() == drug.getName().length() && drug.getAliases().size() > 0) {
            map.put("Aliases", CollectionUtils.collectionToList(drug.getAliases()));
        }

        map.put("Summary", Arrays.asList(drug.getSummary()));
        map.put("Dose", Arrays.asList(drug.getDosages()));
        if (drug.getEffects() != null) {
            map.put("Effects", Arrays.asList(drug.getEffects()));
        }
        if (drug.getCategories().size() > 0) {
            map.put("Categories", CollectionUtils.collectionToList(drug.getCategories()));
        }

        List<String> timing = getDrugTimings(drug);
        if (timing.size() > 0) {
            map.put("Timing", timing);
        }
        if (drug.getWiki() != null) {
            map.put("Wiki", Arrays.asList(drug.getWiki()));
        }
        for (Entry<String, String> entry : drug.getOtherInfo().entrySet()) {
            map.put(capitalise(entry.getKey()), Arrays.asList(entry.getValue()));
        }
        infoList.setAdapter(new DrugListAdapter(this, map));
    }

    private String manipulateDrugName(String drugName, Set<String> set) {
        // If the drug name is less than 4 characters long, assume it's an acronym and capitalise it
        String name = drugName.length() < 4 ? drugName.toUpperCase() : drugName;
        if (set.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(' ').append('(');
            for (String s : set) {
                sb.append(s).append(',').append(' ');
            }
            if (name.length() + sb.length() < 40) { //TODO: check maximum length of String for smallest screen size
                name = name + sb.toString().substring(0, sb.length() - 2) + ')';
            }
        }

        return name;
    }

    private List<String> getDrugTimings(Drug drug) {
        List<String> timing = new ArrayList<String>();
        if (drug.getOnset() != null) {
            timing.add("Onset: " + drug.getOnset());
        }
        if (drug.getDuration() != null) {
            timing.add("Duration: " + drug.getDuration());
        }
        return timing;
    }

    private String capitalise(String string) {
        return string == null || string.length() < 2 ? string :
                Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
