package com.kony.audit.javaservice;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.kony.audit.auditprocess.JsonObjectProcessor;
import com.kony.audit.auditprocess.LogEvent;
import com.kony.audit.auditprocess.PreProcessEvent;
import com.kony.audit.auditprocess.PreProcessStaticData;
import com.kony.audit.auditutils.AuditConstants;
import com.kony.audit.auditutils.AuditErrorMessages;
import com.kony.audit.auditutils.AuditUtils;
import com.kony.audit.auditutils.Event;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class ServeEvent implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		diagnostic.prepareDebug("Service called").log();
		JsonElement eventjson;
		String eventjsonstring = null;
		String customerId = null;
		String userName = null;
		String accountId = null;
		String corecustomerid = null;

		Map<String, String> inputParams = null;
		if (inputArray != null && inputArray.length > 1) {
			inputParams = (Map<String, String>) inputArray[1];
		}

		if (inputParams != null) {
			eventjsonstring = inputParams.get(AuditConstants.SERVICE_EVENTJSON);
			customerId = inputParams.get(AuditConstants.SERVICE_CUSTOMERID);
			userName = inputParams.get(AuditConstants.SERVICE_USERNAME);
			accountId = inputParams.get(AuditConstants.SERVICE_ACCOUNTID);
			corecustomerid = inputParams.get(AuditConstants.SERVICE_CORECUSTOMERID);
		}

		if (!isInputValid(eventjsonstring))
			return AuditUtils.returnResult(false, AuditErrorMessages.C_INVALID_INPUT);
		if ((eventjson = AuditUtils.parseString(eventjsonstring)) == null) {
			return AuditUtils.returnResult(false, AuditErrorMessages.C_JSON_PARSE_ERROR);
		}

		serveEvent(eventjson, customerId, userName, accountId, corecustomerid);
		return AuditUtils.returnResult(true, AuditConstants.EMPTY_STRING);
	}

	private static boolean isInputValid(String eventjsonstring) {
		return eventjsonstring != null;
	}

	private static void serveEvent(JsonElement eventjson, String customerid, String username, String accountid,
			String corecustomerid) {
		diagnostic.prepareDebug("In serveEvent").log();
		Event event = PreProcessEvent.processEventData(eventjson);
		event.setCustomerId(customerid);
		event.setUserName(username);
		if (accountid != null && !accountid.equals(AuditConstants.EMPTY_STRING)
				&& !accountid.equals(AuditConstants.NULL_STRING))
			event.setaccountId(accountid);
		if (corecustomerid != null && !corecustomerid.equals(AuditConstants.EMPTY_STRING)
				&& !corecustomerid.equals(AuditConstants.NULL_STRING))
			event.setCorecustomerid(corecustomerid);
		processEvent(event);
	}

	private static void processEvent(Event event) {
		try {
			diagnostic.prepareDebug("In processEvent").log();
			Map<String, String> eventdata = new HashMap<>();
			Map<String, String> otherdata = new HashMap<>();
			Map<String, String> sessiondata = new HashMap<>();
			JsonObjectProcessor.getAllPairsFromJson(event.getJsonElement(), eventdata, AuditConstants.EVENTDATA,
					PreProcessStaticData.getTransactionTypeData());
			JsonObjectProcessor.getAllPairsFromJson(event.getJsonElement(), otherdata, AuditConstants.OTHERDATA,
					PreProcessStaticData.getTransactionTypeData());
			JsonObjectProcessor.getAllPairsFromJson(event.getJsonElement(), sessiondata, AuditConstants.SESSION,
					PreProcessStaticData.getTransactionTypeData());
			configureApp(event, otherdata, sessiondata, PreProcessStaticData.getMfInfoAppData());
			LogEvent.processEvent(event, eventdata, otherdata, sessiondata, PreProcessStaticData.getCurrencyData());
		} catch (Exception ex) {
			alert.prepareError("Exception occured:", ex).log();
		}

	}

	private static void configureApp(Event e, Map<String, String> otherdata, Map<String, String> sessiondata,
			Map<String, String> mfinfoappleveldata) {

		String appkey = AuditConstants.APPID_LOWER;
		String mfappkey = AuditConstants.AID_LOWER;
		if (otherdata.containsKey(appkey)) {
			e.setappId(otherdata.get(appkey));

		} else if (sessiondata.containsKey(mfappkey) && mfinfoappleveldata.containsKey(sessiondata.get(mfappkey))) {
			e.setappId(mfinfoappleveldata.get(sessiondata.get(mfappkey)));
		}
	}

}
