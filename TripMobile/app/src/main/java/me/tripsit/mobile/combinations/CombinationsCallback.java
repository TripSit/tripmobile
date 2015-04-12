package me.tripsit.mobile.combinations;

import java.util.Map;
import java.util.Set;

public interface CombinationsCallback {

    public void downloadCombinations();
    public void updateCombinationsMap(Map<String, Map<String, Set<String>>> combinations);
    public void finishActivity();

}
