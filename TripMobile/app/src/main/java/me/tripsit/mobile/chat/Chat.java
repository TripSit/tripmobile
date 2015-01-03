package me.tripsit.mobile.chat;

import me.tripsit.mobile.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Chat extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initialiseWebView();
	}

	private void initialiseWebView() {
		WebView webView = (WebView) findViewById(R.id.web_irc);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        return false;
		    }
		});
		webView.loadUrl((String) getIntent().getExtras().get("url"));
	}

	
}
