package com.kony.dbpalerts.dbconnectionutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class CommunicationTemplate {
	private CommunicationTemplate() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String commQuery(List<Event> events) {
		String conditionstring = "";
		for (Event event : events) {
			String temptext = "(AlertSubTypeId eq '" + event.getAlertsubtype() + "' and LanguageCode eq '"
					+ event.getLanguagecode() + "' and Status_id eq '" + event.getCommstatusid() + "' and companyLegalUnit eq '" + event.getCompanyLegalUnit()+"')";
			if (!conditionstring.contains(temptext))
				conditionstring = conditionstring.equals("") ? temptext : conditionstring + " or " + temptext;
		}
		if (!conditionstring.equals(""))
			conditionstring = "( " + conditionstring + " )";
		return conditionstring;
	}

	public static synchronized ConcurrentMap<String, Map<String, JsonObject>> fetchCommTemplate(List<Event> events) {
		String conditionstring = "";
		ConcurrentMap<String, Map<String, JsonObject>> resultsetmap = new ConcurrentHashMap<>();
		conditionstring = commQuery(events);
		if (conditionstring.equals(""))
			return resultsetmap;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put(AlertConstants.FILTER, conditionstring);
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.COMMUNICATIONTEMPLATE,
							StaticDataHolder.getSchemaname()),
					null);

		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		diagnostic.prepareDebug(response.toString()).log();
		if (response != null) {
			Dataset ds = response.getDatasetById("communicationtemplate");
			if (ds == null)
				return resultsetmap;
			resultsetmap = ResultSetToMapUtil.createHashMapOfCommunicationDataDataSet(ds);
		}
		return resultsetmap;
	}
}