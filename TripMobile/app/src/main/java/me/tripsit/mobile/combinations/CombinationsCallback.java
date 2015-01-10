package me.tripsit.mobile.combinations;

import java.util.List;
import java.util.Map;

public interface CombinationsCallback {

    public void updateCombinationsMap(Map<String, Map<String, List<String>>> combinations);
    public void finishActivity();

}
