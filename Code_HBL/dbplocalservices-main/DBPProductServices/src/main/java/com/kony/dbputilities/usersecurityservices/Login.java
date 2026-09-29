package com.kony.dbputilities.usersecurityservices;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.metrics.DBPMetricData;
import com.kony.dbputilities.metrics.DBPMetricsProcessorHelper;
import com.kony.dbputilities.sessionmanager.SessionManager;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MetricsException;

public class Login implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.COREUSER_GET);
			result = postProcess(inputParams, dcRequest, result);
		}

		return result;
	}

	private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws Exception {
		Result retVal = null;
		String password = inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
		String pin = inputParams.get(DBPUtilitiesConstants.PIN);
		String sharedSecret = inputParams.get("sharedSecret");
		String sessionToken = inputParams.get("session_token");
		if (StringUtils.isNotBlank(pin) && !"$pin".equalsIgnoreCase(pin)) {
			retVal = postProcessForPin(inputParams, dcRequest, result);
		} else if (StringUtils.isNotBlank(sessionToken) && !"$session_token".equalsIgnoreCase(sessionToken)
				&& (StringUtils.isBlank(password) || "$password".equalsIgnoreCase(password))
				&& (StringUtils.isBlank(sharedSecret) || "$sharedSecret".equalsIgnoreCase(sharedSecret))
				&& (StringUtils.isBlank(pin) || "$pin".equalsIgnoreCase(pin))) {
			retVal = postProcessForST(inputParams, dcRequest, result);
		} else {
			retVal = postProcessForUserName(inputParams, dcRequest, result);
		}
		return retVal;
	}

	private Result postProcessForST(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		Result retVal = new Result();
		if (HelperMethods.hasRecords(result)) {
			if (isSessionExpired(result.getAllDatasets().get(0).getRecord(0))) {
				Param p = new Param("message", "session token is expired", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				retVal.addParam(p);
				removeSessionToken(dcRequest, result);
			} else {
				retVal = sessionAttributes(result);
			}
		} else {
			Param p = new Param("message", "Invalid token", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	private void removeSessionToken(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		input.put("Id", HelperMethods.getFieldValue(result, "id"));
		input.put("Session_id", "");
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.COREUSER_UPDATE);
	}

	private Result postProcessForPin(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws MetricsException, ParseException, HttpCallException {
		Result retVal = new Result();
		if (HelperMethods.hasRecords(result)) {
			String deviceId = inputParams.get("deviceId");
			if (!isDeviceRegistered(dcRequest, deviceId, HelperMethods.getFieldValue(result, "id"))) {
				Param p = new Param("message", "Please register your device to Use Pin",
						DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				retVal.addParam(p);
			} else {
				Result user = validatePin(dcRequest, inputParams.get("username"), inputParams.get("pin"));
				if (HelperMethods.hasRecords(user)) {
					retVal = sessionAttributes(user);
					updateUserDetails(dcRequest, user, retVal);
				} else {
					Param p = new Param("message", "Invalid Pin", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
					retVal.addParam(p);
				}
			}
		} else {
			Param p = new Param("message", "Invalid username", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retVal.addParam(p);
		}
		return retVal;
	}

	private boolean isDeviceRegistered(DataControllerRequest dcRequest, String deviceId, String userId)
			throws HttpCallException {
		StringBuilder sb = new StringBuilder();
		sb.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId).append(DBPUtilitiesConstants.AND)
				.append("DeviceId").append(DBPUtilitiesConstants.EQUAL).append(deviceId);
		Result device = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.DEVICEREGISTRATION_GET);
		return HelperMethods.hasRecords(device);
	}

	private Result validatePin(DataControllerRequest dcRequest, String username, String pin) throws HttpCallException {
		StringBuilder sb = new StringBuilder();
		sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(username).append(DBPUtilitiesConstants.AND)
				.append("pin").append(DBPUtilitiesConstants.EQUAL).append(pin);
		return HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_GET);
	}

	private Result postProcessForUserName(Map<String, String> inputParams, DataControllerRequest dcRequest,
			Result result) throws Exception {
		Result retVal = new Result();
		String username = inputParams.get(DBPInputConstants.USR_NAME);
		if (HelperMethods.hasRecords(result)) {
			if (isUserExpired(result.getAllDatasets().get(0).getRecord(0))) {
				Param p = new Param("message", "Your trial period has expired. Please create another user",
						DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				retVal.addParam(p);
			} else if (isUserLocked(result.getAllDatasets().get(0).getRecord(0))) {
				Param p = new Param("message", "user is locked", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				retVal.addParam(p);
				pushCustomMetricData(dcRequest, "user_status", "locked");
			} else {
				retVal = sessionAttributes(result);
				updateUserDetails(dcRequest, result, retVal);
			}
		} else {
			Record user = getUser(dcRequest, username);
			if (null != user && !"konyrbdev".equalsIgnoreCase(username) && !"konyolbuser".equalsIgnoreCase(username)
					&& !"admin".equalsIgnoreCase(username)) {
				if (isUserLocked(user)) {
					Param p = new Param("message", "user is locked", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
					retVal.addParam(p);
					pushCustomMetricData(dcRequest, "user_status", "locked");
				} else {
					updateLockCount(dcRequest, user);
					Param p = new Param("message", "Incorrect username or password",
							DBPConstants.FABRIC_STRING_CONSTANT_KEY);
					retVal.addParam(p);
					pushCustomMetricData(dcRequest, "login_status", "failed");
				}

			}
			if (null == user) {
				Param p = new Param("message", "Incorrect username or password",
						DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				retVal.addParam(p);
			}
		}
		return retVal;
	}

	private boolean isUserExpired(Record user) {
		String isEnrolled = HelperMethods.getFieldValue(user, "isEnrolled");
		String validDate = HelperMethods.getFieldValue(user, "ValidDate");
		if (("1".equals(isEnrolled) || "true".equalsIgnoreCase(isEnrolled))
				&& new Date().after(HelperMethods.getFormattedTimeStamp(validDate))) {
			return true;
		}
		return false;
	}

	private boolean isSessionExpired(Record user) {
		Date currentLoginTime = HelperMethods
				.getFormattedTimeStamp(HelperMethods.getFieldValue(user, "currentLoginTime"));
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentLoginTime);
		cal.add(Calendar.MINUTE, 10);
		currentLoginTime = cal.getTime();
		if (now.after(currentLoginTime)) {
			return false;
		}
		return false;
	}

	private void updateUserDetails(DataControllerRequest dcRequest, Result result, Result retVal)
			throws HttpCallException, ParseException {
		Map<String, String> input = new HashMap<>();
		input.put("lastlogintime", HelperMethods.getCurrentTimeStamp());
		if (StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "CurrentLoginTime"))) {
			input.put("lastlogintime",
					HelperMethods.convertDateFormat(HelperMethods.getFieldValue(result, "CurrentLoginTime"), null));
		}
		input.put("Id", HelperMethods.getFieldValue(result, "Id"));
		input.put("CurrentLoginTime", HelperMethods.getCurrentTimeStamp());
		input.put("lockCount", "0");
		input.put("Session_id", retVal.getRecordById("core_security_attributes").getParam("session_token").getValue());
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.COREUSER_UPDATE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateLockCount(DataControllerRequest dcRequest, Record user) throws HttpCallException {
		String id = HelperMethods.getFieldValue(user, "id");
		int count = 1;
		String lockcount = HelperMethods.getFieldValue(user, "lockCount");
		if (StringUtils.isNotBlank(lockcount)) {
			count = Integer.parseInt(lockcount) + 1;
		}
		Map input = new HashMap();
		input.put("id", id);
		input.put("lockCount", String.valueOf(count));
		HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.COREUSER_UPDATE);
	}

	private Record getUser(DataControllerRequest dcRequest, String username) throws HttpCallException {
		String filter = "UserName" + DBPUtilitiesConstants.EQUAL + username;
		Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.USER_GET);
		if (HelperMethods.hasRecords(result)) {
			return result.getAllDatasets().get(0).getRecord(0);
		}
		return null;
	}

	private boolean isUserLocked(Record user) {
		String lockcount = HelperMethods.getFieldValue(user, "lockCount");
		if (StringUtils.isNotBlank(lockcount)) {
			int count = Integer.parseInt(lockcount);
			return count >= 4;
		}
		return false;
	}

	private Result sessionAttributes(Result result) {
		Result retVal = new Result();
		Dataset ds = result.getAllDatasets().get(0);
		Record sessionAttr = new Record();
		sessionAttr.setId("core_security_attributes");

		Record usrAttr = ds.getRecord(0);
		usrAttr.setId("core_user_attributes");
		usrAttr.addParam(new Param("user_id", HelperMethods.getFieldValue(usrAttr, "Id"), "String"));
		usrAttr.addParam(new Param("userName", HelperMethods.getFieldValue(usrAttr, "userName"), "String"));
		usrAttr.addParam(new Param("lastlogintime", HelperMethods.getFieldValue(usrAttr, "lastlogintime"), "String"));
		usrAttr.addParam(new Param("userfirstname", HelperMethods.getFieldValue(usrAttr, "userFirstName"), "String"));
		usrAttr.addParam(new Param("userlastname", HelperMethods.getFieldValue(usrAttr, "userLastName"), "String"));
		usrAttr.addParam(new Param("countryCode", HelperMethods.getFieldValue(usrAttr, "CountryCode"), "String"));
		String token = SessionManager.createSession(HelperMethods.getFieldValue(usrAttr, "Id"));
		Param sessionId = new Param("session_token", token, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		Param sessionTtl = new Param("session_ttl", null, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		sessionAttr.addParam(sessionId);
		sessionAttr.addParam(sessionTtl);
		retVal.addRecord(usrAttr);
		retVal.addRecord(sessionAttr);

		return retVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		String username = (String) inputParams.get(DBPInputConstants.USR_NAME);
		String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
		String pin = (String) inputParams.get(DBPUtilitiesConstants.PIN);
		String sharedSecret = (String) inputParams.get("sharedSecret");
		String csrUserName = (String) inputParams.get("CSRUserName");
		String sessionToken = (String) inputParams.get("session_token");

		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(password) && !"$password".equalsIgnoreCase(password)) {
			sb.append("userName").append(DBPUtilitiesConstants.EQUAL).append(username).append(DBPUtilitiesConstants.AND)
					.append("passWord").append(DBPUtilitiesConstants.EQUAL).append("'" + password + "'");
		} else if (StringUtils.isNotBlank(pin) && !"$pin".equalsIgnoreCase(pin)) {
			sb.append("userName").append(DBPUtilitiesConstants.EQUAL).append(username);
		} else if (StringUtils.isNotBlank(sharedSecret) && !"$sharedSecret".equalsIgnoreCase(sharedSecret)
				&& (StringUtils.isBlank(csrUserName) || "$CSRUserName".equals(csrUserName))) {
			sb.append("userName").append(DBPUtilitiesConstants.EQUAL).append("admin");
		} else if (StringUtils.isNotBlank(sharedSecret) && !"$sharedSecret".equalsIgnoreCase(sharedSecret)) {
			sb.append("userName").append(DBPUtilitiesConstants.EQUAL).append(csrUserName);
		} else if (StringUtils.isNotBlank(sessionToken) && !"$session_token".equalsIgnoreCase(sessionToken)
				&& (StringUtils.isBlank(password) || "$password".equalsIgnoreCase(password))
				&& (StringUtils.isBlank(sharedSecret) || "$sharedSecret".equalsIgnoreCase(sharedSecret))
				&& (StringUtils.isBlank(pin) || "$pin".equalsIgnoreCase(pin))) {
			sb.append("Session_id").append(DBPUtilitiesConstants.EQUAL).append(sessionToken);
		} else {
			HelperMethods.setValidationMsg("Incorrect details.", dcRequest, result);
			status = false;
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		return status;
	}

	private void pushCustomMetricData(DataControllerRequest dcRequest, String metricName, String metricValue)
			throws MetricsException, ParseException, Exception {
		DBPMetricsProcessorHelper helper = new DBPMetricsProcessorHelper();
		List<DBPMetricData> custMetrics = new ArrayList<>();
		custMetrics.add(new DBPMetricData(metricName, metricValue, DBPMetricData.STRING));
		helper.addCustomMetrics(dcRequest, custMetrics, false);
	}
}