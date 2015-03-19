package me.tripsit.mobile.factsheets;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.tripsit.mobile.R;

public class Drug {

	private String summary = null;
	private String name = null;
	private String duration = null;
	private String wiki = null;
	private String dosages = null;
	private String onset;
	private String effects;
	private Set<String> aliases = new HashSet<String>();
	private Set<String> categories = new HashSet<String>();
	private Map<String, String> otherInfo = new HashMap<String, String>();

    private final Activity activity;
	
	public Drug(JSONObject drugObject, Activity activity) throws JSONException {

        this.activity = activity;

        JSONObject data = (JSONObject) drugObject.getJSONArray("data").get(0);
        checkError(data);

        @SuppressWarnings("unchecked")
		Iterator<String> it = data.keys();
		while(it.hasNext()) {
			String key = it.next();
			if (!IgnoredCategories.shouldIgnore(key)) {
				switch (Categories.getMatchingCategory(key)) {
					case NAME : name = data.getString(key);
						break;
					case ALIASES : addAllToCollection(aliases, data.getJSONArray(key));
						break;
					case CATEGORIES : addAllToCollection(categories, data.getJSONArray(key));
						break;
					case PROPERTIES : processProperties(data.getJSONObject(key));
						break;
					default : // Do nothing
				}
			}
		}
	}

    private void checkError(JSONObject data) throws JSONException {
        if (data.has("err")) {
            String error = data.getString("err");
            if (error != null && error.trim().length() > 0 && !"null".equals(error) && !"false".equals(error)) {
                String message = data.has("msg") ? data.getString("msg") : error;
                throw new JSONException(activity.getString(R.string.failed_parse_drug_info) + message);
            }
        }
    }

    private void processProperties(JSONObject object) throws JSONException {
		@SuppressWarnings("unchecked")
		Iterator<String> keys = object.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();

			if (!IgnoredCategories.shouldIgnore(key)) {
				switch (Properties.getMatchingProperty(key)) {
				case DOSE:
					dosages = object.getString(key);
					break;
				case DURATION:
					duration = object.getString(key);
					break;
				case WIKI:
					wiki = object.getString(key);
					break;
				case ONSET:
					onset = object.getString(key);
					break;
				case EFFECTS:
					effects = object.getString(key);
					break;
				case SUMMARY:
					summary = object.getString(key);
					break;
				default:
					if (isOtherCategory(key)) {
						String value = object.optString(key);
						if (value != null) {
							otherInfo.put(key, value);
						}
					}
				}
			}
		}
	}

	private boolean isOtherCategory(String key) {
		return Properties.getMatchingProperty(key).getProperty() == null
				&& Categories.getMatchingCategory(key).getCategory() == null;
	}

	private void addAllToCollection(Collection<String> collection, JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			collection.add(array.getString(i));
		}
	}

	public String getSummary() {
		return summary;
	}

	public String getName() {
		return name;
	}

	public String getDuration() {
		return duration;
	}

	public String getWiki() {
		return wiki;
	}

	public String getDosages() {
		return dosages;
	}

	public String getOnset() {
		return onset;
	}

	public String getEffects() {
		return effects;
	}

	public Set<String> getAliases() {
		return aliases;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public Map<String, String> getOtherInfo() {
		return otherInfo;
	}
}
