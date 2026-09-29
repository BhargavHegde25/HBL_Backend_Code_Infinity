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

public class FetchCustomerFromAccount {
	private FetchCustomerFromAccount() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static String accountCustFetch(List<Event> events) {
		Set<String> accounts = new HashSet<>();
		for (Event x : events) {
			if (x.getaccountId() != null && x.getCustomerId() == null) {
				accounts.add(x.getaccountId());
			}
		}
		return accounts.stream().collect(Collectors.joining(","));
	}

	public static Map<String, List<String>> accountCustomerIdMapping(List<Event> events) {
		String conditionstring = "";
		Map<String, List<String>> accountcustmap = new HashMap<>();
		conditionstring = accountCustFetch(events);
		if (conditionstring.equals(""))
			return accountcustmap;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_accounts", conditionstring);
		Result response = null;
		try {
			response = AuditUtils.callInternalService(null, AuditDBConstants.EVENTDBDBSERVICE,
					AuditUtils.replaceSchemaName(AuditDBConstants.GETCUSTIDFROMACCOUNT, AuditUtils.getMainSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (response != null) {
			Dataset ds = response.getDatasetById("records");
			if (ds != null)
				accountcustmap = DatasettoMapUtil.fetchFieldsFromDataSetAccounts(ds);
		}
		return accountcustmap;

	}

}
