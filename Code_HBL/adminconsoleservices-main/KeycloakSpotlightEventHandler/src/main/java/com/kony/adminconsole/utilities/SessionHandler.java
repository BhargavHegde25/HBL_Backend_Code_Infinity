package com.kony.adminconsole.utilities;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;

public class SessionHandler {
	private static final Properties PROPS = loadProps();
	private static final String APP_KEY = "spotlight_ak";
	private static final String APP_SECRET = "spotlight_as";
	private static final String API_ACCESS_TOKEN = "spotlight_aat";
	private static final String TOKEN_END_POINT = "spotlight_apitokenendpoint";
	
	public static String getSpotlightAPILoginToken() {
		String claimsToken = "";
		String appkey = PROPS.getProperty(APP_KEY);
		String appsecret = PROPS.getProperty(APP_SECRET);
		String apiAccessToken = PROPS.getProperty(API_ACCESS_TOKEN);
		String tokenEndpoint = PROPS.getProperty(TOKEN_END_POINT);

		DBXResult result = new DBXResult();

		Map<String, Object> headersMap = new HashMap<String, Object>();

		headersMap.put("Content-Type", "application/json");
		headersMap.put("Accept", "application/json");
		headersMap.put("X-Kony-App-Key", appkey);
		headersMap.put("X-Kony-App-Secret", appsecret);
		headersMap.put("X-Kony-AC-API-Access-Token", apiAccessToken);

		result = HTTPOperationsForKeycloak.sendHttpRequest(HTTPOperationsForKeycloak.operations.POST, tokenEndpoint, "",
				headersMap);

		if (result != null) {
			JSONObject responseJsonObject = new JSONObject(result.getResponse().toString());
			JSONObject claimsTokenObj = responseJsonObject.getJSONObject("claims_token");

			if (claimsTokenObj != null && claimsTokenObj.has("value")) {
				claimsToken = (claimsTokenObj.get("value") != null ? claimsTokenObj.get("value").toString() : null);
			} else {
				System.out.println("Claims token missing or null in the response");
			}

			System.out.println("Claims token extracted from response");
		}
		return claimsToken;
	}
	
	
	public static Properties loadProps() {
		Properties properties = new Properties();
		try (InputStream configInputStream = SessionHandler.class.getClassLoader()
				.getResourceAsStream("config.properties");) {
			properties.load(configInputStream);
		} catch (Exception e) {
			System.out.println("Error occured while loading config.properties");
		}
		return properties;
	}
	
	public static String getValue(String key) {
		return PROPS.getProperty(key);
	}
}

