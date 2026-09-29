package com.kony.dbpalerts.dbconnectionutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class GlobalAlertQueries {
	private GlobalAlertQueries() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String globalQueryGen(List<Event> events) {
		String conditionstring = "";
		for (Event event : events) {
			String temptext = "";
			if (event.getAlertsubtype() != null && event.getAlerttype() != null)
				temptext = "(AlertSubTypeId eq '" + event.getAlertsubtype() + "' and  AlertTypeId eq '"
						+ event.getAlerttype() + "' and companyLegalUnit eq '" + event.getCompanyLegalUnit()+"')";
			if (!temptext.equals("") && !conditionstring.contains(temptext))
				conditionstring = !conditionstring.equals("") ? conditionstring + " or " + temptext
						: conditionstring + temptext;
		}
		if (!conditionstring.equals(""))
			conditionstring = "( " + conditionstring + " )";
		return conditionstring;
	}

	public static synchronized Map<String, Map<String, String>> processGlogalData(List<Event> events,
																String alertConfigLevel) {
		String conditionstring = "";
		Map<String, Map<String, String>> resultsetmap = new HashMap<>();
		conditionstring = globalQueryGen(events);
		String operationname = null;
		String lookupname = "";
		if (alertConfigLevel.equals(AlertConstants.ALERTLEVEL.CATEGORY.toString())) {
			operationname = AlertsDBServiceConstants.ALERTS_FETCH_GLOBALDATA_VIEW_ALERTCATEGORYLEVEL;
			lookupname="alerts_fetch_globaldata_view_alertcategorylevel";
		} else if (alertConfigLevel.equals(AlertConstants.ALERTLEVEL.GROUP.toString())) {
			operationname = AlertsDBServiceConstants.ALERTS_FETCH_GLOBALDATA_VIEW_ALERTGROUPLEVEL;
			lookupname="alerts_fetch_globaldata_view_alertgrouplevel";
		} else if (alertConfigLevel.equals(AlertConstants.ALERTLEVEL.ALERT.toString())) {
			operationname = AlertsDBServiceConstants.ALERTS_FETCH_GLOBALDATA_VIEW_ALERTLEVEL;
			lookupname="alerts_fetch_globaldata_view_alertlevel";
		}
		diagnostic.prepareDebug("conditionstring" + conditionstring).log();
		diagnostic.prepareDebug("operationname" + operationname).log();
		diagnostic.prepareDebug("lookupname" + lookupname).log();
		if (conditionstring.equals(""))
			return resultsetmap;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put(AlertConstants.FILTER, conditionstring);
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(operationname, StaticDataHolder.getSchemaname()), null);
			diagnostic.prepareDebug(ResultToJSON.convert(response)).log();
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}

		diagnostic.prepareDebug(response.toString()).log();
		if (response != null) {
			// 
			// 
			// 
			Dataset ds = response.getDatasetById(lookupname);
			if (ds != null)
				try {
					resultsetmap = ResultSetToMapUtil.createHashMapOfGlobalDataSet(ds);
				} catch (Exception e) {
					diagnostic.prepareDebug(e.toString()).log();
				}
		}
		return resultsetmap;
	}
}
