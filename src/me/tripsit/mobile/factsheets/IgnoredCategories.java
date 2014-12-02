package me.tripsit.mobile.factsheets;

enum IgnoredCategories {

	FORMATTED_DOSE("formatted_dose");
	
	private String header;

	private IgnoredCategories(String header) {
		this.header = header;
	}
	
	static boolean shouldIgnore(String header) {
		boolean ignore = false;
		for (IgnoredCategories category : IgnoredCategories.values()) {
			if (category.header.equalsIgnoreCase(header)) {
				ignore = true;
				break;
			}
		}
		return ignore;
	}
	
}
