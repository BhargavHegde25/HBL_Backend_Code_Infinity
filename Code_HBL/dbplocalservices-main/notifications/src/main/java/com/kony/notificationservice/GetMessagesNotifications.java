package com.kony.notificationservice;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;

public class GetMessagesNotifications implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			String customerId = null;
			customerId = HelperMethods.getCustomerIdFromSession(requestInstance);
			if(StringUtils.isBlank(customerId)) {
				Result result = new Result();
				ErrorCodeEnum.ERR_10001.setErrorCode(result);
				return result;
			}
			Result finalResult = new Result();
			Result combinedResult = new Result();
			if (StringUtils.isNotBlank(customerId)) {
				combinedResult = getMessagesNotificationDB(customerId, requestInstance);
			}
			addUnreadMessagesCount(combinedResult, finalResult);
			processUnReadNotifications(combinedResult, finalResult);
			Dataset ds = combinedResult.getDatasetById("records3");
			if (ds != null) {
				ds.setId("records");
				finalResult.addDataset(ds);
			} else {
				ds = new Dataset();
				ds.setId("records");
			}
			finalResult.addDataset(ds);
			return finalResult;
		} catch (Exception e) {
			Result res = new Result();
			alert.prepareError("Error while getting Messages and Notifications", e).log();
			ErrorCodeEnum.ERR_10001.setErrorCode(res);
			return res;
		}
	}

	public static JSONObject getClientAppIdMap() {
		try {
			String clientAppIdMapping = EnvironmentConfigurationsHandler.getServerProperty("AC_APPID_TO_APP_MAPPING");
			if (StringUtils.isNotBlank(clientAppIdMapping)) {
				JSONObject clientAppIdMappingJson = new JSONObject(clientAppIdMapping);
				return clientAppIdMappingJson;
			}
		} catch (Exception e) {
			alert.prepareError("Failed while parsing runtime configuration AC_APPID_TO_APP_MAPPING", e).log();
		}
		return new JSONObject();
	}

	private void addUnreadMessagesCount(Result combinedResult, Result finalResult) {
		Dataset ds = combinedResult.getDatasetById("records");
		if (null != ds && null != ds.getAllRecords() && !ds.getAllRecords().isEmpty()) {
			String unreadMessageCount = HelperMethods.getParamValue(ds.getRecord(0).getParam("messageCount"));
			finalResult.addParam(new Param("unreadMessageCount", unreadMessageCount, "String"));
		}
		ds = combinedResult.getDatasetById("records1");
		if (null != ds && null != ds.getAllRecords() && !ds.getAllRecords().isEmpty()) {
			String priorityMessageCount = HelperMethods.getParamValue(ds.getRecord(0).getParam("priorityMessageCount"));
			finalResult.addParam(new Param("priorityMessageCount", priorityMessageCount, "String"));
		}
	}
	
	private void processUnReadNotifications(Result combinedResult, Result finalResult) {
		Dataset ds = combinedResult.getDatasetById("records2");
		String unreadMessageCount = "";
		if (null != ds && null != ds.getAllRecords() && !ds.getAllRecords().isEmpty()) {
			unreadMessageCount = HelperMethods.getParamValue(ds.getRecord(0).getParam("notificationCount"));
		}
		Dataset retds = new Dataset("notifications");
		Record rec = new Record();
		Param p = new Param(DBPUtilitiesConstants.UNREAD_COUNT, unreadMessageCount,
				DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		rec.addParam(p);
		retds.addRecord(rec);
		finalResult.addDataset(retds);
	}
	
	private Result getMessagesNotificationDB(String customerId, DataControllerRequest dcRequest) {
		Result result = new Result();
		String serverAppId = null;
		String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
		String clientAppId = "";
		if (StringUtils.isNotBlank(reportingParams)) {
			JSONObject reportingParamsJson;
			try {
				reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
				clientAppId = reportingParamsJson.optString("aid");
			} catch (Exception e) {
				alert.prepareError("Failed while parsing runtime configuration AC_APPID_TO_APP_MAPPING", e).log();
			}
			if (StringUtils.isNotBlank(clientAppId)) {
				JSONObject clientAppIdToServerMap = getClientAppIdMap();
				if (clientAppIdToServerMap.has(clientAppId)) {
					serverAppId = clientAppIdToServerMap.getString(clientAppId);
				}
			}
		}
		Calendar cal = Calendar.getInstance();
		String toDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), null);
		cal.add(Calendar.DATE, -30);
		String fromDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), null);
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("_customerId", customerId);
		inputParams.put("_startdate", fromDate);
		inputParams.put("_enddate", toDate);
		inputParams.put("_serverAppId", (serverAppId == null) ? "" : serverAppId);
		alert.prepareError("inputParams: " + inputParams).log();
		try {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.GETMESSAGESNOTIFICATIONS_PROC_GET);
		} catch (Exception e) {
			return result;
		}
		return result;
	}
}
