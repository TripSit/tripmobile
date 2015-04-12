package me.tripsit.mobile.chat;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ProgressBar;

import me.tripsit.mobile.R;
import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.common.LoadingWebChromeClient;
import me.tripsit.mobile.common.LoadingWebViewClient;

public class Chat extends TripMobileActivity {

    private WebView webView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        this.setProgressBarVisibility(true);
		setContentView(R.layout.activity_chat);
		initialiseWebView();
	}

	private void initialiseWebView() {
        if (webView == null) {
            webView = (WebView) findViewById(R.id.web_irc);
            webView.getSettings().setJavaScriptEnabled(true);
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.chat_progress);
            webView.setWebViewClient(new LoadingWebViewClient(progressBar));
            webView.setWebChromeClient(new LoadingWebChromeClient(progressBar));
            webView.loadUrl((String) getIntent().getExtras().get("url"));
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
