package com.kony.audit.dbconnectionutils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.audit.auditutils.AuditDBConstants;
import com.kony.audit.auditutils.AuditUtils;
import com.kony.audit.auditutils.Event;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class FetchCustomerFromCoreCustomerId {
	private FetchCustomerFromCoreCustomerId() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String coreIdFetchQuery(List<Event> events) {
		Set<String> custset = new HashSet<>();
		for (Event event : events) {
			if (event.getCorecustomerid() != null && event.getCustomerId() == null && event.getUserName() == null)
				custset.add(event.getCorecustomerid());

		}
		return custset.stream().collect(Collectors.joining(","));
	}

	public static Map<String, Set<String>> coreCustomerIdCustomerIdMapping(List<Event> events) {
		String conditionstring = "";
		Map<String, Set<String>> corecustcustmap = new HashMap<>();
		conditionstring = coreIdFetchQuery(events);
		if (conditionstring.equals(""))
			return corecustcustmap;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_backendids", conditionstring);
		Result response = null;
		try {
			response = AuditUtils.callInternalService(null, AuditDBConstants.EVENTDBDBSERVICE,
					AuditUtils.replaceSchemaName(AuditDBConstants.GETCUSTIDFROMCORE, AuditUtils.getMainSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (response != null) {
			Dataset ds = response.getDatasetById("records");
			if (ds != null)
				corecustcustmap = DatasettoMapUtil.genCoreIdMapFromDataSet(ds);
		}
		return corecustcustmap;

	}

}
