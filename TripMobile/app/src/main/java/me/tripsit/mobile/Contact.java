package me.tripsit.mobile;

import android.os.Bundle;

import me.tripsit.mobile.builders.LayoutBuilder;

public class Contact extends TripMobileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_contact, LayoutBuilder.buildParamsLinearCenterHorizontal()));
    }

}
