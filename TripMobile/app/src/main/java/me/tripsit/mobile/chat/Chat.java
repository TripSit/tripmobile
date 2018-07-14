package me.tripsit.mobile.chat;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import me.tripsit.mobile.R;
import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.common.LoadingWebChromeClient;
import me.tripsit.mobile.common.LoadingWebViewClient;
import me.tripsit.mobile.common.SharedPreferencesManager;

public class Chat extends TripMobileActivity {

    private WebView webView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialiseWebView();
	}

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    private void initialiseWebView() {
        if (webView == null) {
            webView = (WebView) findViewById(R.id.web_irc);
            webView.getSettings().setJavaScriptEnabled(true);
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.chat_progress);
            progressBar.setBackgroundColor(R.attr.progressBackground);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setWebChromeClient(new LoadingWebChromeClient(progressBar));
            webView.loadUrl((String) getIntent().getExtras().get("url"));
            final String ircUsername = SharedPreferencesManager.getChatUsername(this);
            webView.setWebViewClient(new LoadingWebViewClient(progressBar){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    webView.loadUrl(
                            "javascript:(function() { " +
                                    "var element = document.querySelector('input#nick');"
                                    + "if (!element.value) element.value = escape('" + ircUsername + "');" +
                                    "})()");
                }
            });
        }
	}

    @Override
    public void onBackPressed() {
        webView.destroy();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

	
}
