package me.tripsit.mobile.comms;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import me.tripsit.mobile.common.SharedPreferencesManager;

/**
 * Created by eddie on 28/01/15.
 */
public class ContentRetriever {

    private final Activity activity;

    public ContentRetriever(Activity activity) {
        this.activity = activity;
    }

    public void invalidateResponse(String url) {
        SharedPreferencesManager.invalidateURLResponse(url, activity);
    }

    public String getResponseFromURL(String url) throws IOException {
        String cachedResponse = SharedPreferencesManager.getCachedURLResponse(url, activity);
        String response = cachedResponse;
        if (cachedResponse == null || !SharedPreferencesManager.isCachedDataValid(url, activity)) {
            try {
                String liveResponse = retrieveFromURL(url);
                if (liveResponse != null) {
                    SharedPreferencesManager.saveURLResponse(url, liveResponse, activity);
                    response = liveResponse;
                }
            } catch (IOException e) {
                if (cachedResponse == null) {
                    throw e;
                } else {
                    //TODO log this somewhere
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    private String retrieveFromURL(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int character;
            while ((character = rd.read()) != -1) {
                sb.append((char) character);
            }
            return sb.toString();
        } finally {
            is.close();
        }
    }
}
