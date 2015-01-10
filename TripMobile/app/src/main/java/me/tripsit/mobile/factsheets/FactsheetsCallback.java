package me.tripsit.mobile.factsheets;

import java.util.Collection;

public interface FactsheetsCallback {

    public void onDrugListComplete(Collection<String> drugNames);
    public void onDrugSearchComplete(Drug drug);
    public void finishActivity();
}
