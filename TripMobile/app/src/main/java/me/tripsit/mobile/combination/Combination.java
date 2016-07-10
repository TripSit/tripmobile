package me.tripsit.mobile.combination;

import android.content.Context;

import me.tripsit.mobile.R;

/**
 * Created by alex on 08/07/16.
 */
public class Combination {

    public String status;
    private CombinationSeverity severity;
    public String note;
    public String drugA;
    public String drugB;

    public ErrorCode error;
    public Exception exception;

    public CombinationSeverity getSeverity(Context ctx) {
        if ( severity == null ) {
            severity = CombinationSeverity.translationHack(status);
        }
        return severity;
    }

    public void setErrorCode(String remoteError) {
        error = ErrorCode.fromNetwork(remoteError);
    }

    public enum ErrorCode {
        DRUG_A_NOT_FOUND,
        DRUG_B_NOT_FOUND,
        SAME_THING,
        GENERAL_FAILURE,
        NETWORK_ERROR;

        public static ErrorCode fromNetwork(String error) {
            switch ( error ) {
                case "Drug A not found.":
                    return DRUG_A_NOT_FOUND;
                case "Drug B not found.":
                    return DRUG_B_NOT_FOUND;
                case "Drug A and B are the same safety category.":
                    return SAME_THING;
                default:
                    return GENERAL_FAILURE;
            }
        }
    }

    // This is used to transform a server severity to an already translated
    // combination severity.
    public enum CombinationSeverity {
        SAFE_SYNERGY("Low Risk & Synergy", R.string.safe_synergy, R.string.safe_synergy_description, R.color.safesynergy_background),
        SAFE_NO_SYNERGY("Low Risk & No Synergy", R.string.safe_no_synergy, R.string.safe_no_synergy_description, R.color.safenosynergy_background),
        SAFE_DECREASE_SYNERGY("Low Risk & Decrease", R.string.safe_decrease, R.string.safe_decrease_description, R.color.safedecrease_background),
        UNSAFE("Unsafe", R.string.unsafe, R.string.unsafe_description, R.color.unsafe_background),
        CAUTION("Caution", R.string.caution, R.string.caution_description, R.color.caution_background),
        DEADLY("Dangerous", R.string.deadly, R.string.deadly_description, R.color.deadly_background);

        public final String serverText;
        private final int header;
        private final int content;
        public final int backgroundColor;

        CombinationSeverity(String serverText, int header, int content, int backgroundColor) {
            this.serverText = serverText;
            this.header = header;
            this.content = content;
            this.backgroundColor = backgroundColor;
        }

        public static CombinationSeverity translationHack(String serverText) {
            for ( CombinationSeverity severity : values() ) {
                if ( severity.serverText.equalsIgnoreCase(serverText) ) {
                    return severity;
                }
            }
            return null;
        }

        public String getContent(Context ctx) {
            return ctx.getString(content);
        }

        public String getHeader(Context ctx) {
            return ctx.getString(header);
        }
    }
}
