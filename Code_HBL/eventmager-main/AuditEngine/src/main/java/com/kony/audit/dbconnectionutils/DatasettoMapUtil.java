package com.kony.audit.dbconnectionutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kony.audit.auditutils.AuditConstants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;

public class DatasettoMapUtil {
	private DatasettoMapUtil() {
	}

	protected static void fetchAppDataFromDataSet(Dataset ds, Map<String, String> appleveldata) {
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return;
		for (Record record : records) {
			if (record.getParamValueByName(AuditConstants.AID) != null)
				appleveldata.put(record.getParamValueByName(AuditConstants.AID),
						record.getParamValueByName(AuditConstants.APPID_CAMEL));
		}
	}

	protected static Map<String, List<String>> fetchFieldsFromDataSetAccounts(Dataset ds) {

		Map<String, List<String>> resultcustomermap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return resultcustomermap;

		for (Record record : records) {
			if (record.getParamByName(AuditConstants.ACCOUNT_ID) != null
					&& record.getParamByName(AuditConstants.USER_ID) != null
					&& !record.getParamByName(AuditConstants.ACCOUNT_ID).getValue().equals("")
					&& !record.getParamByName(AuditConstants.USER_ID).getValue().equals("")) {
				if (resultcustomermap.containsKey(record.getParamByName(AuditConstants.ACCOUNT_ID).getValue())) {
					List<String> customers = resultcustomermap
							.get(record.getParamByName(AuditConstants.ACCOUNT_ID).getValue());
					customers.add(record.getParamByName(AuditConstants.USER_ID).getValue());
					resultcustomermap.put(record.getParamByName(AuditConstants.ACCOUNT_ID).getValue(), customers);
				} else {
					List<String> customers = new ArrayList<>();
					customers.add(record.getParamByName(AuditConstants.USER_ID).getValue());
					resultcustomermap.put(record.getParamByName(AuditConstants.ACCOUNT_ID).getValue(), customers);
				}
			}
		}
		return resultcustomermap;

	}

	protected static Map<String, Set<String>> genCoreIdMapFromDataSet(Dataset ds) {

		Map<String, Set<String>> coreidmap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return coreidmap;
		for (Record record : records) {
			if (record.getParamValueByName(AuditConstants.BACKENDID) != null
					&& record.getParamValueByName(AuditConstants.CUSTOMER_ID_UNDERSCORE) != null) {
				if (coreidmap.containsKey(record.getParamValueByName(AuditConstants.BACKENDID))) {
					Set<String> custlist = coreidmap.get(record.getParamValueByName(AuditConstants.BACKENDID));
					custlist.add(record.getParamValueByName(AuditConstants.CUSTOMER_ID_UNDERSCORE));
					coreidmap.put(record.getParamValueByName(AuditConstants.BACKENDID), custlist);
				} else {
					Set<String> custlist = new HashSet<>();
					custlist.add(record.getParamValueByName(AuditConstants.CUSTOMER_ID_UNDERSCORE));
					coreidmap.put(record.getParamValueByName(AuditConstants.BACKENDID), custlist);
				}
			}
		}
		return coreidmap;
	}

	public static Map<String, String> fetchFieldsFromDataSet(Dataset ds) {
		Map<String, String> resultmap = new HashMap<>();

		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return resultmap;

		for (Record record : records) {
			if (record.getParamValueByName(AuditConstants.CUSTOMERID_UPPER) != null) {
				resultmap.put(record.getParamValueByName(AuditConstants.CUSTOMERID_UPPER),
						record.getParamValueByName(AuditConstants.USERNAME_UPPER));
			}
			if (record.getParamValueByName(AuditConstants.USERNAME_UPPER) != null) {
				resultmap.put(record.getParamValueByName(AuditConstants.USERNAME_UPPER),
						record.getParamValueByName(AuditConstants.CUSTOMERID_UPPER));
			}
		}
		return resultmap;
	}

}
