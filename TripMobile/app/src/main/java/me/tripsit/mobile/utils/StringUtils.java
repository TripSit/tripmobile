package me.tripsit.mobile.utils;

/**
 * Created by eddie on 28/03/15.
 */
public class StringUtils {

    private static final int MIN_DRUG_NAME_LENGTH = 4;

    private StringUtils () {
        // Static methods only
    }

    /**
     * Capitalises the name of a drug if it is less than {@value #MIN_DRUG_NAME_LENGTH} characters long.
     * Otherwise just the first character is capitalised<br/>
     * For example: <br/>
     * <ul>
     * <li>"dmt" becomes "DMT"</li>
     * <li>"2c-b" becomes "2c-b"</li>
     * <li>"cannabis" becomes "Cannabis"</li>
     * <li>"MDMA" becomes "MDMA"</li>
     * </ul>
     * @param drugName - The drug name
     * @return - The formatted drug name
     */
    public static String formatDrugName(String drugName) {
        if (drugName.length() < MIN_DRUG_NAME_LENGTH) {
            return drugName.toUpperCase();
        } else {
            return Character.toUpperCase(drugName.charAt(0)) + drugName.substring(1);
        }
    }
}
