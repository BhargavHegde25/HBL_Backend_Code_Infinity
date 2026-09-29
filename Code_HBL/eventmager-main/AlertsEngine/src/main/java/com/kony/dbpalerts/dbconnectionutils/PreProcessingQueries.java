package com.kony.dbpalerts.dbconnectionutils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AccountHelper;
import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class PreProcessingQueries {
	private PreProcessingQueries() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String accountCustFetch(List<Event> events) {
		Set<String> accounts = new HashSet<>();
		for (Event x : events) {
			if (x.getAccountid() != null) {
				accounts.add(x.getAccountid());
			}
		}
		return accounts.stream().collect(Collectors.joining(","));
	}

	public static AccountHelper accountCustomerIdMapping(List<Event> events) {
		String conditionstring = "";
		AccountHelper accountdata = null;
		conditionstring = accountCustFetch(events);
		if (conditionstring.equals(""))
			return accountdata;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_accounts", conditionstring);
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.DBPALERTS_GETCUSTIDFROMACCOUNT,
							StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (response != null) {
			Dataset ds = response.getDatasetById("records");
			if (ds != null)
				accountdata = ResultSetToMapUtil.fetchFieldsFromDataSetAccounts(ds);
		}
		return accountdata;

	}
}
