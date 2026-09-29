package com.dbp.reminderengine.businessdelegate.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.reminderengine.businessdelegate.api.CustomerDetailsBusinessDelegate;
import com.dbp.reminderengine.resource.impl.CustomerDetailsResourceImpl;
import com.dbp.reminderengine.utils.Constants;
import com.dbp.reminderengine.utils.HelperMethods;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerDetailsBusinessDelegateImpl implements CustomerDetailsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constants.DATETIMEFORMATTER);
	private static String configlevel = null;

	@Override
	public JsonObject getCustomerDetails() {
		String zoneOffSet = getZoneOffSet();
		if (zoneOffSet == null || zoneOffSet.equals("")) {
			return returnResult(false, "Invalid zoneoffset");
		}
		LocalDateTime lastsynctimestamp = getLastsyncTime();
		LocalDateTime currentTimeStampLdt = getCurrentTimestamp(zoneOffSet);
		if (currentTimeStampLdt == null)
			return returnResult(false, "Invalid zoneoffset");
		if (lastsynctimestamp == null) {
			updateLastSyncTime(currentTimeStampLdt);
			return returnResult(false, "Invalid lastexectime");
		}
		String previousDay = getDay(lastsynctimestamp);
		String currentDay = getDay(currentTimeStampLdt);
		String startTime = getTime(lastsynctimestamp);
		String endTime = getTime(currentTimeStampLdt);
		Integer currentDate = getDate(currentTimeStampLdt);
		Map<String, Object> inputmap = new HashMap<>();
		String isLastDate = "false";
		if (configlevel == null)
			setConfigLevel();
		if (configlevel == null)
			returnResult(false, "Alerts configuration is not set.");
		if (currentDay.equals(previousDay)) {
			isLastDate = Boolean.toString(isLastDate(currentTimeStampLdt.toLocalDate()));
			inputmap.put(Constants.STARTTIME, startTime);
			inputmap.put(Constants.ENDTIME, endTime);
			inputmap.put(Constants.SCHEDULEDAY, getDayName(currentTimeStampLdt));
			inputmap.put(Constants.SCHEDULEDATE, currentDate);
			inputmap.put(Constants.ISLASTDATE, isLastDate);
			String operation = "";
			if (configlevel.equals(Constants.ALERTLEVEL.CATEGORY.toString()))
				operation = Constants.GETCUTOMERS_CATEGORYLEVEL;
			else if (configlevel.equals(Constants.ALERTLEVEL.GROUP.toString()))
				operation = Constants.GETCUTOMERS_GROUPLEVEL;
			else if (configlevel.equals(Constants.ALERTLEVEL.ALERT.toString()))
				operation = Constants.GETCUTOMERS_ALERTLEVEL;

			diagnostic.prepareDebug("inputmap " + inputmap).log();
			diagnostic.prepareDebug("operation " + operation).log();
			try {
				String responseString = DBPServiceExecutorBuilder.builder()
						.withOperationId(
								HelperMethods.replaceSchemaName(operation, CustomerDetailsResourceImpl.getSchemaName()))
						.withRequestParameters(inputmap).withServiceId(Constants.REMINDERENGINEDBSERVICE).build()
						.getResponse();
				diagnostic.prepareDebug("response from alertsscheduleinfo " + responseString).log();
				buildInputAndCallAlertsService(responseString);
			} catch (Exception e) {
				alert.prepareError("Exception occurred while calling db service", e).log();
				return returnResult(false, "error occurred while calling db service");
			}
		} else {
			Integer previousDate = getDate(lastsynctimestamp);
			if (isLastDate(lastsynctimestamp.toLocalDate()) || isLastDate(currentTimeStampLdt.toLocalDate())) {
				isLastDate = "true";
			}
			String isPrevDateLastDate = Boolean.toString(isLastDate(lastsynctimestamp.toLocalDate()));
			inputmap.put(Constants.STARTTIMEPREV, startTime);
			inputmap.put(Constants.STARTTIMECURR, Constants.STARTTIMEVAL);
			inputmap.put(Constants.ENDTIMEPREV, Constants.ENDTIMEVAL);
			inputmap.put(Constants.ENDTIMECURR, endTime);
			inputmap.put(Constants.SCHEDULEDAYPREV, getDayName(lastsynctimestamp));
			inputmap.put(Constants.SCHEDULEDAYCURR, getDayName(currentTimeStampLdt));
			inputmap.put(Constants.SCHEDULEDATECURR, currentDate);
			inputmap.put(Constants.SCHEDULEDATEPREV, previousDate);
			inputmap.put(Constants.ISLASTDATE, isLastDate);
			inputmap.put(Constants.ISPREVDATELASTDATE, isPrevDateLastDate);
			String operation = "";
			if (configlevel.equals(Constants.ALERTLEVEL.CATEGORY.toString()))
				operation = Constants.GETALLCUTOMERS_CATEGORYLEVEL;
			else if (configlevel.equals(Constants.ALERTLEVEL.GROUP.toString()))
				operation = Constants.GETALLCUTOMERS_GROUPLEVEL;
			else if (configlevel.equals(Constants.ALERTLEVEL.ALERT.toString()))
				operation = Constants.GETALLCUTOMERS_ALERTLEVEL;
			diagnostic.prepareDebug("inputmap " + inputmap).log();
			diagnostic.prepareDebug("operation " + operation).log();
			try {
				String responseString = DBPServiceExecutorBuilder.builder()
						.withOperationId(
								HelperMethods.replaceSchemaName(operation, CustomerDetailsResourceImpl.getSchemaName()))
						.withRequestParameters(inputmap).withServiceId(Constants.REMINDERENGINEDBSERVICE).build()
						.getResponse();
				diagnostic.prepareDebug("responseString1 from customeralertfrequency " + responseString).log();
				buildInputAndCallAlertsService(responseString);
			} catch (Exception e) {
				alert.prepareError("Exception occurred ", e).log();
				return returnResult(false, "error occurred while calling db service");
			}

		}
		updateLastSyncTime(currentTimeStampLdt);
		return returnResult(true, "");
	}

	private boolean isLastDate(LocalDate timeStampLd) {
		return (timeStampLd.lengthOfMonth() == timeStampLd.getDayOfMonth());
	}

	private JsonObject returnResult(boolean status, String errmsg) {
		JsonObject result = new JsonObject();
		if (status)
			result.addProperty("success", status);
		else {
			result.addProperty("success", status);
			result.addProperty("errmsg", errmsg);
		}
		return result;
	}

	private Integer getDate(LocalDateTime currentTimeStampLdt) {
		try {
			return currentTimeStampLdt.toLocalDate().getDayOfMonth();
		} catch (Exception e) {
			alert.prepareError("Exception occured while getting date", e).log();
		}
		return null;
	}

	private String getZoneOffSet() {
		try {
			Result response = null;
			Map<String, Object> requestParameters = new HashMap<>();
			response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(Constants.GETZONEOFFSET_OPER,
							CustomerDetailsResourceImpl.getSchemaName()))
					.withRequestParameters(requestParameters).withServiceId(Constants.REMINDERENGINEDBSERVICE).build()
					.getResult();
			Dataset rr = response.getDatasetById(Constants.APPLICATIONDATASETID);
			if (rr == null)
				return null;
			List<Record> rec = rr.getAllRecords();
			if (rec == null || rec.isEmpty())
				return null;
			if (rec.get(0).getParamByName(Constants.TIMEZONEOFFSET) != null
					&& rec.get(0).getParam(Constants.TIMEZONEOFFSET).getValue() != null) {
				return rec.get(0).getParamByName(Constants.TIMEZONEOFFSET).getValue();
			}
		} catch (Exception e) {
			alert.prepareError("Exception occurred", e).log();
		}
		return null;
	}

	private void updateLastSyncTime(LocalDateTime currentTimestamp) {
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("id", 1);
		requestParameters.put(Constants.LASTSYNCTIME, currentTimestamp.format(dtf).replace(" ", "T"));
		try {
			DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(Constants.LASTSYNCTIMEUPDATE_OPERATION,
							CustomerDetailsResourceImpl.getSchemaName()))
					.withRequestParameters(requestParameters).withServiceId(Constants.REMINDERENGINEDBSERVICE).build()
					.getResult();

		} catch (Exception e) {
			alert.prepareError("Error Occured", e).log();
		}

	}

	private void buildInputAndCallAlertsService(String responseString) {
		boolean isOrchCall = true;
		try {
			isOrchCall = Boolean.parseBoolean(HelperMethods.getConfigProperty(Constants.REMINDERENGINEISORCHCALL));
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching env variable", e).log();
			isOrchCall = true;
		}
		JsonArray customerAlertFrequencyRecords = new JsonParser().parse(responseString).getAsJsonObject()
				.getAsJsonArray(Constants.RECORDS);
		if (customerAlertFrequencyRecords == null || customerAlertFrequencyRecords.size() == 0) {
			return;
		}
		JsonArray formattedAlertFreqrecords = formatCustomerAlertFrequencyRecords(customerAlertFrequencyRecords);
		Map<String, Object> inputmap = new HashMap<>();
		getInputMapForAlertsService(isOrchCall, inputmap, formattedAlertFreqrecords);
		diagnostic.prepareDebug("inputmap for alertsservice " + inputmap).log();
		callAlertsService(inputmap);
	}

	private void getInputMapForAlertsService(boolean isOrchCall, Map<String, Object> inputmap,
			JsonArray formattedAlertFreqrecords) {
		if (isOrchCall) {
			StringBuilder jsonObjects = new StringBuilder();
			for (JsonElement record : formattedAlertFreqrecords) {
				JsonArray customerRecord = new JsonArray();
				customerRecord.add(record.getAsJsonObject());
				jsonObjects.append(customerRecord).append(Constants.LOOPSEPARATORVAL);
			}
			inputmap.put("loop_count", formattedAlertFreqrecords.size());
			inputmap.put("loop_seperator", Constants.LOOPSEPARATORVAL);
			inputmap.put("CustomerDetails", jsonObjects);
		} else {
			inputmap.put("loop_count", 1);
			inputmap.put("loop_seperator", Constants.LOOPSEPARATORVAL);
			inputmap.put("CustomerDetails", formattedAlertFreqrecords);
		}

	}

	private JsonArray formatCustomerAlertFrequencyRecords(JsonArray customerAlertFrequencyRecords) {
		Map<String, Object> inputmap = new HashMap<>();
		getInputMapForBackendIds(inputmap, customerAlertFrequencyRecords);
		diagnostic.prepareDebug("backendid inputmap " + inputmap).log();
		try {
			String backendIds = DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(Constants.GETBACKENDIDS_OP,
							CustomerDetailsResourceImpl.getSchemaName()))
					.withRequestParameters(inputmap).withServiceId(Constants.REMINDERENGINEDBSERVICE).build()
					.getResponse();
			Map<String, List<String>> customerIdCoreIdMap = getCustomerIdCoreIdMap(backendIds);
			return formatRecords(customerAlertFrequencyRecords, customerIdCoreIdMap);

		} catch (DBPApplicationException e) {
			alert.prepareError(e.toString()).log();
		}
		return new JsonArray();
	}

	private void getInputMapForBackendIds(Map<String, Object> inputmap, JsonArray customerAlertFrequencyRecords) {
		String backendType = Constants.BACKENDTYPE;
		try {
			backendType = HelperMethods.getConfigProperty("REMINDER_ENGINE_BACKENDTYPE");
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		if (backendType == null)
			backendType = "null";
		Set<String> customerIdSet = new HashSet<>();
		customerAlertFrequencyRecords.forEach(jsonElement -> {
			if (jsonElement.isJsonObject())
				customerIdSet.add(jsonElement.getAsJsonObject().get(Constants.CUSTOMERID).getAsString());
		});
		String customerIds = String.join(",", customerIdSet);
		inputmap.put(Constants.CUSTOMERIDS, customerIds);
		inputmap.put(Constants.BACKENDTYPECOL, backendType);

	}

	private JsonArray formatRecords(JsonArray customerAlertFrequencyRecords,
			Map<String, List<String>> customerIdCoreIdMap) {
		JsonArray formattedAlertFreqRecords = new JsonArray();
		for (JsonElement customerAlertFreqElement : customerAlertFrequencyRecords) {
			JsonObject customerAlertFreqObj = customerAlertFreqElement.getAsJsonObject();
			String customerAlertString = customerAlertFreqObj.toString();
			List<String> backendIds = customerIdCoreIdMap
					.get(customerAlertFreqObj.get(Constants.CUSTOMERID).getAsString());
			if (backendIds != null) {
				for (String backendId : backendIds) {
					JsonObject temp = new JsonObject();
					temp = new JsonParser().parse(customerAlertString).getAsJsonObject();
					temp.addProperty(Constants.BACKENDTYPE, backendId);
					formattedAlertFreqRecords.add(temp);
				}
			} else
				formattedAlertFreqRecords.add(customerAlertFreqObj);
		}
		return formattedAlertFreqRecords;
	}

	private Map<String, List<String>> getCustomerIdCoreIdMap(String backendIds) {
		Map<String, List<String>> customerIdCoreIdMap = new HashMap<>();
		JsonArray recordsArray = new JsonParser().parse(backendIds).getAsJsonObject().getAsJsonArray(Constants.RECORDS);
		for (JsonElement record : recordsArray) {
			JsonObject res = record.getAsJsonObject();
			try {
				String customerid = res.get(Constants.CUSTOMERIDCOL).getAsString();
				String backendid = res.get(Constants.BACKENDID).getAsString();
				if (customerid != null && backendid != null) {
					List<String> coreCustomeridlist = new ArrayList<>();
					if (customerIdCoreIdMap.containsKey(customerid)) {
						coreCustomeridlist = customerIdCoreIdMap.get(customerid);
					}
					coreCustomeridlist.add(backendid);
					customerIdCoreIdMap.put(customerid, coreCustomeridlist);
				}

			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
			}
		}
		return customerIdCoreIdMap;
	}

	private void callAlertsService(Map<String, Object> inputmap) {
		try {
			String responseString = DBPServiceExecutorBuilder.builder().withOperationId(Constants.ORCH_OPERATION)
					.withRequestParameters(inputmap).withServiceId(Constants.ORCH_SERVICE).build().getResponse();
			diagnostic.prepareDebug("response from alerts service " + responseString).log();
		} catch (Exception e) {
			alert.prepareError("exception occurred").log();
		}

	}

	private String getTime(LocalDateTime synctimestamp) {
		DateTimeFormatter dtftime = DateTimeFormatter.ofPattern(Constants.TIMEFORMATTER);
		try {
			if (synctimestamp != null)
				return synctimestamp.format(dtftime);
		} catch (Exception e) {
			alert.prepareError("Exception occured while getting current time", e).log();
		}
		return null;
	}

	public String getDay(LocalDateTime timestamp) {
		String day = "";
		try {
			day = Integer.toString(timestamp.getDayOfWeek().getValue());
		} catch (Exception e) {
			alert.prepareError("Exception occurred ", e).log();
		}
		return day;
	}

	public String getDayName(LocalDateTime timestamp) {
		String day = "";
		try {
			return timestamp.getDayOfWeek().name();
		} catch (Exception e) {
			alert.prepareError("Exception occurred ", e).log();
		}
		return day;
	}

	private LocalDateTime getCurrentTimestamp(String zoneOffSetStr) {
		try {
			if (HelperMethods.getConfigProperty("REMINDER_ENGINE_CURRENT_TIMESTAMP") != null)
				return LocalDateTime
						.parse(HelperMethods.getConfigProperty("REMINDER_ENGINE_CURRENT_TIMESTAMP").replace(" ", "T"));
			zoneOffSetStr = zoneOffSetStr.equals("UTC") ? "Z" : zoneOffSetStr.replace("UTC", "");
			return OffsetDateTime.now(ZoneOffset.of(zoneOffSetStr)).toLocalDateTime();
		} catch (Exception e) {
			alert.prepareError("Exception occurred while getting current timestamp ", e).log();
		}
		return null;
	}

	private LocalDateTime getLastsyncTime() {
		try {
			Result response = null;
			response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(Constants.GETLASTSYNCTIME_OPERATION,
							CustomerDetailsResourceImpl.getSchemaName()))
					.withRequestParameters(new HashMap<>()).withServiceId(Constants.REMINDERENGINEDBSERVICE).build()
					.getResult();
			Dataset rr = response.getDatasetById(Constants.DATASETID);
			if (rr == null)
				return null;
			List<Record> rec = rr.getAllRecords();
			if (rec == null || rec.isEmpty())
				return null;
			if (rec.get(0).getParamByName(Constants.LASTSYNCTIME) != null
					&& rec.get(0).getParam(Constants.LASTSYNCTIME).getValue() != null) {
				return LocalDateTime
						.parse(rec.get(0).getParamByName(Constants.LASTSYNCTIME).getValue().replace(" ", "T"));
			}
		} catch (Exception e) {
			alert.prepareError("Exception occurred", e).log();
		}
		return null;
	}

	private static void setConfigLevel() {
		Map<String, Object> inputmap = new HashMap<>();
		Result response = null;
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(Constants.CUSTOMERVIEWALERTCONFIGURATION_GET,
							CustomerDetailsResourceImpl.getSchemaName()))
					.withRequestParameters(inputmap).withServiceId(Constants.REMINDERENGINEDBSERVICE).build()
					.getResult();
			if (response != null) {
				Dataset ds = response.getDatasetById("customerviewalertconfiguration");
				if (ds != null) {
					List<Record> records = ds.getAllRecords();
					if (records != null && !records.isEmpty())
						configlevel = records.get(0).getParamValueByName("alertPreferenceView");
				}
			}
		} catch (DBPApplicationException e) {
			alert.prepareError("Error in fetching alertview configuration.", e).log();
		}
	}

	public static void main(String[] args) {
		CustomerDetailsBusinessDelegateImpl c = new CustomerDetailsBusinessDelegateImpl();
		System.out.println(c.getDay(LocalDateTime.now()));
		System.out.println(c.getDayName(LocalDateTime.now()));
	}
}
