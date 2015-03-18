package me.tripsit.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.tripsit.mobile.builders.LayoutBuilder;

public class Contact extends TripMobileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_contact, LayoutBuilder.buildParamsLinearCenterHorizontal()));
    }

    public void bugReport(View view) {
        sendEmail(getString(R.string.android_email_address), getEmailHeader(R.string.email_bug_report_header), getString(R.string.bug_report_email));
    }

    public void suggestions(View view) {
        sendEmail(getString(R.string.android_email_address), getEmailHeader(R.string.email_feature_header));
    }

    public void content(View view) {
        sendEmail(getString(R.string.content_email_address), getEmailHeader(R.string.email_content_header));
    }

    public void sayHi(View view) {
        sendEmail(getString(R.string.say_hi_email_address), getEmailHeader(R.string.email_say_hi_header));
    }

    private String getEmailHeader(int headerStringId) {
        return getString(R.string.email_header_prefix) + getString(headerStringId);
    }

    private void sendEmail(String email, String subject) {
        sendEmail(email, subject, null);
    }

    private void sendEmail(String email, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.setData(Uri.parse(getString(R.string.mailto) + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (text != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        }
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.error_email_client), Toast.LENGTH_SHORT).show();
        }
    }
}
