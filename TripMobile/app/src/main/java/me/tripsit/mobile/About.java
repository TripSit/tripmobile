package me.tripsit.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import me.tripsit.mobile.builders.LayoutBuilder;


public class About extends TripMobileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_about, LayoutBuilder.buildParamsLinearCenterHorizontal()));
    }

    public void clickFacebook(View view) {
        Intent intent;
        try {
            getPackageManager().getPackageInfo(getString(R.string.facebook_package), 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook_app)));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook_url)));
        }
        startActivity(intent);
    }

    public void clickReddit(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.reddit_url))));
    }

    public void clickTwitter(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.twitter_app))));
        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.twitter_url))));
        }
    }

    public void clickBitcoin(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.bitcoin_url))));
        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.donate_url))));
        }
    }
}
