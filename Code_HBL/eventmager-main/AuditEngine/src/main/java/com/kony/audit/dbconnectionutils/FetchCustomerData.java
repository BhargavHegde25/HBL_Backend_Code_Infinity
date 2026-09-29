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

public class FetchCustomerData {
	private FetchCustomerData() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static Map<String, Object> custDetailsFetch(List<Event> events) {
		Set<String> customers = new HashSet<>();
		Set<String> userids = new HashSet<>();
		Map<String, Object> resultsetmap = new HashMap<>();
		for (Event x : events) {
			if (x.getUserName() == null && x.getCustomerId() != null) {
				customers.add(x.getCustomerId());
			} else if (x.getCustomerId() == null && x.getUserName() != null) {
				userids.add(x.getUserName());
			}
		}
		resultsetmap.put("_customerids", customers.stream().collect(Collectors.joining(",")));
		resultsetmap.put("_usernames", userids.stream().collect(Collectors.joining(",")));
		return resultsetmap;
	}

	public static synchronized Map<String, String> getCustomerDataMap(List<Event> events) {
		Map<String, String> resultsetmap = new HashMap<>();
		Map<String, Object> requestParameters = custDetailsFetch(events);
		Result response = null;
		try {
			response = AuditUtils.callInternalService(requestParameters, AuditDBConstants.EVENTDBDBSERVICE,
					AuditUtils.replaceSchemaName(AuditDBConstants.GETCUSTOMERDATA, AuditUtils.getMainSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}

		if (response != null) {
			Dataset ds = response.getDatasetById("records");
			if (ds != null)
				resultsetmap = DatasettoMapUtil.fetchFieldsFromDataSet(ds);
		}
		return resultsetmap;
	}

}
