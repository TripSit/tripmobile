package me.tripsit.mobile.factsheets;

public enum Categories {

	NAME("name"),
	ALIASES("aliases"),
	PROPERTIES("properties"),
	CATEGORIES("categories"),
	OTHER(null);
	
	private String category;
	
	private Categories(String category) {
		this.category = category;
	}
	
	public static Categories getMatchingCategory(String category) {
		for (Categories cat : Categories.values()) {
			if (category.equalsIgnoreCase(cat.getCategory())) {
				return cat;
			}
		}
		return OTHER;
	}
	
	public String getCategory() {
		return category;
	}
}
