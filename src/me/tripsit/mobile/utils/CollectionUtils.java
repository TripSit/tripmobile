package me.tripsit.mobile.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

	public static <T> List<T> collectionToList(Collection<T> collection) {
		List<T> list = new ArrayList<T>();
		list.addAll(collection);
		return list;
	}
}
