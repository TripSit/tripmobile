package me.tripsit.mobile.comms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONComms {

    //TODO: where called, this needs to be done in a separate thread or AsyncTask
	public static JSONObject retrieveObjectFromUrl(String url) throws JSONException, IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			StringBuilder sb = new StringBuilder();
			int character;
			while ((character = rd.read()) != -1) {
				sb.append((char) character);
			}
			return new JSONObject(sb.toString());
		} finally {
			is.close();
		}
	}
}
