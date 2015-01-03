package me.tripsit.mobile.wiki;

import me.tripsit.mobile.R;
import me.tripsit.mobile.builders.LayoutBuilder;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Wiki extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_wiki, LayoutBuilder.buildParams()));
		initialiseWebView();
	}

	private void initialiseWebView() {
		WebView myWebView = (WebView) findViewById(R.id.web_wiki);
		myWebView.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        if (Uri.parse(url).getHost().equals("wiki.tripsit.me")) {
		            return false;
		        }
		        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		        startActivity(intent);
		        return true;
		    }
		});
		myWebView.loadUrl("https://wiki.tripsit.me/wiki/Main_Page");
	}
}
