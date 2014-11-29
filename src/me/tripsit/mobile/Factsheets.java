package me.tripsit.mobile;

import me.tripsit.mobile.builders.LayoutBuilder;
import android.app.Activity;
import android.os.Bundle;

/**
 * The factsheets activity is used to retrieve data about particular drugs from the tripbot API
 * @author Eddie Curtis
 */
public class Factsheets extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_factsheets, LayoutBuilder.buildParams()));
	}
}
