package com.kony.eventdispatcher.wrapper;

import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.kony.utils.HelperMethods;


public class EventsConfigurationHolder {

	private EventsConfigurationHolder() {
	}

	private static JsonArray eventsConfig = null;

	public static void updateEventsConfiguration() {
		eventsConfig = new JsonArray();
		String res = HelperMethods.callInternalService(new HashMap<>(), "EventManagerDBService",
				"dbxdb_eventtriggerconfiguration_get", null);
		try {
			eventsConfig = new JsonParser().parse(res).getAsJsonObject().get("eventtriggerconfiguration")
					.getAsJsonArray();
		} catch (Exception e) {
			eventsConfig = new JsonArray();
		}
	}

	public static JsonArray getEventsConfiguration() {
		return eventsConfig;
	}

}
