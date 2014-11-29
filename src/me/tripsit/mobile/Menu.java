package me.tripsit.mobile;

import me.tripsit.mobile.chat.Constants;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	public void clickTripsit(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TRIPSIT_URL)));
	}
	
	public void clickGeneralChat(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GENERAL_URL)));
	}
}
