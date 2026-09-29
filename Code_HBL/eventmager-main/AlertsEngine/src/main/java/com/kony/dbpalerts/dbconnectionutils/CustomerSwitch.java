package com.kony.dbpalerts.dbconnectionutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class CustomerSwitch {
	private CustomerSwitch() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String customerSwitchQuery(List<Event> events) {
		String conditionstring = "";
		for (Event event : events) {
			String temptext = "";
			if (event.getCustomerid() != null && event.getAlertcategory() != null)
				temptext = "(Customer_id eq '" + event.getCustomerid() + "' and  AlertCategoryId eq '"
						+ event.getAlertcategory() +  "' and companyLegalUnit eq '" + event.getCompanyLegalUnit()+"')";

			if (!temptext.equals("") && !conditionstring.contains(temptext))
				conditionstring = !conditionstring.equals("") ? conditionstring + " or " + temptext
						: conditionstring + temptext;
		}
		if (!conditionstring.equals(""))
			conditionstring = "( " + conditionstring + " )";
		return conditionstring;
	}

	public static synchronized ConcurrentMap<String, String> processCustomerSwitchData(List<Event> events) {
		String conditionstring = "";
		ConcurrentMap<String, String> resultsetmap = new ConcurrentHashMap<>();
		conditionstring = customerSwitchQuery(events);
		if (conditionstring.equals(""))
			return resultsetmap;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put(AlertConstants.FILTER, conditionstring);
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE, AlertsUtils
					.replaceSchemaName(AlertsDBServiceConstants.CUSTOMERALERTSWITCH, StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		diagnostic.prepareDebug(response.toString()).log();
		if (response != null) {
			Dataset ds = response.getDatasetById("customeralertswitch");
			if (ds != null)
				resultsetmap = ResultSetToMapUtil.genCustMapFromDataSet(ds);
		}
		return resultsetmap;
	}
}
