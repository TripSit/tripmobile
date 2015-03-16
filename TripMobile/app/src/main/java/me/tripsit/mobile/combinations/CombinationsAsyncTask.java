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

import me.tripsit.mobile.R;
import me.tripsit.mobile.comms.ContentRetriever;

public class CombinationsAsyncTask extends AsyncTask<Activity, Void, Void> {

    private static final String URL = "http://tripsit.me/combo.json";

    private final CombinationsCallback callback;
    private final Activity activity;
    private final Map<String, Map<String, List<String>>> combinationsMap;

    CombinationsAsyncTask(CombinationsCallback callback, Activity activity) {
        this.callback = callback;
        this.activity = activity;
        combinationsMap = new HashMap<String, Map<String, List<String>>>();
    }

    @Override
    protected Void doInBackground(final Activity... context) {
        ContentRetriever contentRetriever = new ContentRetriever(context[0]);
        try {
            String response = contentRetriever.getResponseFromURL(URL);
            JSONObject combinations = new JSONObject(response);
            JSONArray drugNames = combinations.names();
            for (int i = 0; i < drugNames.length(); i++) {
                String drugName = drugNames.getString(i);
                Map<String, List<String>> interactionsMap = buildInteractionsMap(combinations, drugName);
                combinationsMap.put(drugName, interactionsMap);
            }
        } catch (final JSONException e) {
            contentRetriever.invalidateResponse(URL);
            context[0].runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(activity)
                            .setTitle(activity.getString(R.string.operation_failed))
                            .setMessage(activity.getString(R.string.failed_parse_combinations) + e.getMessage())
                            .setNeutralButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    callback.finishActivity();
                                }
                            })
                            .show();
                }
            });
        } catch (final IOException e) {
            contentRetriever.invalidateResponse(URL);
            context[0].runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(activity)
                            .setTitle(activity.getString(R.string.operation_failed))
                            .setMessage(activity.getString(R.string.failed_download_combinations))
                            .setPositiveButton(activity.getString(R.string.retry), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    callback.downloadCombinations();
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
