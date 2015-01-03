package me.tripsit.mobile.factsheets;

public enum Properties {
	DOSE("dose"),
	DURATION("duration"),
	SUMMARY("summary"),
	WIKI("wiki"),
	ONSET("onset"),
	EFFECTS("effects"),
	ALIASES("aliases"),
	OTHER(null);
	
	private String property;
	
	private Properties(String property) {
		this.property = property;
	}
	
	public static Properties getMatchingProperty(String property) {
		for (Properties prop : Properties.values()) {
			if (property.equalsIgnoreCase(prop.getProperty())) {
				return prop;
			}
		}
		return OTHER;
	}
	
	public String getProperty() {
		return property;
	}
}
