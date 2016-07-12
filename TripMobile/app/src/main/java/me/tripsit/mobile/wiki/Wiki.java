package me.tripsit.mobile.wiki;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

import me.tripsit.mobile.R;
import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.common.LoadingWebChromeClient;
import me.tripsit.mobile.common.LoadingWebViewClient;

public class Wiki extends TripMobileActivity {

    private WebView webView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialiseWebView();
	}

    @Override
    public int getLayoutId() {
        return R.layout.activity_wiki;
    }

    private void initialiseWebView() {
        if (webView == null) {
            webView = (WebView) findViewById(R.id.web_wiki);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.wiki_progress);
            webView.setWebViewClient(new LoadingWebViewClient(progressBar) {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (Uri.parse(url).getHost().equals(getString(R.string.wiki_domain))) {
                        return false;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            });
            webView.setWebChromeClient(new LoadingWebChromeClient(progressBar));
            webView.loadUrl(getString(R.string.wiki_main_page));
        }
	}

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }
}
