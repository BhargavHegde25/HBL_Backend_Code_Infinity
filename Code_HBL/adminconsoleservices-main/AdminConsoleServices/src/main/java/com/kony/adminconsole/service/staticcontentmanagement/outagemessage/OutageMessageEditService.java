package com.kony.adminconsole.service.staticcontentmanagement.outagemessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.DateUtils;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class OutageMessageEditService implements JavaService2 {

	private static final String UPDATE_OUTAGEMESSAGE = "updateOutageMessage";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final SimpleDateFormat TARGET_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
	private static final SimpleDateFormat INPUT_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static final SimpleDateFormat INPUT_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			if (methodID.equalsIgnoreCase(UPDATE_OUTAGEMESSAGE)) {
				return editOutageMessage(requestInstance);
			} else {
				// Bulk update method id : bulkUpdateOutageMessage
				return updateStatusForListOfMessages(requestInstance);
			}

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			errorResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			return errorResult;
		} catch (NumberFormatException e) {
			Result errorResult = new Result();
			ErrorCodeEnum.ERR_20193.setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			errorResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			return errorResult;
		}
	}

	private static JSONArray getOutageMessageLinkedApps(DataControllerRequest requestInstance, String outageMessage)
			throws ApplicationException {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "Outagemessage_id eq '" + outageMessage + "'");
		JSONObject readResponse = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.OUTAGEMESSAGEAPP_READ, postParametersMap, null, requestInstance));
		if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
				|| readResponse.getInt(FabricConstants.OPSTATUS) != 0 || !readResponse.has("outagemessageapp")) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20182);
		}
		JSONArray result = new JSONArray();
		JSONArray readResponseArray = readResponse.getJSONArray("outagemessageapp");
		readResponseArray.forEach((recordObject) -> {
			result.put(((JSONObject) recordObject).getString("App_id"));
		});
		return result;
	}

	private static Result editOutageMessage(DataControllerRequest requestInstance)
			throws ApplicationException, NumberFormatException, ParseException {
		Result result = new Result();

		String id = requestInstance.getParameter("id");
		String status_id = requestInstance.getParameter("Status_id");
		String outageName = requestInstance.getParameter("Name");
		String outageText = requestInstance.getParameter("MessageText");
		String app_ids_Added = requestInstance.getParameter("App_ids_Added");
		String app_ids_Removed = requestInstance.getParameter("App_ids_Removed");
		String startTime = requestInstance.getParameter("Start_Time");
		String endTime = requestInstance.getParameter("End_Time");
		String timezoneOffset = requestInstance.getParameter("timezoneOffset");
		
		String filter = "id eq '"+ id + "'";
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, filter.toString());
		JSONObject readOutageMessages = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.OUTAGEMESSAGE_READ, postParametersMap, null, requestInstance));

		if (readOutageMessages == null || !readOutageMessages.has(FabricConstants.OPSTATUS)
				|| readOutageMessages.getInt(FabricConstants.OPSTATUS) != 0
				|| !readOutageMessages.has("outagemessage")) {
			ErrorCodeEnum.ERR_20185.setErrorCode(result);
			return result;
		}
		JSONObject outageDetails = readOutageMessages.getJSONArray("outagemessage").getJSONObject(0);
		String status = outageDetails.getString("Status_id");
		Date start = INPUT_FORMAT2.parse(outageDetails.get("startTime").toString());
		Date currTime = new Date();
		if (status.equalsIgnoreCase("SID_OUTAGE_TERMINATED")
				|| status.equalsIgnoreCase("SID_OUTAGE_SCHEDULED_ACTIVE_COMPLETED") && currTime.after(start)) {
			ErrorCodeEnum.ERR_20183.setErrorCode(result);
			return result;
		}
		
		if (!validateInputForUpdateOutageMessage(requestInstance, result)) {
			return result;
		}
		if (StringUtils.isBlank(timezoneOffset)) {
			ErrorCodeEnum.ERR_20192.setErrorCode(result);
			return result;
		}

		if (StringUtils.isNotBlank(app_ids_Removed)) {
			// Remove App to outage message mapping
			removeAppMapping(requestInstance, result, id, app_ids_Removed);
			if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
				return result;
			}
		}

		if (StringUtils.isNotBlank(app_ids_Added)) {
			// Add App to outage message mapping
			addAppMapping(requestInstance, result, id, app_ids_Added);
			if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
				return result;
			}
		}

		JSONArray appIdsArray = getOutageMessageLinkedApps(requestInstance, id);
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)
				&& !OutageMessageCreateService.validateStartAndEndTimes(requestInstance, result,
						DateUtils.convertLocalDateTimeToServerZone(startTime, Integer.parseInt(timezoneOffset)),
						DateUtils.convertLocalDateTimeToServerZone(endTime, Integer.parseInt(timezoneOffset)),
						appIdsArray)) {
			result.addParam(
					new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_20197.getErrorCode())));
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY,
					requestInstance.getAttribute("MaxActiveMessages") + ErrorCodeEnum.ERR_20197.getMessage()));
			return result;
		}

		if (StringUtils.isNotBlank(outageName)
				&& !OutageMessageCreateService.validateName(requestInstance, result, outageName, id)) {
			ErrorCodeEnum.ERR_20198.setErrorCode(result);
			return result;
		}

		UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
		postParametersMap = new HashMap<String, String>();
		postParametersMap.put("id", id);

//		if (StringUtils.isNotBlank(status_id)) {
//			postParametersMap.put("Status_id", status_id);
//		}

		if (StringUtils.isNotBlank(outageName)) {
			postParametersMap.put("name", outageName);
		}

		if (StringUtils.isNotBlank(outageText)) {
			postParametersMap.put("MessageText", outageText);
		}

		if (StringUtils.isNotBlank(startTime)) {
			startTime = DateUtils.convertLocalDateTimeToServerZone(startTime, Integer.parseInt(timezoneOffset));
			postParametersMap.put("startTime", startTime);
		}

		if (StringUtils.isNotBlank(endTime)) {
			endTime = DateUtils.convertLocalDateTimeToServerZone(endTime, Integer.parseInt(timezoneOffset));
			postParametersMap.put("endTime", endTime);
		}

		postParametersMap.put("createdby", userDetailsBeanInstance.getUserName());
		postParametersMap.put("modifiedby", userDetailsBeanInstance.getUserName());

		String updateResponse = Executor.invokeService(ServiceURLEnum.OUTAGEMESSAGE_UPDATE, postParametersMap, null,
				requestInstance);
		JSONObject updateJSON = CommonUtilities.getStringAsJSONObject(updateResponse);
		if (updateJSON == null || !updateJSON.has(FabricConstants.OPSTATUS)
				|| updateJSON.getInt(FabricConstants.OPSTATUS) != 0) {
			result.addParam(new Param("Status", "Error", FabricConstants.STRING));
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.OUTAGEMESSAGE, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED,
					"Outage message update failed. Outage name: " + outageName + " Outage Message: " + outageText
							+ " Start time: " + TARGET_FORMAT.format(INPUT_FORMAT1.parse(startTime)) + " UTC."
							+ " End time: " + TARGET_FORMAT.format(INPUT_FORMAT1.parse(endTime)) + " UTC");
			ErrorCodeEnum.ERR_20182.setErrorCode(result);
			return result;
		}

		result.addParam(new Param("Status", "Success", FabricConstants.STRING));
		AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.OUTAGEMESSAGE, EventEnum.UPDATE,
				ActivityStatusEnum.SUCCESSFUL,
				"Outage message update successful. Outage name: " + outageName + " Outage Message: " + outageText
						+ " Start time: " + TARGET_FORMAT.format(INPUT_FORMAT1.parse(startTime)) + " UTC."
						+ " End time: " + TARGET_FORMAT.format(INPUT_FORMAT1.parse(endTime)) + " UTC");
		return result;
	}

	private static Result updateStatusForListOfMessages(DataControllerRequest requestInstance)
			throws ApplicationException, JSONException, ParseException {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		Result result = new Result();

		requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
		JSONArray OutageMessageIds = new JSONArray(requestInstance.getParameter("OutageMessageIds"));
		StringBuilder filter = new StringBuilder("");
		OutageMessageIds.forEach((outageObject) -> {
			JSONObject outage = (JSONObject) outageObject;
			if (StringUtils.isNotBlank(filter)) {
				filter.append(" or ");
			}
			filter.append("id eq '" + outage.getString("id") + "'");
		});
		postParametersMap.put(ODataQueryConstants.FILTER, filter.toString());
		JSONObject readOutageMessages = CommonUtilities.getStringAsJSONObject(
				Executor.invokeService(ServiceURLEnum.OUTAGEMESSAGE_READ, postParametersMap, null, requestInstance));

		if (readOutageMessages == null || !readOutageMessages.has(FabricConstants.OPSTATUS)
				|| readOutageMessages.getInt(FabricConstants.OPSTATUS) != 0
				|| !readOutageMessages.has("outagemessage")) {
			ErrorCodeEnum.ERR_20185.setErrorCode(result);
			return result;
		}
		Map<String, JSONObject> outageDetails = OutageMessageDeleteService
				.getOutageMap(readOutageMessages.getJSONArray("outagemessage"));
		postParametersMap.clear();
		for (int i = 0; i < OutageMessageIds.length(); i++) {
			postParametersMap.clear();
			JSONObject currentRecord = OutageMessageIds.getJSONObject(i);
			if( !outageDetails.containsKey(currentRecord.getString("id"))){
				continue;
			}
			String status = currentRecord.getString("Status_id");
			String currStatus = outageDetails.get(currentRecord.getString("id")).getString("Status_id");
			Date startTime = INPUT_FORMAT2.parse(outageDetails.get(currentRecord.getString("id")).getString("startTime"));
			Date endTime = INPUT_FORMAT2.parse(outageDetails.get(currentRecord.getString("id")).getString("endTime"));
			Date currTime = new Date();
			if (!(currStatus.equalsIgnoreCase("SID_OUTAGE_TERMINATED"))
					&& ((status.equalsIgnoreCase("SID_OUTAGE_SCHEDULED_ACTIVE_COMPLETED")||
							status.equalsIgnoreCase("SID_OUTAGE_PAUSED")) && currTime.before(endTime) && currTime.after(startTime))) {
				postParametersMap.put("id", currentRecord.getString("id"));
				if (currentRecord.has("Status_id")) {
					postParametersMap.put("Status_id", currentRecord.getString("Status_id"));
				}

				UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
				postParametersMap.put("modifiedby", userDetailsBeanInstance.getUserName());

				String updateOutageMessageResponse = Executor.invokeService(ServiceURLEnum.OUTAGEMESSAGE_UPDATE,
						postParametersMap, null, requestInstance);
				JSONObject updateOutageMessageURLJSON = CommonUtilities
						.getStringAsJSONObject(updateOutageMessageResponse);
				if (updateOutageMessageURLJSON != null && updateOutageMessageURLJSON.has(FabricConstants.OPSTATUS)
						&& updateOutageMessageURLJSON.getInt(FabricConstants.OPSTATUS) == 0) {
					result.addParam(new Param("Status", "Success", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.OUTAGEMESSAGE, EventEnum.UPDATE,
							ActivityStatusEnum.SUCCESSFUL,
							"Outage message status updated successfully. Status: "
									+ currentRecord.getString("Status_id") + " Outage name: "
									+ outageDetails.get(currentRecord.getString("id")).getString("name")
									+ " Outage Message: "
									+ outageDetails.get(currentRecord.getString("id")).getString("MessageText")
									+ " Start time: "
									+ TARGET_FORMAT.format(INPUT_FORMAT2.parse(
											outageDetails.get(currentRecord.getString("id")).getString("startTime")))
									+ " UTC." + " End time: "
									+ TARGET_FORMAT.format(INPUT_FORMAT2.parse(
											outageDetails.get(currentRecord.getString("id")).getString("endTime")))
									+ " UTC");
				} else {
					result.addParam(new Param("Status", "Error", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.OUTAGEMESSAGE, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Outage message status update has failed. Status: " + currentRecord.getString("Status_id")
									+ " Outage name: "
									+ outageDetails.get(currentRecord.getString("id")).getString("name")
									+ " Outage Message: "
									+ outageDetails.get(currentRecord.getString("id")).getString("MessageText")
									+ " Start time: "
									+ TARGET_FORMAT.format(INPUT_FORMAT2.parse(
											outageDetails.get(currentRecord.getString("id")).getString("startTime")))
									+ " UTC." + " End time: "
									+ TARGET_FORMAT.format(INPUT_FORMAT2.parse(
											outageDetails.get(currentRecord.getString("id")).getString("endTime")))
									+ " UTC");
					ErrorCodeEnum.ERR_20183.setErrorCode(result);
					result.addParam(new Param("FailureReason", "CRUD Operation failed!", FabricConstants.STRING));
					return result;
				}
			}
		}
		return result;
	}

	static void addAppMapping(DataControllerRequest requestInstance, Result result, String id, String app_ids_Added) {
		JSONArray appIdsArray = new JSONArray(app_ids_Added);
		for (int i = 0; i < appIdsArray.length(); i++) {
			Map<String, String> postParameters = new HashMap<String, String>();
			postParameters.put("Outagemessage_id", id);
			postParameters.put("App_id", appIdsArray.getString(i));
			String createBackendResponseJSON = Executor.invokeService(ServiceURLEnum.OUTAGEMESSAGEAPP_CREATE,
					postParameters, null, requestInstance);
			JSONObject createBackendResponse = CommonUtilities.getStringAsJSONObject(createBackendResponseJSON);
			if (createBackendResponse == null || !createBackendResponse.has(FabricConstants.OPSTATUS)
					|| createBackendResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				result.addParam(new Param("Status", "Error", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.OUTAGEMESSAGE, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED, "Outage message create failed");
				result.addParam(new Param("FailureReason", "CRUD Operation failed!", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20194.setErrorCode(result);
				return;
			}
		}
	}

	private static void removeAppMapping(DataControllerRequest requestInstance, Result result, String id,
			String app_ids_Added) {
		JSONArray appIdsArray = new JSONArray(app_ids_Added);
		for (int i = 0; i < appIdsArray.length(); i++) {
			Map<String, String> postParameters = new HashMap<String, String>();
			postParameters.put("Outagemessage_id", id);
			postParameters.put("App_id", appIdsArray.getString(i));
			String deleteBackendResponseJSON = Executor.invokeService(ServiceURLEnum.OUTAGEMESSAGEAPP_DELETE,
					postParameters, null, requestInstance);
			JSONObject deleteBackendResponse = CommonUtilities.getStringAsJSONObject(deleteBackendResponseJSON);
			if (deleteBackendResponse == null || !deleteBackendResponse.has(FabricConstants.OPSTATUS)
					|| deleteBackendResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				result.addParam(new Param("Status", "Error", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.OUTAGEMESSAGE, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED, "Outage message create failed.");
				ErrorCodeEnum.ERR_20194.setErrorCode(result);
				result.addParam(new Param("FailureReason", "CRUD Operation failed!", FabricConstants.STRING));
				return;
			}
		}
	}

	private static boolean validateInputForUpdateOutageMessage(DataControllerRequest requestInstance, Result result) {
		final int SHORT_LENGTH = 25;
		final int MEDIUM_LENGTH = 50;
		final int LONG_LENGTH = 200;
		String id = requestInstance.getParameter("id");
		String status_id = requestInstance.getParameter("Status_id");
		String outageName = requestInstance.getParameter("Name");
		String outageText = requestInstance.getParameter("MessageText");
		String app_ids_Added = requestInstance.getParameter("App_ids_Added");
		String app_ids_Removed = requestInstance.getParameter("App_ids_Removed");
		String startTime = requestInstance.getParameter("Start_Time");
		String endTime = requestInstance.getParameter("End_Time");

		String regex = "[^a-zA-Z0-9_ ]"; // Any char except a-z or A-Z or 0-9
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = null;

		// Validate id
		try {
			UUID.fromString(id);
		} catch (Exception e) {
			result.addParam("dbpErrCode", "20541", FabricConstants.INT);
			result.addParam("Status", "Error");
			result.addParam("dbpErrMsg", "Invalid Id");
			return false;
		}

		// validate status_id
		m = p.matcher(status_id);
		if (m.find() || status_id.length() > MEDIUM_LENGTH) {
			result.addParam("dbpErrCode", "20541", FabricConstants.INT);
			result.addParam("Status", "Error");
			result.addParam("dbpErrMsg", "Invalid Status_id");
			return false;
		}

		// validate outageName
		m = p.matcher(outageName);
		if (m.find() || outageName.length() > SHORT_LENGTH) {
			result.addParam("dbpErrCode", "20541", FabricConstants.INT);
			result.addParam("Status", "Error");
			result.addParam("dbpErrMsg", "Invalid outageName");
			return false;
		}

		// validate outageText
		String regex1 = "[^a-zA-Z0-9_.,\':; -]"; // Any char except a-z or A-Z or 0-9 and other special characters
		Pattern p1 = Pattern.compile(regex1, Pattern.CASE_INSENSITIVE);
		m = p1.matcher(outageText);
		alert.prepareError("outageText.length() > LONG_LENGTH " + (outageText.length() > LONG_LENGTH)).log();
		if (m.find() || outageText.length() > LONG_LENGTH) {
			result.addParam("dbpErrCode", "20541", FabricConstants.INT);
			result.addParam("Status", "Error");
			result.addParam("dbpErrMsg", "Invalid Text");
			return false;
		}

		// startTime endTime timezoneOffset
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		try {
			Date start = dateFormat.parse(startTime.trim());
			Date end = dateFormat.parse(endTime.trim());
			if (start.after(end)) {
				result.addParam("dbpErrCode", "20541", FabricConstants.INT);
				result.addParam("Status", "Error");
				result.addParam("dbpErrMsg", "Invalid Start Date or End Date");
				return false;
			}
		} catch (ParseException pe) {
			result.addParam("dbpErrCode", "20541", FabricConstants.INT);
			result.addParam("Status", "Error");
			result.addParam("dbpErrMsg", "Invalid Start Date or End Date");
			return false;
		}

		// app_ids_Added app_ids_Removed
		JsonArray appIdsAddedJson = null;
		try {
			appIdsAddedJson = new JsonParser().parse(app_ids_Added).getAsJsonArray();
		} catch (Exception e) {
			result.addParam("dbpErrCode", "20541", FabricConstants.INT);
			result.addParam("Status", "Error");
			result.addParam("dbpErrMsg", "Invalid input payload for app_ids_Added");
			return false;
		}

		if (appIdsAddedJson != null && appIdsAddedJson.size() > 0) {
			for (int i = 0; i < appIdsAddedJson.size(); i++) {
				String appId = appIdsAddedJson.get(i).getAsString();
				m = p.matcher(appId);
				if (m.find() || appId.length() > MEDIUM_LENGTH) {
					result.addParam("dbpErrCode", "20541", FabricConstants.INT);
					result.addParam("Status", "Error");
					result.addParam("dbpErrMsg", "Invalid AppId");
					return false;
				}
			}
		}

		JsonArray appIdRemovedJson = null;
		try {
			appIdRemovedJson = new JsonParser().parse(app_ids_Removed).getAsJsonArray();
		} catch (Exception e) {
			result.addParam("dbpErrCode", "20541", FabricConstants.INT);
			result.addParam("Status", "Error");
			result.addParam("dbpErrMsg", "Invalid input payload for app_ids_Removed");
			return false;
		}

		if (appIdRemovedJson != null && appIdRemovedJson.size() > 0) {
			for (int i = 0; i < appIdRemovedJson.size(); i++) {
				String appId = appIdRemovedJson.get(i).getAsString();
				m = p.matcher(appId);
				if (m.find() || appId.length() > MEDIUM_LENGTH) {
					result.addParam("dbpErrCode", "20541", FabricConstants.INT);
					result.addParam("Status", "Error");
					result.addParam("dbpErrMsg", "Invalid AppId");
					return false;
				}
			}
		}
		return true;
	}
}
