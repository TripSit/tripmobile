package me.tripsit.mobile.combination;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Locale;

import me.tripsit.mobile.comms.ContentRetriever;

/**
 * Created by alex on 08/07/16.
 */
public class CombinationAsyncTask extends AsyncTask<String, Integer, Combination> {

    private final static String queryFormat = "https://tripbot.tripsit.me/api/tripsit/getInteraction"+
                                           "?drugA=%1$s&drugB=%2$s";
    private static final String LOG_TAG = "CombinationAsyncTask";

    @Override
    protected Combination doInBackground(String... params) {

        if ( params.length != 2 ) {
            return errorCombination(new InvalidParameterException("Not enough parameters - need 2"));
        }

        // Fetch a URL. For now, don't save it to the cache
        // or we would fill the cache up with aliases
        String url = String.format(Locale.UK, queryFormat, params[0], params[1]);
        Combination combination = new Combination();
        try {
            String response = ContentRetriever.retrieveFromURL(url);
            // We should be using JsonReader but our API target is 8.
            // JSONObject is okay in this case because the response is probably very small.
            JSONObject json = new JSONObject(response);

            // Check for errors.
            boolean error = false;
            if ( !json.get("err").equals(JSONObject.NULL) ) {
                error = json.getBoolean("err");
            }

            // Check for data
            JSONArray array = json.getJSONArray("data");
            if ( array.length() < 1 ) {
                return errorCombination(new InvalidParameterException("remote json 'data' array is empty"));
            }
            JSONObject data = (JSONObject)array.get(0);

            // Set something for return
            if ( !error ) {
                combination.status = data.getString("status");
                if ( data.has("note") ) combination.note = data.getString("note");
                combination.drugA = data.getString("interactionCategoryA");
                combination.drugB = data.getString("interactionCategoryB");
            } else {
                combination.setErrorCode(data.getString("msg"));

                // Check if the error is related to both drugs being the same
                if ( combination.error == Combination.ErrorCode.SAME_THING ) {
                    // Make this not an error but a synergetic effect because lol it's the same drug
                    combination.error = null;
                    combination.drugA = params[0];
                    combination.drugB = params[1];
                    combination.status = Combination.CombinationSeverity.SAFE_SYNERGY.serverText;
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to parse JSON: " + e);
            return errorCombination(e);
        } catch (IOException e) {
            combination.error = Combination.ErrorCode.NETWORK_ERROR;
            Log.e(LOG_TAG, "Failed to network");
        } catch ( Exception e ) {
            // Could have failed a class cast or something
            Log.e(LOG_TAG, "Failed: " + e);
            return errorCombination(e);
        }

        return combination;
    }

    @NonNull
    private Combination errorCombination(Exception e) {
        Combination combination = new Combination();
        combination.error = Combination.ErrorCode.GENERAL_FAILURE;
        combination.exception = e;
        return combination;
    }
}
