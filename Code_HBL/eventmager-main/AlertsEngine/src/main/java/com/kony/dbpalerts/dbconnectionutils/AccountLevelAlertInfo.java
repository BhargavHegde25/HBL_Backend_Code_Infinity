package com.kony.dbpalerts.dbconnectionutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AccountLevelAlertInfo {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private AccountLevelAlertInfo() {

	}

	public static Integer isAlertAccountLevel() {
		Map<String, Object> requestParameters = new HashMap<>();
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE, AlertsUtils
					.replaceSchemaName(AlertsDBServiceConstants.APPLICATION, StaticDataHolder.getSchemaname()), null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
			return -1;
		}
		if (response == null)
			return -1;
		Dataset ds = response.getDatasetById("application");
		if (ds != null) {
			List<Record> records = ds.getAllRecords();
			if (records == null || records.isEmpty())
				return -1;
			if (records.get(0).getParamValueByName(AlertConstants.ISALERTACCOUNTLEVEL) == null)
				return -1;
			String val = records.get(0).getParamValueByName(AlertConstants.ISALERTACCOUNTLEVEL);
			if (val.equals("1") || val.equals("true"))
				return 1;
			else if (val.equals("0") || val.equals("false"))
				return 0;
		}

		return -1;
	}
}
