package com.kony.dbpalerts.dbconnectionutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.RecipientUtil.ALERTRECIPIENTTYPE_TABLE;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class InitialDbProcess {	

	private InitialDbProcess() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static Map<String, String> fetchAppData() {
		Map<String, String> resultsetmap = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.ALERTSUBTYPEAPP_GET,
							StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured ", e).log();
		}
		if (response != null) {
			Dataset ds = response.getDatasetById("alertsubtypeapp");
			if (ds != null)
				resultsetmap = ResultSetToMapUtil.fetchFieldsFromDataSetApps(ds);
		}
		return resultsetmap;
	}

	static Map<String, String> funcRecordToParamMap(Record rec) {
		return rec.getAllParams().stream().filter(p -> p.getValue() != null).
				collect(Collectors.toMap(Param::getName, Param::getValue));
		
	};
	
	public static Map<String, Map<String, String>> fetchRecipientTypes() {
		Map<String, Map<String, String>> recipientMap = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		Result result = null;
		try {
			result = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.ALERTSRECIPIENTTYPE_GET,
							StaticDataHolder.getSchemaname()),
					null);
			if(result != null && Integer.parseInt(result.getParamValueByName(AlertConstants.OPSTATUS)) == 0 &&
					result.getDatasetById(AlertConstants.ALERTRECIPIENTTYPE) !=  null) {				
				for (Record rec : result.getDatasetById(AlertConstants.ALERTRECIPIENTTYPE).getAllRecords()) {
					recipientMap.put(
							rec.getParamValueByName(ALERTRECIPIENTTYPE_TABLE.ID.getColumnName()), funcRecordToParamMap(rec));
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured which fetch recipients master data", e).log();
		}

		return recipientMap;
	}	

	public static Map<String, String> fetchAlertSubtypeCustomerTypeData() {
		Map<String, String> resultsetmap = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.ALERTSUBTYPECUSTOMERTYPE_GET,
							StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (response != null) {
			Dataset ds = response.getDatasetById("alertsubtypecustomertype");
			if (ds != null)
				resultsetmap = ResultSetToMapUtil.fetchFieldsFromDataSetAlertSubtypeCustomerType(ds);
		}
		return resultsetmap;
	}

	public static Map<String, String> fetchMfAppData() {
		Map<String, String> resultsetmap = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		Result response = null;
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.APPMAPPINGAID_GET,
							StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (response != null) {
			Dataset ds = response.getDatasetById("appmappingaid");
			if (ds != null)
				resultsetmap = ResultSetToMapUtil.fetchFieldsFromDataSetAppsMf(ds);
		}
		return resultsetmap;
	}

	public static String getAlertLevel() {

		Result response = null;
		Map<String, Object> requestParameters = new HashMap<>();
		try {
			response = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.CUSTOMERVIEWALERTCONFIGURATION_GET,
							StaticDataHolder.getSchemaname()),
					null);
			if (response != null) {
				Dataset ds = response.getDatasetById("customerviewalertconfiguration");
				if (ds != null) {
					List<Record> records = ds.getAllRecords();
					if (records != null && !records.isEmpty())
						return records.get(0).getParamValueByName("alertPreferenceView");
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}

		return null;
	}

}
