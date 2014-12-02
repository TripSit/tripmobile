package me.tripsit.mobile.builders;

import me.tripsit.mobile.R;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class LayoutBuilder {
	
	public static LinearLayout buildLinearLayout(Activity origin, int viewId, LayoutParams params) {
		LayoutInflater inflater = (LayoutInflater) origin.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LinearLayout baseLayout = new LinearLayout(origin);
		baseLayout.setOrientation(LinearLayout.VERTICAL);
		
		baseLayout.addView((RelativeLayout) inflater.inflate(R.layout.main_layout, null));
		
		baseLayout.addView(inflater.inflate(viewId, null), params);
		return baseLayout;
	}
	
	public static ScrollView buildScrollView(Activity origin, int viewId, LayoutParams params) {
		ScrollView baseLayout = new ScrollView(origin);		
		baseLayout.addView(buildLinearLayout(origin, viewId, params));
		return baseLayout;
	}


	public static LinearLayout.LayoutParams buildParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		return params;
	}
	
	public static LayoutParams buildParamsLinearCenterHorizontal() {
		LinearLayout.LayoutParams params = buildParams();
		params.gravity = Gravity.CENTER_HORIZONTAL;
		return params;
	}
}
