/**
 * 
 */
package com.kony.adminconsole.commons.utils;

import java.net.URL;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author amitabh.kotha
 *
 */
public class JSONInputValidatorUtil {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	public static boolean isValidNestedJsonInput(String input) {
		boolean res = true;
		String type = getJsonObjectType(input);
		if (type.equalsIgnoreCase("String")) {
			res = res && isValidString(input);
		} else if (type.equalsIgnoreCase("JSONArray")) {
			JSONArray array = new JSONArray(input);
			for (int i = 0; i < array.length(); i++) {
				res = res && isValidNestedJsonInput(array.get(i).toString());
			}
		} else if (type.equalsIgnoreCase("JSONObject")) {
			JSONObject object = new JSONObject(input);
			Set<String> objects = object.keySet();
			for (String s : objects) {
				res = res && isValidNestedJsonInput(object.get(s).toString());
			}
		}
		return res;
	}

	public static boolean isValidString(String input) {
		if (isURL(input)) {
			return true;
		}
		if (hasHMTLTag(input)) {
			return false;
		}
		String regex = "[^a-zA-Z0-9_.,\\s:&/;@+-=()%]"; // Any char except a-z or A-Z or 0-9 _.,\\s:&/;@+-=()
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return !m.find();
	}

	public static String getJsonObjectType(String input) {
		if (input.startsWith("{") && input.endsWith("}")) {
			return "JSONObject";
		} else if (input.startsWith("[") && input.endsWith("]")) {
			return "JSONArray";
		} else
			return "String";
	}

	public static boolean isURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean hasHMTLTag(String tag) {
		if(tag.contains("<") || tag.contains(">")) {
			return true;
		}
		return false;
	}
}
