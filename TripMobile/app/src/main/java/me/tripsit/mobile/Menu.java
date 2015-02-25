package me.tripsit.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.tripsit.mobile.builders.LayoutBuilder;
import me.tripsit.mobile.chat.Chat;
import me.tripsit.mobile.chat.Constants;
import me.tripsit.mobile.combinations.Combinations;
import me.tripsit.mobile.common.SharedPreferencesManager;
import me.tripsit.mobile.factsheets.Factsheets;
import me.tripsit.mobile.settings.Settings;
import me.tripsit.mobile.wiki.Wiki;

/**
 * The main menu activity which is shown to the user on startup
 * @author Eddie Curtis
 */
public class Menu extends TripMobileActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutBuilder.buildLinearLayout(this, R.layout.activity_menu, LayoutBuilder.buildParamsLinearCenterHorizontal()));
	}

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    public void clickTripsit(View view) {
		startChatActivity(generateUrl(Constants.TRIPSIT_CHAN));
	}
	
	public void clickGeneralChat(View view) {
		startChatActivity(generateUrl(Constants.PREFIX + SharedPreferencesManager.getChatChannel(this)));
	}

    private String generateUrl(String channel) {
        String kiwiTheme = SharedPreferencesManager.getTheme(this).getKiwiTheme();
        return String.format(Constants.URL_BASE, kiwiTheme, channel);
    }
	
	public void clickFactsheets(View view) {
		Intent intent = new Intent(this, Factsheets.class);
		startActivity(intent);
	}

    public void clickCombinations(View view) {
        Intent intent = new Intent(this, Combinations.class);
        startActivity(intent);
    }
	
	public void clickWiki(View view) {
		Intent intent = new Intent(this, Wiki.class);
		startActivity(intent);
	}

    public void clickSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void clickAbout(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void clickContact(View view) {
        Intent intent = new Intent(this, Contact.class);
        startActivity(intent);
    }

	private void startChatActivity(String chatUrl) {
		Intent intent = new Intent(this, Chat.class);
		intent.putExtra("url", chatUrl);
		startActivity(intent);
	}
}
