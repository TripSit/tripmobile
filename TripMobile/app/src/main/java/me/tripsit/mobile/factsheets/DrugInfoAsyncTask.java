package me.tripsit.mobile.factsheets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.tripsit.mobile.comms.ContentRetriever;

public class DrugInfoAsyncTask extends AsyncTask<Activity, Void, Void>  {

    private static final String DRUG_URL = "http://tripbot.tripsit.me/api/tripsit/getDrug?name=";

    private final FactsheetsCallback callback;
    private final Activity activity;
    private final String drugName;
    private final String url;

    private Drug result = null;

    DrugInfoAsyncTask(FactsheetsCallback callback, Activity activity, String drugName) {
        this.callback = callback;
        this.activity = activity;
        this.drugName = drugName;
        this.url = DRUG_URL + drugName;
    }

    @Override
    protected Void doInBackground(final Activity... activities) {
        ContentRetriever contentRetriever = new ContentRetriever(activities[0]);
        try {
            String response = contentRetriever.getResponseFromURL(url);
            result = new Drug(new JSONObject(response));
        } catch (final JSONException e) {
            contentRetriever.invalidateResponse(url);
            activities[0].runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            contentRetriever.invalidateResponse(url);
            activities[0].runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(activity)
                            .setTitle("Operation failed")
                            .setMessage("Failed to download drug information. Please check your internet connection and try again.")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    callback.searchDrug(drugName);
                                }
                            })
                            .setNegativeButton("Return to menu", new DialogInterface.OnClickListener() {
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
        callback.onDrugSearchComplete(result);
    }
}
