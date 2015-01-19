package me.tripsit.mobile.common;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by eddie on 19/01/15.
 */
public class LoadingWebChromeClient extends WebChromeClient {

    private final ProgressBar progressBar;

    public LoadingWebChromeClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
        progressBar.setProgress(progress);
    }
}
