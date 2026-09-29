package com.kony.eventdispatcher.operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.eventdispatcher.dto.EventTriggerConfig;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.utils.HelperMethods;
import com.kony.utils.JsonParsingEngine;
import com.kony.utils.URLConstants;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class ProcessDispatchEvent {

	private ProcessDispatchEvent() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static void checkConfigurationsAndDispatchEvent(EventTriggerConfig event) {
		if (!event.isActive()) {
			diagnostic.prepareDebug("Event trigger is inactive").log();
			return;
		}
		JsonObject requestObj = event.getRequestObject() != null
				? new JsonParser().parse(event.getRequestObject()).getAsJsonObject()
				: new JsonObject();
		JsonObject responseObj = event.getResponseObject() != null
				? new JsonParser().parse(event.getResponseObject()).getAsJsonObject()
				: new JsonObject();
		JsonObject customParams = event.getCustomParams() != null
				? new JsonParser().parse(event.getCustomParams()).getAsJsonObject()
				: new JsonObject();
		JsonObject otherData = event.getOtherData() != null
				? new JsonParser().parse(event.getOtherData()).getAsJsonObject()
				: new JsonObject();
		JsonObject reportingParams = event.getReportingParams() != null
				? new JsonParser().parse(event.getReportingParams()).getAsJsonObject()
				: null;
		makeServiceCall(requestObj, responseObj, customParams, event);
		Map<String, String> requestMap = new HashMap<>();
		Map<String, String> responseMap = new HashMap<>();
		Map<String, String> customParamsMap = new HashMap<>();
		JsonParsingEngine.getAllPairsFromJson(requestObj, requestMap);
		JsonParsingEngine.getAllPairsFromJson(responseObj, responseMap);
		JsonParsingEngine.getAllPairsFromJson(customParams, customParamsMap);
		setExternalCustomerId(requestMap, responseMap, customParamsMap, event);
		if (!hasFields(requestMap, responseMap, customParamsMap, event)) {
			diagnostic.prepareDebug("Payload does not have required fields").log();
			return;
		}
		if (!conditionsMet(requestMap, responseMap, customParamsMap, event)) {
			diagnostic.prepareDebug("Conditions not met").log();
			return;
		}
		getAccountIdFromEvent(requestMap, responseMap, customParamsMap, event);
		maskFields(requestObj, responseObj, customParams, event);
		addAdditionalParams(customParams, event);
		addExternalCommunicationData(otherData, customParamsMap);
		diagnostic.prepareDebug("customparamsfromservice " + customParams).log();
		excludeFields(requestObj, responseObj, customParams, event);
		String user = null;
		if (event.getPasscustomerid() != null && event.getPasscustomerid().equals("UserName")) {
			user = event.getExternalCustomerId();
			event.setCustomerId(null);
		} else if (event.getPasscustomerid() != null && StringUtils.isNotBlank(event.getExternalCustomerId())) {
			event.setCustomerId(event.getExternalCustomerId());
		}
		EventsDispatcher.triggerEvent(event.getEventtype(), event.getEventsubtype(), event.getStatus(),
				event.getClassname(), requestObj, responseObj, otherData, customParams, reportingParams,
				event.getCustomerId(), user, event.getAccountId(), event.getAppid());
	}

	private static void addExternalCommunicationData(JsonObject otherData, Map<String, String> customParamsMap) {
		if (otherData == null || customParamsMap == null)
			return;
		if (contains(customParamsMap, URLConstants.EXTERNALPHONE))
			otherData.addProperty(URLConstants.EXTERNALPHONE, customParamsMap.get(URLConstants.EXTERNALPHONE));
		if (contains(customParamsMap, URLConstants.EXTERNALMAIL))
			otherData.addProperty(URLConstants.EXTERNALMAIL, customParamsMap.get(URLConstants.EXTERNALMAIL));
		if (contains(customParamsMap, URLConstants.INCLUDEPREFERREDCONTACT))
			otherData.addProperty(URLConstants.INCLUDEPREFERREDCONTACT,
					customParamsMap.get(URLConstants.INCLUDEPREFERREDCONTACT));

	}

	private static void makeServiceCall(JsonObject requestObj, JsonObject responseObj, JsonObject customParams,
			EventTriggerConfig event) {
		if (event.getServicecall() != null) {
			Map<String, Object> inputparams = new HashMap<>();
			if (StringUtils.isNotBlank(event.getCustomerId())) {
				inputparams.put("customerId", event.getCustomerId());
			}
			inputparams.put("request", requestObj);
			inputparams.put("response", responseObj);
			inputparams.put("result", customParams);
			String[] arr = event.getServicecall().split("-");
			String objectId = null;
			String serviceId = null;
			String operationId = null;
			if (arr.length == 3) {
				objectId = arr[0];
				serviceId = arr[1];
				operationId = arr[2];
			} else {
				serviceId = arr[0];
				operationId = arr[1];
			}
			String res = HelperMethods.callInternalService(inputparams, serviceId, operationId, objectId);
			try {
				JsonObject customParamsFromService = new JsonParser()
						.parse(new JsonParser().parse(res).getAsJsonObject().get("customparams").getAsString())
						.getAsJsonObject();
				customParams.add("customParamsFromService", customParamsFromService);
			} catch (Exception e) {
				alert.prepareError(URLConstants.EXCEPTION, e).log();
			}
		}
	}

	private static void setExternalCustomerId(Map<String, String> requestMap, Map<String, String> responseMap,
			Map<String, String> customParamsMap, EventTriggerConfig event) {
		if (event.getPasscustomerid() != null) {
			String customerIdKey = event.getPasscustomerid().toLowerCase();
			if (contains(requestMap, customerIdKey))
				event.setExternalCustomerId(requestMap.get(customerIdKey.toLowerCase()));
			else if (contains(responseMap, customerIdKey))
				event.setExternalCustomerId(responseMap.get(customerIdKey.toLowerCase()));
			else if (contains(customParamsMap, customerIdKey))
				event.setExternalCustomerId(customParamsMap.get(customerIdKey.toLowerCase()));
		}

	}

	private static boolean contains(Map<String, String> map, String text) {
		return (map != null && map.containsKey(text));
	}

	private static boolean hasFields(Map<String, String> requestMap, Map<String, String> responseMap,
			Map<String, String> customParamsMap, EventTriggerConfig event) {
		if (StringUtils.isBlank(event.getHasFields()))
			return true;
		String[] arr = event.getHasFields().split(",");
		boolean hasFields = false;
		for (String i : arr) {
			if (contains(requestMap, i.toLowerCase()) || contains(responseMap, i.toLowerCase())
					|| contains(customParamsMap, i.toLowerCase())) {
				hasFields = true;
				continue;
			}
			hasFields = false;
			break;
		}
		return hasFields;
	}

	private static boolean conditionsMet(Map<String, String> requestMap, Map<String, String> responseMap,
			Map<String, String> customParamsMap, EventTriggerConfig event) {
		if (event.getConditions() == null)
			return true;
		Set<String> attributesList = getAttributesFromEvent(event);
		diagnostic.prepareDebug("attributesList " + attributesList).log();
		String initializationString = getInitialisationString(requestMap, responseMap, customParamsMap, attributesList);
		diagnostic.prepareDebug("intialisationstring " + initializationString).log();
		if (StringUtils.isEmpty(initializationString))
			return false;
		String conditions = StringUtils.remove(event.getConditions(), "[#]");
		conditions = StringUtils.remove(conditions, "[/#]");
		String validateString = initializationString + conditions;
		diagnostic.prepareDebug("validatestring " + validateString).log();
		return validateConditions(validateString);
	}

	private static boolean validateConditions(String validateString) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			alert.prepareError(engine.eval(validateString).toString()).log();
			return (boolean) engine.eval(validateString);
		} catch (ScriptException e) {
			alert.prepareError(e.toString()).log();
		}
		return false;
	}

	private static String getInitialisationString(Map<String, String> requestMap, Map<String, String> responseMap,
			Map<String, String> customParamsMap, Set<String> attributesList) {
		StringBuilder initialisationStr = new StringBuilder();
		for (String i : attributesList) {
			if (contains(requestMap, i.toLowerCase())) {
				initialisationStr = getAppendedString(initialisationStr, i, requestMap.get(i.toLowerCase()));
			} else if (contains(responseMap, i.toLowerCase())) {
				initialisationStr = getAppendedString(initialisationStr, i, responseMap.get(i.toLowerCase()));
			} else if (contains(customParamsMap, i.toLowerCase())) {
				initialisationStr = getAppendedString(initialisationStr, i, customParamsMap.get(i.toLowerCase()));
			} else {
				initialisationStr = getAppendedString(initialisationStr, i, "&&&");
			}
		}
		return initialisationStr.toString();
	}

	private static StringBuilder getAppendedString(StringBuilder initialisationStr, String i, String value) {
		return initialisationStr.append(i).append("=").append("'").append(value).append("'").append(",");
	}

	private static Set<String> getAttributesFromEvent(EventTriggerConfig event) {
		Set<String> attributeList = new HashSet<>();
		String conditionString = event.getConditions();
		Pattern p = Pattern.compile("\\[#\\](.*?)\\[/#\\]");
		Matcher m = p.matcher(conditionString);
		while (m.find()) {
			String a = m.group();
			attributeList.add(a.substring(3, a.length() - 4));
		}
		return attributeList;
	}

	private static void getAccountIdFromEvent(Map<String, String> requestMap, Map<String, String> responseMap,
			Map<String, String> customParamsMap, EventTriggerConfig event) {
		if (event.getAccountlevelField() != null) {
			String accountIdField = event.getAccountlevelField().toLowerCase();
			if (contains(requestMap, accountIdField))
				event.setAccountId(requestMap.get(accountIdField));
			else if (contains(responseMap, accountIdField))
				event.setAccountId(responseMap.get(accountIdField));
			else if (contains(customParamsMap, accountIdField))
				event.setAccountId(customParamsMap.get(accountIdField));
		}

	}

	private static void maskFields(JsonObject requestObj, JsonObject responseObj, JsonObject customParamsObj,
			EventTriggerConfig event) {
		if (event.getMaskedFields() != null) {
			String maskingLogic = null;
			try {
				maskingLogic = EnvironmentConfigurationsHandler.getServerAppProperty("MASKING_LOGIC");
			} catch (Exception e) {
				diagnostic.prepareDebug(e.toString()).log();
			}
			if (StringUtils.isBlank(maskingLogic))
				maskingLogic = "Last_4_Digits";
			String[] maskedFields = event.getMaskedFields().split(",");
			for (String i : maskedFields) {
				if (requestObj.has(i) && requestObj.get(i) != null) {
					requestObj.addProperty(i, maskFields(requestObj.get(i).getAsString(), maskingLogic));
				}
				if (responseObj.has(i) && responseObj.get(i) != null) {
					responseObj.addProperty(i, maskFields(responseObj.get(i).getAsString(), maskingLogic));
				}
				if (customParamsObj.has(i) && customParamsObj.get(i) != null) {
					customParamsObj.addProperty(i, maskFields(customParamsObj.get(i).getAsString(), maskingLogic));
				}

			}
		}
	}

	private static String maskFields(String value, String maskingLogic) {
		String[] arr = maskingLogic.split("_");
		int n = Integer.parseInt(arr[1]);
		String maskingLoginConcat = arr[0] + "n" + arr[2];
		String maskedvalue = StringUtils.EMPTY;
		switch (maskingLoginConcat) {
		case "LastnDigits":
			if (value != null && !value.isEmpty()) {
				String lastNDigits;
				if (value.length() > n)
					lastNDigits = value.substring(value.length() - n);
				else
					lastNDigits = value;

				maskedvalue = StringUtils.repeat("X", 4) + lastNDigits;
			}
			break;
		case "FirstnDigits":
			if (value != null && !value.isEmpty()) {
				String firstNDigits;
				if (value.length() > n)
					firstNDigits = value.substring(0, n);
				else
					firstNDigits = value;

				maskedvalue = firstNDigits + StringUtils.repeat("X", 4);
			}
			break;
		default:
			break;
		}
		return maskedvalue;
	}

	private static void addAdditionalParams(JsonObject customParamsFromService, EventTriggerConfig event) {
		if (event.getAddFields() == null)
			return;
		JsonObject addFieldsObj = new JsonParser().parse(event.getAddFields()).getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = addFieldsObj.entrySet();
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			customParamsFromService.add(entry.getKey(), entry.getValue());
		}

	}

	private static void excludeFields(JsonObject requestObj, JsonObject responseObj, JsonObject customParamsFromService,
			EventTriggerConfig event) {
		if (event.getExcludedFields() != null) {
			String[] arr = event.getExcludedFields().split(",");

			for (String i : arr) {
				if (requestObj.has(i)) {
					requestObj.remove(i);
				}
				if (responseObj.has(i)) {
					responseObj.remove(i);
				}
				if (customParamsFromService.has(i)) {
					customParamsFromService.remove(i);
				}
			}
		}

	}

}
