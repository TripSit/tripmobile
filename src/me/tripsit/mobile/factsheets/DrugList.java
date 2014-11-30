package me.tripsit.mobile.factsheets;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import me.tripsit.mobile.comms.JSONComms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrugList {

	private static final String ALL_DRUGS_URL = "http://tripbot.tripsit.me/api/tripsit/getAllDrugNames";
	private static final String DATA = "data";
	private static final String SPLIT = "\",\""; // Split by ","
	
	private static DrugList instance = null;

	private Set<String> drugNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	
	private DrugList() throws JSONException, IOException {
		JSONObject drugs = JSONComms.retrieveObjectFromUrl(ALL_DRUGS_URL);
		JSONArray list = drugs.getJSONArray(DATA);
		for (int i = 0; i < list.length(); i++) {
			String drugList = list.getString(i);
			// List is in the format ["one","two","three"] and drug names can contain commas
			drugList = drugList.substring(2, drugList.length() - 2);
			for (String s : drugList.split(SPLIT)) {
				drugNames.add(s);
			}
		}
	}
	
	public static DrugList instance() throws JSONException, IOException {
		if (instance == null) {
			instance = new DrugList();
		}
		return instance;
	}
	
	public String[] getDrugNames() {
		return drugNames.toArray(new String[drugNames.size()]);
	}
	
	public boolean addDrug(String name) {
		return drugNames.add(name);
	}
}
