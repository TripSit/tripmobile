package me.tripsit.mobile.builders;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import me.tripsit.mobile.R;

public class LayoutBuilder {
	
	public static LinearLayout buildLinearLayout(Activity origin, int viewId, LayoutParams params) {
		LayoutInflater inflater = LayoutInflater.from(origin);

		LinearLayout baseLayout = new LinearLayout(origin);
		baseLayout.setOrientation(LinearLayout.VERTICAL);
		
		baseLayout.addView((RelativeLayout) inflater.inflate(R.layout.main_layout, null));
		
		baseLayout.addView(inflater.inflate(viewId, null), params);
		return baseLayout;
	}

	public static LinearLayout.LayoutParams buildParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		return params;
	}
	
	public static LayoutParams buildParamsLinearCenterHorizontal() {
		LinearLayout.LayoutParams params = buildParams();
		params.gravity = Gravity.CENTER_HORIZONTAL;
		return params;
	}
}
