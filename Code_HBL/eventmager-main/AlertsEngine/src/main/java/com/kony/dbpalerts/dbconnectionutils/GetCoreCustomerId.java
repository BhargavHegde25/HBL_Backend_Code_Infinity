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
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class GetCoreCustomerId {
	private GetCoreCustomerId() {
	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String coreIdFetchQuery(List<Event> events) {
		Set<String> custset = new HashSet<>();
		for (Event event : events) {
			if (event.getCorecustomerid() != null && event.getCustomerid() == null && event.getUsername() == null)
				custset.add(event.getCorecustomerid());

		}
		return custset.stream().collect(Collectors.joining(","));
	}

	public static Map<String, Set<String>> getCoreIdentityMap(List<Event> events) {

		String conditionstring = "";
		Map<String, Set<String>> resultsetmap = new HashMap<>();
		conditionstring = coreIdFetchQuery(events);
		if (conditionstring.equals(""))
			return resultsetmap;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_backendids", conditionstring);
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.DBPALERTS_GETCUSTIDFROMCORE,
							StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		diagnostic.prepareDebug(response.toString()).log();
		if (response != null) {
			Dataset ds = response.getDatasetById("records");
			if (ds != null)

				resultsetmap = ResultSetToMapUtil.genCoreIdMapFromDataSet(ds);
		}
		return resultsetmap;

	}

}
