package me.tripsit.mobile;

import com.splunk.mint.Mint;

/**
 * Created by alex on 23/07/16.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Only report bugs to Mint in release mode
        if ( !BuildConfig.DEBUG ) Mint.initAndStartSession(this, "10465a6c");
    }
}
