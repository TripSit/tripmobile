package me.tripsit.mobile.combinations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.tripsit.mobile.comms.ContentRetriever;
import me.tripsit.mobile.error.ErrorHandler;

public class CombinationsAsyncTask extends AsyncTask<Activity, Void, Void> {

    private static final String URL = "http://tripsit.me/combo.json";

    private final CombinationsCallback callback;
    private final ErrorHandler errorHandler;
    private final Map<String, Map<String, List<String>>> combinationsMap;

    CombinationsAsyncTask(CombinationsCallback callback, ErrorHandler errorHandler) {
        this.callback = callback;
        this.errorHandler = errorHandler;
        combinationsMap = new HashMap<String, Map<String, List<String>>>();
    }

    @Override
    protected Void doInBackground(final Activity... activity) {
        try {
            String response = new ContentRetriever(activity[0]).getResponseFromURL(URL);
            JSONObject combinations = new JSONObject(response);
            JSONArray drugNames = combinations.names();
            for (int i = 0; i < drugNames.length(); i++) {
                String drugName = drugNames.getString(i);
                Map<String, List<String>> interactionsMap = buildInteractionsMap(combinations, drugName);
                combinationsMap.put(drugName, interactionsMap);
            }
        } catch (JSONException e) {
            errorHandler.handleGenericError(String.format("Failed to parse combinations text, please report this error: " + e.toString()));
        } catch (IOException e) {
            new AlertDialog.Builder(activity[0])
                    .setTitle("Operation failed")
                    .setMessage("Failed to combinations information. Please check your internet connection and try again.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            doInBackground(activity);
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

    private Map<String, List<String>> buildInteractionsMap(JSONObject combinations, String drugName) throws JSONException {
        Map<String, List<String>> interactionsMap = new HashMap<String, List<String>>();

        JSONObject interactions = combinations.getJSONObject(drugName);
        JSONArray interactionDrugNames = interactions.names();
        for (int j = 0; j < interactionDrugNames.length(); j++) {
            String interactionDrugName = interactionDrugNames.getString(j);
            String interaction = interactions.getString(interactionDrugName);

            List<String> drugsForInteraction = interactionsMap.get(interaction);
            if (drugsForInteraction == null) {
                drugsForInteraction = new LinkedList<String>();
                interactionsMap.put(interaction, drugsForInteraction);
            }
            drugsForInteraction.add(interactionDrugName);
        }
        return interactionsMap;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.updateCombinationsMap(combinationsMap);
    }
}
