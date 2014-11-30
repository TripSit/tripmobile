package me.tripsit.mobile.factsheets;

import java.io.IOException;

import me.tripsit.mobile.R;
import me.tripsit.mobile.builders.LayoutBuilder;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * The factsheets activity is used to retrieve data about particular drugs from the tripbot API
 * @author Eddie Curtis
 */
public class Factsheets extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_factsheets, LayoutBuilder.buildParams()));
		downloadFactsheetInfo();
	}

	private void downloadFactsheetInfo() {
		try {
			initialiseDrugList();
		} catch (JSONException e) {
			//TODO: replace with android strings
			new AlertDialog.Builder(this)
		    .setTitle("Operation failed")
		    .setMessage("Could not retrieve drug names from tripbot interface. Search may still work but autocomplete suggestions will not be present.")
		    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // Do nothing
		        }
		     })
		     .show();
		} catch (IOException e) {
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
	}
	
	private void initialiseDrugList() throws JSONException, IOException {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DrugList.instance().getDrugNames());
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.drugNameSearch);
        textView.setAdapter(adapter);
	}
}
