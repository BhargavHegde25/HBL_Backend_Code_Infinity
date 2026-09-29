package com.kony.audit.dbconnectionutils;

import com.kony.audit.auditutils.AuditDBConstants;
import com.kony.audit.auditutils.AuditUtils;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

public class DataBasePreprocessingEvents {
	private DataBasePreprocessingEvents() {
	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static void mfInfoFetchAppLevelData(Map<String, String> appleveldata) {
		Result response = null;
		try {
			response = AuditUtils.callInternalService(null, AuditDBConstants.EVENTDBDBSERVICE,
					AuditUtils.replaceSchemaName(AuditDBConstants.APPMAPPINGAID_GET, AuditUtils.getMainSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error Occured", e).log();
		}
		diagnostic.prepareDebug(response.toString()).log();
		if (response == null)
			return;
		Dataset ds = response.getDatasetById("appmappingaid");
		if (ds == null)
			return;
		DatasettoMapUtil.fetchAppDataFromDataSet(ds, appleveldata);
	}

	public static void fetchCurrencyData(List<String> currencycodes) {
		Result response = null;
		try {
			response = AuditUtils.callInternalService(null, AuditDBConstants.EVENTDBDBSERVICE,
					AuditUtils.replaceSchemaName(AuditDBConstants.APPLICATION_GET, AuditUtils.getMainSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (response == null)
			return;
		Dataset ds = response.getDatasetById("application");
		if (ds == null)
			return;
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return;
		if ((records.get(0)).getParamValueByName("currencyCode") != null)
			currencycodes.add((records.get(0)).getParamValueByName("currencyCode"));
	}

	public static void transactiontypefetch(Map<String, String> transactiondata) {
		Result response = null;
		try {
			response = AuditUtils.callInternalService(null, AuditDBConstants.EVENTDBDBSERVICE,
					AuditUtils.replaceSchemaName(AuditDBConstants.TRANSACTION_TYPE_GET, AuditUtils.getMainSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (response == null)
			return;
		Dataset ds = response.getDatasetById("transactiontype");
		if (ds == null)
			return;
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return;
		for (Record record : records) {
			if (record.getParamValueByName("description") != null && record.getParamValueByName("Id") != null)
				transactiondata.put(record.getParamValueByName("description"), record.getParamValueByName("Id"));
		}
	}
}
