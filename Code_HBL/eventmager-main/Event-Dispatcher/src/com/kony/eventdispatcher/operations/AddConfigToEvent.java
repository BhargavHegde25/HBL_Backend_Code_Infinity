package com.kony.eventdispatcher.operations;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.utils.DbConstants;
import com.kony.utils.ErrorCodeEnum;
import com.kony.utils.HelperMethods;
import com.kony.utils.URLConstants;
import com.kony.eventdispatcher.dto.EventTriggerConfig;
import com.konylabs.middleware.dataobject.Result;

public class AddConfigToEvent {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static List<EventTriggerConfig> addConfigurationToEvent(JsonArray eventConfiguration, JsonObject otherData,
			JsonObject customparams, JsonObject reportingParams, JsonObject requestObject, JsonObject responseObject,
			String customerId, Result res) {
		List<EventTriggerConfig> eventList = new ArrayList<>();
		try {
			for (JsonElement i : eventConfiguration) {
				eventList.add(jsonObjectToEvent(i.getAsJsonObject(), otherData, customparams, reportingParams,
						requestObject, responseObject, customerId));
			}
		} catch (Exception e) {
			alert.prepareError(URLConstants.EXCEPTION, e).log();
		}
		if (eventList.isEmpty()) {
			HelperMethods.result(res, ErrorCodeEnum.ERROR_EVENTLISTEMPTY);
			return new ArrayList<>();
		}
		return eventList;

	}

	private static EventTriggerConfig jsonObjectToEvent(JsonObject obj, JsonObject otherData, JsonObject customparams,
			JsonObject reportingParams, JsonObject requestObject, JsonObject responseObject, String customerId) {
		EventTriggerConfig event = new EventTriggerConfig();
		if (obj != null) {
			if (obj.has(DbConstants.CLASSNAME) && obj.get(DbConstants.CLASSNAME) != null)
				event.setClassname(obj.get(DbConstants.CLASSNAME).getAsString());
			if (obj.has(DbConstants.EVENTTYPE) && obj.get(DbConstants.EVENTTYPE) != null)
				event.setEventtype(obj.get(DbConstants.EVENTTYPE).getAsString());
			if (obj.has(DbConstants.EVENTSUBTYPE) && obj.get(DbConstants.EVENTSUBTYPE) != null)
				event.setEventsubtype(obj.get(DbConstants.EVENTSUBTYPE).getAsString());
			if (obj.has(DbConstants.STATUS) && obj.get(DbConstants.STATUS) != null)
				event.setStatus(obj.get(DbConstants.STATUS).getAsString());
			if (obj.has(DbConstants.SERVICECALL) && obj.get(DbConstants.SERVICECALL) != null)
				event.setServicecall(obj.get(DbConstants.SERVICECALL).getAsString());
			if (obj.has(DbConstants.HASFIELDS) && obj.get(DbConstants.HASFIELDS) != null)
				event.setHasFields(obj.get(DbConstants.HASFIELDS).getAsString());
			if (obj.has(DbConstants.CONDITIONS) && obj.get(DbConstants.CONDITIONS) != null)
				event.setConditions(obj.get(DbConstants.CONDITIONS).getAsString());
			if (obj.has(DbConstants.MASKFIELDS) && obj.get(DbConstants.MASKFIELDS) != null)
				event.setMaskedFields(obj.get(DbConstants.MASKFIELDS).getAsString());
			if (obj.has(DbConstants.EXCLUDEFIELDS) && obj.get(DbConstants.EXCLUDEFIELDS) != null)
				event.setExcludedFields(obj.get(DbConstants.EXCLUDEFIELDS).getAsString());
			if (obj.has(DbConstants.EXTERNALCUSTOMERID) && obj.get(DbConstants.EXTERNALCUSTOMERID) != null)
				event.setPasscustomerid(obj.get(DbConstants.EXTERNALCUSTOMERID).getAsString());
			if (obj.has(DbConstants.ACCOUNTIDFIELD) && obj.get(DbConstants.ACCOUNTIDFIELD) != null)
				event.setAccountlevelField(obj.get(DbConstants.ACCOUNTIDFIELD).getAsString());
			if (obj.has(DbConstants.APPID) && obj.get(DbConstants.APPID) != null)
				event.setAppid(obj.get(DbConstants.APPID).getAsString());
			if (obj.has(DbConstants.ISACTIVE) && obj.get(DbConstants.ISACTIVE) != null) {
				if (obj.get(DbConstants.ISACTIVE).getAsString().equals("true")
						|| obj.get(DbConstants.ISACTIVE).getAsString().equals("1"))
					event.setActive(true);
			}
			if (obj.has(DbConstants.ADDFIELDS) && obj.get(DbConstants.ADDFIELDS) != null) {
				event.setAddFields(obj.get(DbConstants.ADDFIELDS).getAsString());
			}
			if (otherData != null && !otherData.toString().equals("{}")) {
				event.setOtherData(otherData.toString());
			}
			if (customparams != null && !customparams.toString().equals("{}")) {
				event.setCustomParams(customparams.toString());
			}
			if (reportingParams != null && !reportingParams.toString().equals("{}")) {
				event.setReportingParams(reportingParams.toString());
			}
			if (requestObject != null && !requestObject.toString().equals("{}")) {
				event.setRequestObject(requestObject.toString());
			}
			if (responseObject != null && !responseObject.toString().equals("{}")) {
				event.setResponseObject(responseObject.toString());
			}
			if (StringUtils.isNotBlank(customerId)) {
				event.setCustomerId(customerId);
			}
		}
		return event;

	}

}
