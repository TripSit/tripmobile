package me.tripsit.mobile.factsheets;

import java.util.List;

public interface FactsheetsCallback {

    public void onDrugListComplete(List<String> drugNames);
    public void onDrugSearchComplete(Drug drug);
    public void searchDrug(String drugName);
    public void searchDrugNames();
    public void finishActivity();
}
