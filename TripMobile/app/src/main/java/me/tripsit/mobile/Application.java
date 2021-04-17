package me.tripsit.mobile;

import android.os.Build;
import android.webkit.WebView;

/**
 * Created by alex on 23/07/16.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        // Only report bugs to Mint in release mode
        if ( !BuildConfig.DEBUG ) {
            //Mint.initAndStartSession(this, "10465a6c");
        }
    }
}
