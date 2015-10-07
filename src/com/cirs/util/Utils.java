package com.cirs.util;

import java.util.HashMap;
import java.util.Map;

public class Utils {
	public static <K, V> Map<K, V> getAsMap(K key, V value) {
		HashMap<K, V> hashMap = new HashMap<>();
		hashMap.put(key, value);
		return hashMap;
	}
}
