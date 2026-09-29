package com.kony.dbpalerts.alertsprocess;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.google.gson.JsonObject;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.dbconnectionutils.CommunicationTemplate;

public class FetchCommunicationData {
	private FetchCommunicationData() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static ConcurrentMap<String, Map<String, JsonObject>> fetchCommunicationTemplateData(List<Event> events) {
		ConcurrentMap<String, Map<String, JsonObject>> commtemp = new ConcurrentHashMap<>();
		try {
			commtemp = CommunicationTemplate.fetchCommTemplate(events);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
		return commtemp;
	}

}
