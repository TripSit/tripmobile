package me.tripsit.mobile;

import me.tripsit.mobile.chat.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout head = (RelativeLayout) inflater.inflate(R.layout.main_layout, null);
		LinearLayout content = (LinearLayout) inflater.inflate(R.layout.activity_menu, null);
		
		LinearLayout baseLayout = new LinearLayout(this);
		baseLayout.setOrientation(LinearLayout.VERTICAL);
		baseLayout.addView(head);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		baseLayout.addView(content, params);
		
		setContentView(baseLayout);
	}
	
	public void clickTripsit(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TRIPSIT_URL)));
	}
	
	public void clickGeneralChat(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GENERAL_URL)));
	}
}
