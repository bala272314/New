package com.ford.fc.glo.autotest.integration;

import java.util.HashMap;
import java.util.Map;

public class DataStore {

	private static ThreadLocal<Map<String, String>> dataMapLocal = new ThreadLocal<>();

	private static Map<String, String> dataMap = null;

	public static void setValue(String key, String value) {
		if (dataMapLocal.get() == null) {
			dataMap = new HashMap<>();
		}
		dataMap.put(key, value);
		dataMapLocal.set(dataMap);
	}

	public static String getValue(String key) {
		String value = "";
		if (dataMapLocal.get() != null) {
			value = dataMapLocal.get().get(key);
		}
		return value;
	}

}
