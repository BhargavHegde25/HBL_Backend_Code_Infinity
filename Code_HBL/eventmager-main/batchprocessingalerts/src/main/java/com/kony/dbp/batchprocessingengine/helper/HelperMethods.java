package com.kony.dbp.batchprocessingengine.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map.Entry;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.google.common.hash.Hashing;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class HelperMethods {

	private HelperMethods() {
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}
	public static JsonArray mergeJsonArray(JsonArray first, JsonArray second) {
		for (JsonElement element : second) {
			first.add(element);
		}
		return first;
	}

	public static String getJsonObjects(JsonObject event, String key, boolean required) {
		JsonObject eventobj = event.getAsJsonObject();
		String tempele = null;
		tempele = JsonParsingEngine.getStringFromJsonObject(eventobj, key, required);
		if (tempele == null)
			return null;
		if (tempele.equals("") && (key.equalsIgnoreCase("eventId") || key.equalsIgnoreCase("eventType")
				|| key.equalsIgnoreCase("eventSubType") || key.equalsIgnoreCase("Status_id")
				|| key.equalsIgnoreCase("user") || key.equalsIgnoreCase("customerId")
				|| key.equalsIgnoreCase("accountnumber"))) {
			return null;
		}

		return tempele;
	}

	public static JsonObject getJsonObjects(String key, JsonObject event, boolean required) {
		JsonObject eventdata = null;
		try {
			JsonObject eventobj = event.getAsJsonObject();
			JsonElement tempele = null;

			tempele = JsonParsingEngine.getElementFromJsonObject(eventobj, key, required);
			if (tempele == null)
				return null;
			eventdata = tempele.getAsJsonObject();
			return eventdata;
		} catch (Exception e) {
			return eventdata;
		}

	}

	public String getProperty1(String paramkey) {
		Properties prop = new Properties();
		String paramvalue = null;
		InputStream inputStream = null;
		try {

			inputStream = HelperMethods.class.getClassLoader().getResourceAsStream("Alert.properties");
			prop.load(inputStream);
			paramvalue = prop.getProperty(paramkey);
		} catch (IOException e) {
			diagnostic.prepareDebug("Error occured:" + e).log();
		}
		finally
		{
			if(inputStream != null)
			{
				try {
					inputStream.close();
				} catch (IOException e) {
					alert.prepareError("Error  Occured :", e).log();
				}
			}
		}
		return paramvalue;
	}

	public static JsonObject mergeJsonObjects(JsonObject src, JsonObject dest) {
		Set<Entry<String, JsonElement>> s = src.entrySet();
		for (Entry<String, JsonElement> elem : s) {
			dest.add(elem.getKey(), elem.getValue());
		}
		return dest;
	}

	public static String deriveToken(ServicesManager servicesManager, String events) throws Exception {
		String secret = HelperMethods.getConfigProperty("QUEUEMASTER_SHARED_SECRET");
		if (secret == null || secret.length() == 0) {
			throw new RuntimeException("QueueMaster shared secret has not been configured!  ");
		}
		String eventsHash = Hashing.sha512().hashString(events, StandardCharsets.UTF_8).toString(); // Hashing using
																									// Guava lib
		String saltedSecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedSecret, StandardCharsets.UTF_8).toString();
	}

	public static Result getErrorResult(int errorNumber, String errorMsg) {
		Result result = new Result();
		result.addParam(new Param("errornumber", Integer.toString(errorNumber)));
		result.addParam(new Param("errormsg", errorMsg));
		result.addParam(new Param("success", "false"));
		return result;
	}

	public static Result getErrorResult(Throwable ex) {
		String errorMsg = ex.getMessage();
		if (errorMsg == null) {
			StackTraceElement ste = ex.getStackTrace()[0];
			errorMsg = ex.getClass().getName() + "' thrown in " + ste.getClassName() + "." + ste.getMethodName();
		}
		return getErrorResult(7777, errorMsg);
	}

	public static int getDifferenceBetweenDates(String date1, String date2) {
		return (int) ChronoUnit.DAYS.between(LocalDate.parse(date1), LocalDate.parse(date2));
	}

}
