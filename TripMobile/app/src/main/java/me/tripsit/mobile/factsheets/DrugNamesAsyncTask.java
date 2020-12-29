package me.tripsit.mobile.factsheets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import me.tripsit.mobile.R;
import me.tripsit.mobile.comms.ContentRetriever;

public class DrugNamesAsyncTask extends AsyncTask<Activity, Void, Void>  {

	private static final String ALL_DRUGS_URL = "https://tripbot.tripsit.me/api/tripsit/getAllDrugNames";
	private static final String DATA = "data";
	private static final String SPLIT = "\",\""; // Split by ","

    private final FactsheetsCallback callback;
    private final Activity activity;

	private Set<String> drugNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	
	DrugNamesAsyncTask(FactsheetsCallback callback, Activity activity) {
        this.callback = callback;
        this.activity = activity;
	}

    @Override
    protected Void doInBackground(final Activity... context) {
        ContentRetriever contentRetriever = new ContentRetriever(context[0]);
        try {
            String response = contentRetriever.getResponseFromURL(ALL_DRUGS_URL);
            JSONObject drugs = new JSONObject(response);
            JSONArray list = drugs.getJSONArray(DATA);
            for (int i = 0; i < list.length(); i++) {
                String drugList = list.getString(i);
                // List is in the format ["one","two","three"] and drug names can contain commas
                drugList = drugList.substring(2, drugList.length() - 2);
                for (String s : drugList.split(SPLIT)) {
                    drugNames.add(s);
                }
            }
        } catch (JSONException e) {
            contentRetriever.invalidateResponse(ALL_DRUGS_URL);
            context[0].runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity.getBaseContext(), activity.getString(R.string.failed_download_drug_names), Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            contentRetriever.invalidateResponse(ALL_DRUGS_URL);
            context[0].runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(activity)
                            .setTitle(activity.getString(R.string.operation_failed))
                            .setMessage(activity.getString(R.string.failed_download_drug_info))
                            .setPositiveButton(activity.getString(R.string.retry), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    callback.searchDrugNames();
                                }
                            })
                            .setNegativeButton(activity.getString(R.string.return_to_menu), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    callback.finishActivity();
                                }
                            })
                            .show();
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onDrugListComplete(new ArrayList<String>(drugNames));
    }
}
