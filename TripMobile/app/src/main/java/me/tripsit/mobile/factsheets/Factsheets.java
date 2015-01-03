package me.tripsit.mobile.factsheets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import me.tripsit.mobile.R;
import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.comms.JSONComms;
import me.tripsit.mobile.utils.CollectionUtils;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * The factsheets activity is used to retrieve data about particular drugs from the tripbot API
 * @author Eddie Curtis
 */
public class Factsheets extends Activity {

	private static final String DRUG_URL = "http://tripbot.tripsit.me/api/tripsit/getDrug?name=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_factsheets, LayoutBuilder.buildParams()));
		downloadFactsheetInfo();
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
				populateFormWithDrug((String)parent.getItemAtPosition(position));
			}
		});
	}
	
	public void clickSearch(View v) {
		AutoCompleteTextView searchBox = (AutoCompleteTextView) findViewById(R.id.drugNameSearch);
		Editable drugName = searchBox.getText();
		populateFormWithDrug(drugName.toString());
	}

	private void populateFormWithDrug(String drugName) {
		try {
			Drug drug = new Drug(JSONComms.retrieveObjectFromUrl(DRUG_URL + drugName));
			if (drug != null) {
				TextView disclaimer = (TextView) findViewById(R.id.txt_drugDisclaimer);
				disclaimer.setVisibility(View.GONE);
				updateDrugView(drug);
			}
		} catch (JSONException e) {
			genericError(String.format("Failed to parse text for '%s', please report this error: %s", drugName, e.toString()));
		} catch (IOException e) {
			connectionFailedError();
		}
	}
	
	private void updateDrugView(Drug drug) {
		TextView drugName = (TextView) findViewById(R.id.txt_drugName);
		String updatedDrugName = manipulateDrugName(drug.getName(), drug.getAliases());
		drugName.setText(updatedDrugName);
		
		ExpandableListView infoList = (ExpandableListView) findViewById(R.id.exlist_drugInfo);
		LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		map.put("Summary", Arrays.asList(drug.getSummary()));
		map.put("Dose", Arrays.asList(drug.getDosages()));
		map.put("Effects", Arrays.asList(drug.getEffects()));
		if (drug.getCategories().size() > 0) {
			map.put("Categories", CollectionUtils.collectionToList(drug.getCategories()));
		}
		
		// If we didn't already put the aliases in the name
		if (updatedDrugName.length() == drug.getName().length() && drug.getAliases().size() > 0) {
			map.put("Aliases", CollectionUtils.collectionToList(drug.getAliases()));
		}
		
		List<String> timing = getDrugTimings(drug);
		if (timing.size() > 0) {
			map.put("Timing", timing);
		}
		if (drug.getWiki() != null) {
			drug.getWiki();
		}
		for (Entry<String, String> entry : drug.getOtherInfo().entrySet()) {
			map.put(entry.getKey(), Arrays.asList(entry.getValue()));
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

	private void downloadFactsheetInfo() {
		try {
			initialiseDrugList();
		} catch (JSONException e) {
			genericError("Could not retrieve drug names from tripbot interface. Search may still work but autocomplete suggestions will not be present.");
		} catch (IOException e) {
			connectionFailedError();
		}
	}

	private void genericError(String error) {
		new AlertDialog.Builder(this)
		.setTitle("Operation failed")
		.setMessage(error)
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) { 
		        // Do nothing
		    }
		 })
		 .show();
	}

	private void connectionFailedError() {
		new AlertDialog.Builder(this)
		.setTitle("Operation failed")
		.setMessage("Failed to download drug information. Please check your internet connection and try again.")
		.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) { 
		        downloadFactsheetInfo();
		    }
		 })
		.setNegativeButton("Return to menu", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) { 
		        finish();
		    }
		 })
		 .show();
	}
	
	private void initialiseDrugList() throws JSONException, IOException {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DrugList.instance().getDrugNames());
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.drugNameSearch);
        textView.setAdapter(adapter);
	}
}
