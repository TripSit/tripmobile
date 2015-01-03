package me.tripsit.mobile.chat;

/**
 * Constants used for the chat features
 * @author Eddie Curtis
 */
public class Constants {

	private static final String URL_FORMAT = "%s/?nick=%s?%s";
	private static final String URL_BASE = "http://chat.tripsit.me";
	
	private static final String NICK = "TripMobileUser";
	
	private static final String TRIPSIT_CHAN = "#tripsit";
	private static final String GENERAL_CHAN = "#home";
	
	public static final String TRIPSIT_URL = String.format(URL_FORMAT, URL_BASE, NICK, TRIPSIT_CHAN);
	public static final String GENERAL_URL = String.format(URL_FORMAT, URL_BASE, NICK, GENERAL_CHAN);
}
