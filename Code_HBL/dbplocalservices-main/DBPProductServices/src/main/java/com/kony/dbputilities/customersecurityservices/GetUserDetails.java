package com.kony.dbputilities.customersecurityservices;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
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

public class GetUserDetails implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		SessionScope.clear(dcRequest);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERPREFERENCES_GET);
		}
		if (HelperMethods.hasRecords(result)) {
			result = postProcess(dcRequest, result);
		}

		return result;
	}

	private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		if (HelperMethods.hasRecords(result)) {
			result.getAllDatasets().get(0).setId("customer");
			addCurrencyCode(result);
			updateBankName(dcRequest, result);
			updateCustomerCommunication(dcRequest, result);
			createFeedbackStatus(dcRequest, result);
		}
		return result;
	}

	private void createFeedbackStatus(DataControllerRequest dcRequest, Result result) {
		Map<String, String> inputParams = new HashMap<>();
		String userName = HelperMethods.getCustomerFromIdentityService(dcRequest).get("UserName");
		String uuid = UUID.randomUUID().toString();
		String createdts = HelperMethods.getCurrentTimeStamp();
		String status = "false";
		String deviceId = null;
		String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
		if (StringUtils.isNotBlank(reportingParams)) {
			JSONObject reportingParamsJson = null;
			try {
				reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
			} catch (JSONException e) {
				alert.prepareError(e.getMessage()).log();
			} catch (UnsupportedEncodingException e) {
				alert.prepareError(e.getMessage()).log();
			}
			if (null != reportingParamsJson) {
				deviceId = reportingParamsJson.optString("did");
			}
		}
		String filterQuery = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
		if (StringUtils.isNotBlank(deviceId)) {
			filterQuery = filterQuery + DBPUtilitiesConstants.AND + "deviceID" + DBPUtilitiesConstants.EQUAL + deviceId;
		}
		String url = URLConstants.FEEDBACKSTATUS_CREATE;
		Result feedbackStatus = null;
		try {
			feedbackStatus = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
					URLConstants.FEEDBACKSTATUS_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}
		inputParams.put("UserName", userName);
		inputParams.put("feedbackID", uuid);
		inputParams.put("createdts", createdts);
		inputParams.put("status", status);
		inputParams.put("deviceID", deviceId);
		HelperMethods.removeOnlyNullValues(inputParams);
		if (feedbackStatus != null && HelperMethods.hasRecords(feedbackStatus)) {
			url = URLConstants.FEEDBACKSTATUS_UPDATE;
			inputParams.put("id", HelperMethods.getFieldValue(feedbackStatus, "id"));
			try {
				HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
			} catch (HttpCallException e) {
				alert.prepareError(e.getMessage()).log();
			}
		} else {
			url = URLConstants.FEEDBACKSTATUS_CREATE;
			String id = UUID.randomUUID().toString();
			inputParams.put("id", id);
			try {
				HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
			} catch (HttpCallException e) {
				alert.prepareError(e.getMessage()).log();
			}
		}
		Dataset ds = result.getAllDatasets().get(0);
		ds.getRecord(0).addParam(new Param("feedbackUserId", uuid, "String"));
	}

	private void updateCustomerCommunication(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		Record user = result.getAllDatasets().get(0).getRecord(0);
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + HelperMethods.getFieldValue(result, "id");
		Result communications = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_COMMUNICATION_GET);
		Map<String, String> map = HelperMethods.getCommunicationMap();
		if (HelperMethods.hasRecords(communications)) {
			for (Record communication : communications.getAllDatasets().get(0).getAllRecords()) {
				String communication_type = map.get(HelperMethods.getFieldValue(communication, "Type_id"));
				if ("email".equalsIgnoreCase(communication_type)) {
					updateEmail(user, communication);
				} else {
					updatePhone(user, communication);
				}
			}
		}
	}

	private void updatePhone(Record user, Record communication) {
		String isPrimary = HelperMethods.getFieldValue(communication, "isPrimary");
		if ("1".equals(isPrimary) || "true".equalsIgnoreCase(isPrimary)) {
			user.addParam(new Param("phone", HelperMethods.getFieldValue(communication, "Value"), "String"));
		} else if (StringUtils.isBlank(HelperMethods.getFieldValue(user, "secondaryphone"))) {
			user.addParam(new Param("secondaryphone", HelperMethods.getFieldValue(communication, "Value"), "String"));
		} else if (StringUtils.isBlank(HelperMethods.getFieldValue(user, "secondaryphone2"))) {
			user.addParam(new Param("secondaryphone2", HelperMethods.getFieldValue(communication, "Value"), "String"));
		}
	}

	private void updateEmail(Record user, Record communication) {
		String isPrimary = HelperMethods.getFieldValue(communication, "isPrimary");
		if ("1".equals(isPrimary) || "true".equalsIgnoreCase(isPrimary)) {
			user.addParam(new Param("email", HelperMethods.getFieldValue(communication, "Value"), "String"));
		} else if (StringUtils.isBlank(HelperMethods.getFieldValue(user, "secondaryemail"))) {
			user.addParam(new Param("secondaryemail", HelperMethods.getFieldValue(communication, "Value"), "String"));
		} else if (StringUtils.isBlank(HelperMethods.getFieldValue(user, "secondaryemail2"))) {
			user.addParam(new Param("secondaryemail2", HelperMethods.getFieldValue(communication, "Value"), "String"));
		}
	}

	private void updateBankName(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		String bankName = getBankName(dcRequest, HelperMethods.getFieldValue(result, "Bank_id"));
		Dataset ds = result.getAllDatasets().get(0);
		ds.getRecord(0).addParam(new Param("bankName", bankName, "String"));
	}

	private String getBankName(DataControllerRequest dcRequest, String bankId) throws HttpCallException {
		String filter = "id" + DBPUtilitiesConstants.EQUAL + bankId;
		Result bank = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BANK_GET);
		return HelperMethods.getFieldValue(bank, "Description");
	}

	private void addCurrencyCode(Result result) {
		Dataset ds = result.getAllDatasets().get(0);
		String country = HelperMethods.getParamValue(ds.getRecord(0).getParam("CountryCode"));
		Param currrencyCode = new Param(DBPUtilitiesConstants.CURRENCYCODE, HelperMethods.getCurrencyCode(country),
				"String");
		ds.getRecord(0).addParam(currrencyCode);
		try {
			ds.getRecord(0).addParam(new Param("lastlogintime", HelperMethods.convertDateFormat(
					HelperMethods.getFieldValue(result, "lastlogintime"), "yyyy-MM-dd'T'HH:mm:ss"), "String"));
		} catch (ParseException e) {

			alert.prepareError(e.getMessage()).log();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String id = HelperMethods.getCustomerIdFromSession(dcRequest);
		String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		return true;
	}
}