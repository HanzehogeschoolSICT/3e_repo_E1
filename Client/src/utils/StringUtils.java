package utils;

import java.util.HashMap;

public class StringUtils {

	public static String[] stringToArray(String arrayString) {
		arrayString = arrayString.substring(1, arrayString.length()-1).replace("\"", "");
		
		return arrayString.split(", ");
	}
	
	public static HashMap<String, String> stringToMap(String mapString) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		mapString = mapString.substring(1, mapString.length()-1).replace("\"", "");
		String[] entries = mapString.split(", ");
		for(String entry : entries) {			
			String key = entry.split(": ")[0];
			String value = entry.split(": ").length > 1 ? entry.split(": ")[1] : "";
			
			map.put(key, value);
		}
		
		return map;
	}
}
