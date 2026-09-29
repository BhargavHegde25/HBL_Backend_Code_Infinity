package com.kony.dbpalerts.dbconnectionutils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class FetchCustomerData {
	private FetchCustomerData() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static Map<String, Object> custDetailsFetch(List<Event> events) {
		Set<String> customers = new HashSet<>();
		Set<String> userids = new HashSet<>();
		Map<String, Object> resultsetmap = new HashMap<>();
		for (Event x : events) {
			if (x.getUsername() == null && x.getCustomerid() != null) {
				customers.add(x.getCustomerid());
			} else if (x.getCustomerid() == null && x.getUsername() != null) {
				userids.add(x.getUsername());
			}
		}
		resultsetmap.put("_customerids", customers.stream().collect(Collectors.joining(",")));
		resultsetmap.put("_usernames", userids.stream().collect(Collectors.joining(",")));
		return resultsetmap;
	}

	public static Map<String, String> getCustomerDataMap(List<Event> events) {
		Map<String, String> resultsetmap = new HashMap<>();
		Map<String, Object> requestParameters = custDetailsFetch(events);
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.DBPALERTS_GETCUSTOMERDATA,
							StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		diagnostic.prepareDebug(response.toString()).log();
		if (response != null) {
			Dataset ds = response.getDatasetById("records");
			if (ds != null)
				resultsetmap = ResultSetToMapUtil.fetchFieldsFromDataSet(ds);
		}
		return resultsetmap;
	}
}
