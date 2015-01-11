package me.tripsit.mobile.factsheets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

import me.tripsit.mobile.comms.JSONComms;
import me.tripsit.mobile.error.ErrorHandler;

public class DrugInfoAsyncTask extends AsyncTask<Context, Void, Void>  {

    private static final String DRUG_URL = "http://tripbot.tripsit.me/api/tripsit/getDrug?name=";

    private final FactsheetsCallback callback;
    private final ErrorHandler errorHandler;
    private final String drugName;

    private Drug result = null;

    DrugInfoAsyncTask(FactsheetsCallback callback, ErrorHandler errorHandler, String drugName) {
        this.callback = callback;
        this.errorHandler = errorHandler;
        this.drugName = drugName;
    }

    @Override
    protected Void doInBackground(final Context... context) {
        try {
            result = new Drug(JSONComms.retrieveObjectFromUrl(DRUG_URL + drugName));
        } catch (JSONException e) {
            errorHandler.handleGenericError(String.format("Failed to parse text for '%s', please report this error: %s", drugName, e.toString()));
        } catch (IOException e) {
            new AlertDialog.Builder(context[0])
                    .setTitle("Operation failed")
                    .setMessage("Failed to download drug information. Please check your internet connection and try again.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            doInBackground(context);
                        }
                    })
                    .setNegativeButton("Return to menu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            callback.finishActivity();
                        }
                    })
                    .show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onDrugSearchComplete(result);
    }
}
