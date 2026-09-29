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
import com.kony.dbpalerts.alertsutils.UserAlertDTO;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class CustomerAlertsQueries {
	private CustomerAlertsQueries() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String appendNonAccountLevel(String conditionstring, Event event) {

		if (event.getCustomerid() != null && event.getAlertsubtype() != null && !event.getIsglobal()
				&& !event.getIsaccountLevel()) {
			String temptext = "(Customer_id  eq '" + event.getCustomerid() + "' and AlertSubTypeId  eq '"
					+ event.getAlertsubtype() + "' and companyLegalUnit eq '" + event.getCompanyLegalUnit()+"')";
			if (!conditionstring.contains(temptext))
				conditionstring = !conditionstring.equals("") ? conditionstring + " or " + temptext
						: conditionstring + temptext;
		}
		return conditionstring;

	}

	private static String appendAccountLevel(String conditionstring, Event event, Integer isaccountlevel) {

		if (isaccountlevel == null || isaccountlevel != 1)
			return conditionstring;

		if (event.getAlertsubtype() != null && event.getCustomerid() != null && !event.getIsglobal()
				&& event.getIsaccountLevel() && event.getAccountid() != null) {
			String temptext = "(Customer_id eq '" + event.getCustomerid() + "' and  AlertSubTypeId eq '"
					+ event.getAlertsubtype() + "' and AccountId eq '" + event.getAccountid() + "' and companyLegalUnit eq '" + event.getCompanyLegalUnit()+"')";
			if (!conditionstring.contains(temptext))
				conditionstring = !conditionstring.equals("") ? conditionstring + " or " + temptext
						: conditionstring + temptext;
		}
		return conditionstring;

	}

	private static String appendAccountTypeLevel(String conditionstring, Event event, Integer isaccountlevel) {

		if (isaccountlevel == null || isaccountlevel != 0)
			return conditionstring;
		if (event.getAlertsubtype() != null && event.getCustomerid() != null && !event.getIsglobal()
				&& event.getIsaccountLevel() && event.getAccountid() != null && event.getAccountTypeId() != null) {
			String temptext = "(Customer_id eq '" + event.getCustomerid() + "' and  AlertSubTypeId eq '"
					+ event.getAlertsubtype() + "' and AccountType eq '" + event.getAccountTypeId() + "' and companyLegalUnit eq '" + event.getCompanyLegalUnit()+"')";
			if (!conditionstring.contains(temptext))
				conditionstring = !conditionstring.equals("") ? conditionstring + " or " + temptext
						: conditionstring + temptext;
		}
		return conditionstring;

	}

	private static String customerQueryGen(List<Event> events, Integer isAccountLevel) {
		String conditionstring = "";
		for (Event event : events) {
			conditionstring = appendNonAccountLevel(conditionstring, event);
			conditionstring = appendAccountLevel(conditionstring, event, isAccountLevel);
			conditionstring = appendAccountTypeLevel(conditionstring, event, isAccountLevel);
		}
		if (!conditionstring.equals(""))
			conditionstring = "( " + conditionstring + " )";
		return conditionstring;
	}

	public static synchronized Map<String, UserAlertDTO> processUserLevelNonAccountAlert(List<Event> events,
			Integer isAccountLevel, String alertConfigLevel) {
		String conditionstring = "";
		Map<String, UserAlertDTO> resultSetMap = new HashMap<>();
		conditionstring = customerQueryGen(events, isAccountLevel);
		if (conditionstring.equals(""))
			return resultSetMap;
		String operationname = null;
		String lookupname = "";
		if (alertConfigLevel.equals(AlertConstants.ALERTLEVEL.CATEGORY.toString())) {
			operationname = AlertsDBServiceConstants.ALERT_CUSTOMERCHANNELS_VIEW_ALERTCATEGORYLEVEL;
			lookupname = "alertcustomerchannels_view_alertcategorylevel";
		} else if (alertConfigLevel.equals(AlertConstants.ALERTLEVEL.GROUP.toString())) {
			operationname = AlertsDBServiceConstants.ALERT_CUSTOMERCHANNELS_VIEW_ALERTGROUPLEVEL;
			lookupname = "alertcustomerchannels_view_alertgrouplevel";
		} else if (alertConfigLevel.equals(AlertConstants.ALERTLEVEL.ALERT.toString())) {
			operationname = AlertsDBServiceConstants.ALERT_CUSTOMERCHANNELS_VIEW_ALERTLEVEL;
			lookupname = "alertcustomerchannels_view_alertlevel";
		}
		diagnostic.prepareDebug("conditionstring" + conditionstring).log();
		diagnostic.prepareDebug("operationname" + operationname).log();
		diagnostic.prepareDebug("lookupname" + lookupname).log();

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
			Dataset ds = response.getDatasetById(lookupname);
			if (ds != null)
				try {
					resultSetMap = ResultSetToMapUtil.createUserAlertGenericDatafromDataSet(ds);
				} catch (Exception e) {
					diagnostic.prepareDebug("Error in fetching user level alert data", e).log();
				}
		}
		return resultSetMap;
	}
}