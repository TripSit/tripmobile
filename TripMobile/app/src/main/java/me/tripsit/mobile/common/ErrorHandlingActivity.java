package me.tripsit.mobile.common;

import android.app.AlertDialog;
import android.content.DialogInterface;

import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.error.ErrorHandler;

public abstract class ErrorHandlingActivity extends TripMobileActivity implements ErrorHandler {

    @Override
    public void handleGenericError(String error) {
        new AlertDialog.Builder(this)
                .setTitle("Operation failed")
                .setMessage(error)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
    }
}
