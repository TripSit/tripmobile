package me.tripsit.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.tripsit.mobile.builders.LayoutBuilder;

public class Contact extends TripMobileActivity {

    private static final String PREFIX = "[TripMobile] ";
    private static final String ANDROID_ADDRESS = "android@tripsit.me";
    private static final String BUG_REPORT_SUBJECT = PREFIX + "Bug Report";
    private static final String SUGGESTIONS_SUBJECT = PREFIX + "Feature Suggestion";
    private static final String CONTENT_ADDRESS = "content@tripsit.me";
    private static final String CONTENT_SUBJECT = PREFIX + "Content Update Request";
    private static final String SAY_HI_ADDRESS = "tripsitters@tripsit.me";
    private static final String SAY_HI_SUBJECT = PREFIX + "Social Message";
    private static final String MAILTO = "mailto:";
    private static final String BUG_REPORT_TEXT = "Please fill out the following information before sending this email.\n" +
            "Version of android: \n" +
            "Device: \n" +
            "What happened before you saw the bug: \n" +
            "What is the bug? Does the app crash or function unusually: \n" +
            "If you're happy for us to contact you to request more details, please provide your IRC nick or email address: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_contact, LayoutBuilder.buildParamsLinearCenterHorizontal()));
    }

    public void bugReport(View view) {
        sendEmail(ANDROID_ADDRESS, BUG_REPORT_SUBJECT, BUG_REPORT_TEXT);
    }

    public void suggestions(View view) {
        sendEmail(ANDROID_ADDRESS, SUGGESTIONS_SUBJECT);
    }

    public void content(View view) {
        sendEmail(CONTENT_ADDRESS, CONTENT_SUBJECT);
    }

    public void sayHi(View view) {
        sendEmail(SAY_HI_ADDRESS, SAY_HI_SUBJECT);
    }


    private void sendEmail(String email, String subject) {
        sendEmail(email, subject, null);
    }

    /*
     * Code modified from http://stackoverflow.com/a/2197841/1044603
     */
    private void sendEmail(String email, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{MAILTO + email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (text != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        }
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "You need to install an email client to use the email feature", Toast.LENGTH_SHORT).show();
        }
    }
}
